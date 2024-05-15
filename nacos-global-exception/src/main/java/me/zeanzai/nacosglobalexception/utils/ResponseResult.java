package me.zeanzai.nacosglobalexception.utils;

import lombok.*;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResponseResult<T> {

    private String code;

    private String msg;

    private T data;

    public ResponseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static <T> ResponseResult response(String code, String msg, T data) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setData(data);
        responseResult.setMsg(msg);
        return responseResult;
    }

    public static <T> ResponseResult success(T data) {
        return response("100", "request success", data);
    }
    public static ResponseResult error(String msg) {
        return response("999", msg, null);
    }
}
