package me.zeanzai.nacosglobalexception.entity.request;

import lombok.Data;
import me.zeanzai.nacosglobalexception.constant.BizExceptionConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
@Data
public class CustomRequestDo {

    @NotEmpty(message = BizExceptionConstant.EE3001)
    private String name;

    @NotBlank(message = BizExceptionConstant.EE4001)
    private String address;
    private Integer age;
    private Long phonenum;


}
