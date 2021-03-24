package com.mxk.chain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.Assert;


/**
 * Created by 2YSP on 2020/12/26
 */
public class MxkResponseUtil {

    private static Gson gson = new GsonBuilder().create();

    /**
     * @param exchange
     * @param resp
     */
    public static reactor.core.publisher.Mono<Void> doResponse(org.springframework.web.server.ServerWebExchange exchange, String resp) {
        Assert.notNull(resp, "response object can't be null");
        exchange.getResponse().getHeaders().setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        return exchange.getResponse().writeWith(reactor.core.publisher.Mono.just(exchange.getResponse()
                .bufferFactory().wrap(resp.getBytes())));
    }

}
