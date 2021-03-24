package com.mxk.plugin;


import com.mxk.chain.PluginChain;

/**
 * 自定义插件
 */
public interface MxkPlugin {
    /**
     * lower values have higher priority
     *
     * @return
     */
    Integer order();

    /**
     * return current plugin name
     *
     * @return
     */
    String name();

    reactor.core.publisher.Mono<Void> execute(org.springframework.web.server.ServerWebExchange exchange, PluginChain pluginChain);

}
