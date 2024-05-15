package me.zeanzai.nacosglobalexception.configs;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
public class CustomMessageSource{

    /**
     [{
     "code": "E40001",
     "msg": "用户名{0}不能为空, 姓名{1}不能为空"
     },
     {
     "code": "E40004",
     "msg": "用户名a{0}不能为空, 姓名b{1}不能为空"
     }]
     */


    private String code;
    private String msg;

    public CustomMessageSource(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


}
