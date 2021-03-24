package com.mxk.constants;

/**
 * Object 匹配枚举类
 */
public enum MatchObjectEnum {
    /**
     * DEFAULT
     */
    DEFAULT("DEFAULT", "默认"),
    /**
     * QUERY
     */
    QUERY("QUERY", "参数"),
    /**
     * HEADER
     */
    HEADER("HEADER", "头部");

    private String code;

    private String desc;

    MatchObjectEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
