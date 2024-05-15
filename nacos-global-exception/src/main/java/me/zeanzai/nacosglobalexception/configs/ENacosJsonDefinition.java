package me.zeanzai.nacosglobalexception.configs;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
public enum ENacosJsonDefinition {


    /**
     {
        "customMessageSources": [{
                 "code": "E40001",
                 "msg": "用户名{0}不能为空, 姓名{1}不能为空"
             },
             {
                 "code": "E40004",
                 "msg": "用户名a{0}不能为空, 姓名b{1}不能为空"
             }
            ]
     }

     {
        "placeHolders": [{
            "code": "E40001",
            "placeholder": [
                "place01",
                "place02"
                ]
            }
        ]
     }
     */

    CODE_PLACEHOLD("code_placeholder", PlaceHolderSource.class,"占位符"),
    MESSAGE_ZH("message_zh",
            CustomMsg.class,
            "中文描述"),
    MESSAGE_US("message_us",
            CustomMsg.class,
            "英文描述");

    private String dataId;
    private Class cls; // 需要转换成的对象
    private String desc;

    ENacosJsonDefinition(String dataId, Class cls, String desc) {
        this.dataId = dataId;
        this.cls = cls;
        this.desc = desc;
    }

    public String getDataId() {
        return dataId;
    }

    public Class getCls() {
        return cls;
    }

    public String getDesc() {
        return desc;
    }


}
