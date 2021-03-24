package com.mxk.constants;

/**
 * 自定义插件枚举类
 */
public enum MxkPluginEnum {
    /**
     * 动态路由
     */
    DYNAMIC_ROUTE("DynamicRoute", 2, "动态路由插件"),
    /**
     * 鉴权
     */
    AUTH("Auth",1,"鉴权插件");

    private String name;

    private Integer order;

    private String desc;

    MxkPluginEnum(String name, Integer order, String desc) {
        this.name = name;
        this.order = order;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
