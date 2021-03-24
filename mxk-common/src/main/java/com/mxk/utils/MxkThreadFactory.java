package com.mxk.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ThreadFactory;

/**
 * 自定义线程工厂类
 */
public class MxkThreadFactory {

    private String name;

    private Boolean daemon;

    public MxkThreadFactory(String name, Boolean daemon) {
        this.name = name;
        this.daemon = daemon;
    }

    public ThreadFactory create() {
        return new ThreadFactoryBuilder().setNameFormat(this.name + "-%d").setDaemon(this.daemon).build();
    }

}
