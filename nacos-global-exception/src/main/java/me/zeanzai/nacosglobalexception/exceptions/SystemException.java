package me.zeanzai.nacosglobalexception.exceptions;

import me.zeanzai.nacosglobalexception.utils.ResponseResult;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
public class SystemException extends BaseException{
    private ResponseResult result = new ResponseResult();

    public SystemException(ResponseResult result) {
        super(result.getCode()+ ":" + result.getMsg());
        this.result = result;
    }

    public SystemException(String code, String msg) {
        super(code + ":" + msg);
        this.result.setCode(code);
        this.result.setMsg(msg);
    }

    public SystemException(ResponseResult result, Throwable cause) {
        super(result.getCode() + ":" + result.getMsg(), cause);
        this.result = result;
    }

    public SystemException(String code, String msg, Throwable cause) {
        super(code + ":" + msg, cause);
        this.result.setCode(code);
        this.result.setMsg(msg);
    }

    public ResponseResult getResult() {
        return result;
    }

    public void setResult(ResponseResult result) {
        this.result = result;
    }
}

