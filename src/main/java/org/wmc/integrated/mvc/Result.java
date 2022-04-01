package org.wmc.integrated.mvc;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private Integer code;

    private String message;

    private Object date;

    public Result(ResultCode resultCode) {
        this.code = resultCode.code();
        this.message = resultCode.message();
    }

    public Result(ResultCode resultCode, Object data) {
        this.code = resultCode.code();
        this.message = resultCode.message();
        this.date = data;
    }

    // 返回成功
    public static Result success() {
        return new Result(ResultCode.SUCCESS);
    }

    // 返回成功
    public static Result success(Object data) {
        return new Result(ResultCode.SUCCESS, data);
    }

    // 返回失败
    public static Result failure(ResultCode resultCode) {
        return new Result(resultCode);
    }

    // 返回成功
    public static Result failure(ResultCode resultCode, Object data) {
        return new Result(resultCode, data);
    }

}
