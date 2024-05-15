package me.zeanzai.nacosglobalexception.exceptions;

import me.zeanzai.nacosglobalexception.utils.ResponseResult;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
public class BusinessException extends BaseException {
    private ResponseResult responseResult = new ResponseResult();

    public BusinessException(ResponseResult responseResult) {
        super(responseResult.getCode()+":"+responseResult.getMsg());
        this.responseResult = responseResult;
    }

    public BusinessException(String code, String msg) {
        super(code + ":" + msg);
        this.responseResult.setCode(code);
        this.responseResult.setMsg(msg);
    }

    public BusinessException(ResponseResult responseResult, Throwable cause) {
        super(responseResult.getCode() + ":" + responseResult.getMsg(), cause);
        this.responseResult = responseResult;
    }

    public BusinessException(String code, String msg, Throwable cause) {
        super(code + ":" + msg, cause);
        this.responseResult.setCode(code);
        this.responseResult.setMsg(msg);
    }

    public ResponseResult getResult() {
        return responseResult;
    }

    public void setResult(ResponseResult responseResult) {
        this.responseResult = responseResult;
    }
}
