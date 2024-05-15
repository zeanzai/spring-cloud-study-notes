//package me.zeanzai.nacosglobalexception.constant;
//
///**
// * @author shawnwang
// * @version 1.0
// * @describe
// * @date 2023/4/21
// */
//public enum BizExceptionEnum {
//    EE3001("EE3001", new String[]{}),
//    EE4001("EE4001", new String[]{"地址", "address"}),
//    ;
//
//    String code;
//    String[] desc;
//
//    BizExceptionEnum(String code, String[] desc) {
//        this.code = code;
//        this.desc = desc;
//    }
//
//    public static String[] getDescByCode(String code) {
//        for (BizExceptionEnum temp : BizExceptionEnum.values()) {
//            if (code.equals(temp.getCode())) {
//                return temp.getDesc();
//            }
//        }
//        return null;
//    }
//
//
//    public String getCode() {
//        return code;
//    }
//
//    public String[] getDesc() {
//        return desc;
//    }
//}
