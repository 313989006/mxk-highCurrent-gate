package com.mxk.pojo.vo;

import com.mxk.exception.MxkException;

import java.io.Serializable;

/**
 * 通用Rest接口结果类
 */
public class Result<T> implements Serializable {

    private int code;

    private String message;

    private T data;

    private Result() {
    }

    /**
     * return the success result
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * return the success result without data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    /**
     * return the fail result
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> fail() {
        Result<T> result = new Result();
        result.setCode(500);
        result.setMessage("fail");
        return result;
    }


    public static <T> Result<T> error(MxkException mxkException) {
        Result<T> result = new Result();
        result.setCode(mxkException.getCode());
        result.setMessage(mxkException.getMessage());
        return result;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
