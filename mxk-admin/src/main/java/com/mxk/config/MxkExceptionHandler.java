package com.mxk.config;

import com.mxk.exception.MxkException;
import com.mxk.pojo.vo.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 *
 */
@RestControllerAdvice
public class MxkExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Void> handlerBusinessException(Exception exception) {
        return Result.error(transferToMxkException(exception));
    }

    private MxkException transferToMxkException(Exception exception) {
        MxkException mxkException;
        if (exception instanceof MxkException) {
            mxkException = (MxkException) exception;

        } else if (exception instanceof BindException) {
            BindException bindException = (BindException) exception;
            BindingResult bindingResult = bindException.getBindingResult();
            mxkException = new MxkException(getErrorMsg(bindingResult));

        } else if (exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException validException = (MethodArgumentNotValidException) exception;
            BindingResult bindingResult = validException.getBindingResult();
            mxkException = new MxkException(getErrorMsg(bindingResult));

        } else {
            mxkException = new MxkException(exception.getMessage());
        }
        return mxkException;
    }

    private String getErrorMsg(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        fieldErrors.forEach(fieldError -> {
            sb.append(fieldError.getDefaultMessage());
            sb.append("-");
        });
        return sb.toString();
    }


}
