package me.zeanzai.nacosglobalexception.utils;

import me.zeanzai.nacosglobalexception.exceptions.BusinessException;
import me.zeanzai.nacosglobalexception.exceptions.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@Component
public class ExecptionUtils {

//    @Autowired
//    private MessageSource messageSource;

    @Autowired
    private static MessageSource staticMessageSource;

//    @PostConstruct
//    public void init(){
//        staticMessageSource = messageSource;
//    }

    @Autowired
    public void setStaticMessageSource(MessageSource staticMessageSource){
        ExecptionUtils.staticMessageSource = staticMessageSource;
    }


    /**
     * 业务处理异常
     * @param errCode   异常码
     * @return
     */
    public static BusinessException businessException(String errCode) {
        return new BusinessException(createResult(errCode));
    }

    /**
     * 业务处理异常
     * @param errCode   异常码
     * @param args  错误描述信息中的参数
     * @return
     */
    public  static BusinessException businessException(String errCode, String... args) {
        return new BusinessException(createResult(errCode, args));
    }

    /**
     * 系统级异常
     * @param errCode   异常码
     * @return
     */
    public  static SystemException systemException(String errCode) {
        return new SystemException(createResult(errCode));
    }

    /**
     * 系统级异常
     * @param errCode   异常码
     * @param args  错误描述信息中的参数
     * @return
     */
    public SystemException systemException(String errCode, String... args) {
        return new SystemException(createResult(errCode, args));
    }


    private static  ResponseResult createResult(String errCode) {
        return new ResponseResult(errCode, getErrorMsg(errCode));
    }

    private static  ResponseResult createResult(String errCode, String msg) {
        return new ResponseResult(errCode, msg);
    }

    private static  ResponseResult createResult(String errCode, String[] args) {
        return new ResponseResult(errCode, getErrorMsg(errCode, args));
    }

    /**
     * 获取错误信息
     * @param errCode   错误码
     * @return
     */
    private static String getErrorMsg(String errCode) {
        return staticMessageSource.getMessage(errCode,null, LocaleContextHolder.getLocale());
    }

    /**
     * 获取错误信息
     * @param errCode   错误码
     * @param args  错误描述信息中的参数
     * @return
     */
    private  static String getErrorMsg(String errCode, String[] args) {
        return staticMessageSource.getMessage(errCode, args, LocaleContextHolder.getLocale());
    }
}
