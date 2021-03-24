package com.mxk.exception;


import com.mxk.constants.MxkExceptionEnum;

/**
 * Created by 2YSP on 2020/12/23
 */
public final class MxkException extends RuntimeException {

    private Integer code;

    private String errMsg;

    public MxkException(MxkExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMsg());
        this.code = exceptionEnum.getCode();
        this.errMsg = exceptionEnum.getMsg();
    }

    public MxkException(String errMsg) {
        super(errMsg);
        this.errMsg = errMsg;
        this.code = 5000;
    }

    public MxkException(Integer code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }


    public Integer getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
