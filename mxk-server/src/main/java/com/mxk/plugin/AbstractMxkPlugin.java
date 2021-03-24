package com.mxk.plugin;


import com.mxk.config.ServerConfigProperties;

/**
 *
 */
public abstract class AbstractMxkPlugin implements MxkPlugin {

    protected ServerConfigProperties properties;

    public AbstractMxkPlugin(ServerConfigProperties properties) {
        this.properties = properties;
    }
}
