package me.zeanzai.nacosglobalexception.handler;

import cn.hutool.core.text.StrFormatter;
import me.zeanzai.nacosglobalexception.configs.*;
import me.zeanzai.nacosglobalexception.exceptions.BusinessException;
import me.zeanzai.nacosglobalexception.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@ControllerAdvice
public class GlobalExceptionHandler {

//    @Autowired
//    private MessageSource staticMessageSource;

//    @PostConstruct
//    public void init(){
//        staticMessageSource = messageSource;
//    }


    /**
     * 业务逻辑异常。
     *  HTTP响应状态为200
     * @param businessException
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity businessExceptionHandler(BusinessException businessException) {
        ResponseResult result = businessException.getResult();
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Autowired
    private NacosConfig2 nacosConfig2;
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();

        String message = null;
        CustomMsg customMsg = null;
        PlaceHolderSource placeHolderSource = null;
        if(bindingResult.getFieldErrors() != null){
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                String defaultMessage = fieldError.getDefaultMessage();

                if(
                        "".equals(LocaleContextHolder.getLocale().getLanguage())
                        | "zh".equals(LocaleContextHolder.getLocale().getLanguage())
                ) {
                    customMsg = nacosConfig2.getLocalCatchConfig(ENacosJsonDefinition.MESSAGE_ZH, CustomMsg.class);
                    placeHolderSource = nacosConfig2.getLocalCatchConfig(ENacosJsonDefinition.CODE_PLACEHOLD, PlaceHolderSource.class);


                } else {
                    customMsg = nacosConfig2.getLocalCatchConfig(ENacosJsonDefinition.MESSAGE_US, CustomMsg.class);
                    placeHolderSource = nacosConfig2.getLocalCatchConfig(ENacosJsonDefinition.CODE_PLACEHOLD, PlaceHolderSource.class);
                }

                // 根据 defaultMessage 获取语言下的对应的msg
                List<CustomMessageSource> customMessageSources = customMsg.getCustomMessageSources();
                List<PlaceHolder> placeHolders = placeHolderSource.getPlaceHolders();
                for (CustomMessageSource tem : customMessageSources) {
                    if (defaultMessage.equals(tem.getCode())) {
                        for (PlaceHolder placeHolder : placeHolders) {
                            if (defaultMessage.equals(placeHolder.getCode())) {
                                // 组装 placeholder
                                message = StrFormatter.format(tem.getMsg(), placeHolder.getPlaceholder());
                                break;
                            }
                        }
                        break;
                    }

                }

//                String[] desc = BizExceptionEnum.getDescByCode(defaultMessage);
//                String message = staticMessageSource.getMessage(defaultMessage, desc, LocaleContextHolder.getLocale());

                BusinessException businessException = new BusinessException(defaultMessage, message);
                ResponseResult result = businessException.getResult();
                return new ResponseEntity(result, HttpStatus.OK);
            }
        }
        return new ResponseEntity(null, HttpStatus.OK);
    }
}
