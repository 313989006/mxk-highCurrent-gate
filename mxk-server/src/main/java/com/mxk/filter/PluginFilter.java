package com.mxk.filter;

import com.mxk.cache.ServiceCache;
import com.mxk.chain.PluginChain;
import com.mxk.config.ServerConfigProperties;
import com.mxk.constants.MxkExceptionEnum;
import com.mxk.exception.MxkException;
import com.mxk.plugin.AuthPlugin;
import com.mxk.plugin.DynamicRoutePlugin;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.*;
import org.springframework.web.server.ServerWebExchange;

/**
 *
 */
public class PluginFilter implements WebFilter {

    private ServerConfigProperties properties;

    public PluginFilter(ServerConfigProperties properties) {
        this.properties = properties;
    }

    @Override
    public reactor.core.publisher.Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String appName = parseAppName(exchange);
        if (CollectionUtils.isEmpty(ServiceCache.getAllInstances(appName))) {
            throw new MxkException(MxkExceptionEnum.SERVICE_NOT_FIND);
        }
        PluginChain pluginChain = new PluginChain(properties, appName);
        pluginChain.addPlugin(new DynamicRoutePlugin(properties));
        pluginChain.addPlugin(new AuthPlugin(properties));
        return pluginChain.execute(exchange, pluginChain);
    }

    private String parseAppName(ServerWebExchange exchange) {
        org.springframework.http.server.RequestPath path = exchange.getRequest().getPath();
        String appName = path.value().split("/")[1];
        return appName;
    }
}
