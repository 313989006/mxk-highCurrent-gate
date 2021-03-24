package com.mxk.plugin;


import com.mxk.chain.PluginChain;
import com.mxk.config.ServerConfigProperties;
import com.mxk.constants.MxkPluginEnum;

/**
 *
 */
public class AuthPlugin extends AbstractMxkPlugin {

    public AuthPlugin(ServerConfigProperties properties) {
        super(properties);
    }

    @Override
    public Integer order() {
        return MxkPluginEnum.AUTH.getOrder();
    }

    @Override
    public String name() {
        return MxkPluginEnum.AUTH.getName();
    }

    @Override
    public reactor.core.publisher.Mono<Void> execute(org.springframework.web.server.ServerWebExchange exchange, PluginChain pluginChain) {
        System.out.println("auth plugin");
        return pluginChain.execute(exchange, pluginChain);
    }
}
