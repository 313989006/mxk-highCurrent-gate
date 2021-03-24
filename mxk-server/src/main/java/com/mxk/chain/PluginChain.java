package com.mxk.chain;


import com.mxk.cache.PluginCache;
import com.mxk.config.ServerConfigProperties;
import com.mxk.plugin.AbstractMxkPlugin;
import com.mxk.plugin.MxkPlugin;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 */
public class PluginChain extends AbstractMxkPlugin {
    /**
     * the pos point to current plugin
     */
    private int pos;
    /**
     * the plugins of chain
     */
    private java.util.List<MxkPlugin> plugins;

    private final String appName;

    public PluginChain(ServerConfigProperties properties, String appName) {
        super(properties);
        this.appName = appName;
    }

    /**
     * add enabled plugin to chain
     *
     * @param mxkPlugin
     */
    public void addPlugin(MxkPlugin mxkPlugin) {
        if (plugins == null) {
            plugins = new ArrayList<>();
        }
        if (!PluginCache.isEnabled(appName, mxkPlugin.name())) {
            return;
        }
        plugins.add(mxkPlugin);
        // order by the plugin's order
        plugins.sort(Comparator.comparing(MxkPlugin::order));
    }

    @Override
    public Integer order() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public reactor.core.publisher.Mono<Void> execute(org.springframework.web.server.ServerWebExchange exchange, PluginChain pluginChain) {
        if (pos == plugins.size()) {
            return exchange.getResponse().setComplete();
        }
        return pluginChain.plugins.get(pos++).execute(exchange, pluginChain);
    }

    public String getAppName() {
        return appName;
    }

}
