package com.mxk.plugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mxk.cache.LoadBalanceFactory;
import com.mxk.cache.RouteRuleCache;
import com.mxk.cache.ServiceCache;
import com.mxk.chain.MxkResponseUtil;
import com.mxk.chain.PluginChain;
import com.mxk.config.ServerConfigProperties;
import com.mxk.constants.MatchObjectEnum;
import com.mxk.constants.MxkExceptionEnum;
import com.mxk.constants.MxkPluginEnum;
import com.mxk.exception.MxkException;
import com.mxk.pojo.dto.AppRuleDTO;
import com.mxk.pojo.dto.ServiceInstance;
import com.mxk.spi.LoadBalance;
import com.mxk.utils.StringTools;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 动态路由插件
 */
public class DynamicRoutePlugin extends AbstractMxkPlugin {

    private final static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(DynamicRoutePlugin.class);

    private static WebClient webClient;

    private static final Gson gson = new GsonBuilder().create();

    static {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client ->
                        client.doOnConnected(conn ->
                                conn.addHandlerLast(new ReadTimeoutHandler(3))
                                        .addHandlerLast(new WriteTimeoutHandler(3)))
                                .option(ChannelOption.TCP_NODELAY, true)
                );
        webClient = WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public DynamicRoutePlugin(ServerConfigProperties properties) {
        super(properties);
    }

    @Override
    public Integer order() {
        return MxkPluginEnum.DYNAMIC_ROUTE.getOrder();
    }

    @Override
    public String name() {
        return MxkPluginEnum.DYNAMIC_ROUTE.getName();
    }

    @Override
    public Mono<Void> execute(org.springframework.web.server.ServerWebExchange exchange, PluginChain pluginChain) {
        String appName = pluginChain.getAppName();
        ServiceInstance serviceInstance = chooseInstance(appName, exchange.getRequest());
//        LOGGER.info("selected instance is [{}]", gson.toJson(serviceInstance));
        // request service
        String url = buildUrl(exchange, serviceInstance);
        return forward(exchange, url);
    }

    /**
     * forward request to backend service
     *
     * @param exchange
     * @param url
     * @return
     */
    private Mono<Void> forward(org.springframework.web.server.ServerWebExchange exchange, String url) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        HttpMethod method = request.getMethod();

        WebClient.RequestBodySpec requestBodySpec = webClient.method(method).uri(url).headers((headers) -> {
            headers.addAll(request.getHeaders());
        });

        WebClient.RequestHeadersSpec<?> reqHeadersSpec;
        if (requireHttpBody(method)) {
            reqHeadersSpec = requestBodySpec.body(BodyInserters.fromDataBuffers(request.getBody()));
        } else {
            reqHeadersSpec = requestBodySpec;
        }
        // nio->callback->nio
        return reqHeadersSpec.exchange().timeout(Duration.ofMillis(properties.getTimeOutMillis()))
                .onErrorResume(ex -> {
                    return Mono.defer(() -> {
                        String errorResultJson = "";
                        if (ex instanceof TimeoutException) {
                            errorResultJson = "{\"code\":5001,\"message\":\"network timeout\"}";
                        } else {
                            errorResultJson = "{\"code\":5000,\"message\":\"system error\"}";
                        }
                        return MxkResponseUtil.doResponse(exchange, errorResultJson);
                    }).then(Mono.empty());
                }).flatMap(backendResponse -> {
                    response.setStatusCode(backendResponse.statusCode());
                    response.getHeaders().putAll(backendResponse.headers().asHttpHeaders());
                    return response.writeWith(backendResponse.bodyToFlux(DataBuffer.class));
                });
    }

    /**
     * weather the http method need http body
     *
     * @param method
     * @return
     */
    private boolean requireHttpBody(HttpMethod method) {
        if (method.equals(HttpMethod.POST) || method.equals(HttpMethod.PUT) || method.equals(HttpMethod.PATCH)) {
            return true;
        }
        return false;
    }

    private String buildUrl(org.springframework.web.server.ServerWebExchange exchange, ServiceInstance serviceInstance) {
        ServerHttpRequest request = exchange.getRequest();
        String query = request.getURI().getQuery();
        String path = request.getPath().value().replaceFirst("/" + serviceInstance.getAppName(), "");
        String url = "http://" + serviceInstance.getIp() + ":" + serviceInstance.getPort() + path;
        if (!StringUtils.isEmpty(query)) {
            url = url + "?" + query;
        }
        return url;
    }


    /**
     * choose an ServiceInstance according to route rule config and load balancing algorithm
     *
     * @param appName
     * @param request
     * @return
     */
    private ServiceInstance chooseInstance(String appName, ServerHttpRequest request) {
        List<ServiceInstance> serviceInstances = ServiceCache.getAllInstances(appName);
        if (CollectionUtils.isEmpty(serviceInstances)) {
            LOGGER.error("service instance of {} not find", appName);
            throw new MxkException(MxkExceptionEnum.SERVICE_NOT_FIND);
        }
        String version = matchAppVersion(appName, request);
        if (StringUtils.isEmpty(version)) {
            throw new MxkException("match app version error");
        }
        // filter serviceInstances by version
        List<ServiceInstance> instances = serviceInstances.stream().filter(i -> i.getVersion().equals(version)).collect(Collectors.toList());
        //Select an instance based on the load balancing algorithm
        LoadBalance loadBalance = LoadBalanceFactory.getInstance(properties.getLoadBalance(), appName, version);
        ServiceInstance serviceInstance = loadBalance.chooseOne(instances);
        return serviceInstance;
    }


    private String matchAppVersion(String appName, ServerHttpRequest request) {
        List<AppRuleDTO> rules = RouteRuleCache.getRules(appName);
        rules.sort(Comparator.comparing(AppRuleDTO::getPriority).reversed());
        for (AppRuleDTO rule : rules) {
            if (match(rule, request)) {
                return rule.getVersion();
            }
        }
        return null;
    }


    private boolean match(AppRuleDTO rule, ServerHttpRequest request) {
        String matchObject = rule.getMatchObject();
        String matchKey = rule.getMatchKey();
        String matchRule = rule.getMatchRule();
        Byte matchMethod = rule.getMatchMethod();
        if (MatchObjectEnum.DEFAULT.getCode().equals(matchObject)) {
            return true;
        } else if (MatchObjectEnum.QUERY.getCode().equals(matchObject)) {
            String param = request.getQueryParams().getFirst(matchKey);
            if (!StringUtils.isEmpty(param)) {
                return StringTools.match(param, matchMethod, matchRule);
            }
        } else if (MatchObjectEnum.HEADER.getCode().equals(matchObject)) {
            HttpHeaders headers = request.getHeaders();
            String headerValue = headers.getFirst(matchKey);
            if (!StringUtils.isEmpty(headerValue)) {
                return StringTools.match(headerValue, matchMethod, matchRule);
            }
        }
        return false;
    }

}
