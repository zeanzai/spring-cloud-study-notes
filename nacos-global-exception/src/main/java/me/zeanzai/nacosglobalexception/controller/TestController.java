package me.zeanzai.nacosglobalexception.controller;

import me.zeanzai.nacosglobalexception.configs.PlaceHolderConfig;
import me.zeanzai.nacosglobalexception.constant.BizExceptionConstant;
import me.zeanzai.nacosglobalexception.entity.request.CustomRequestDo;
import me.zeanzai.nacosglobalexception.utils.ExecptionUtils;
import me.zeanzai.nacosglobalexception.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/result")
    public ResponseResult<String> result(){
//        ResponseResult responseResult = new ResponseResult();
//        responseResult.setCode("code");
//        responseResult.setMsg("ssss");
//        responseResult.setData("object");
        ResponseResult responseResult = ResponseResult.builder()
                .code("code")
                .msg("mess")
                .data("tttt").build();

        return responseResult;
    }

    @GetMapping("/throwexception")
    public String throwexception(){

        if (!"mybrother".equals("mmmmm")) {

            throw ExecptionUtils.businessException("EE3001");
        }
        return "dddd";

    }

    @GetMapping("/throwexception1")
    public String throwexception1(){
        if (!"mybrother".equals("mmmmm")) {

            throw ExecptionUtils.businessException(BizExceptionConstant.EE3001);
        }
        return "dddd";
    }

    @GetMapping("/throwexception2")
    public String throwexception2(){
        if (!"mybrother".equals("mmmmm")) {

            throw ExecptionUtils.businessException("EE4001", "tttt");
        }
        return "dddd";
    }

    @GetMapping("/throwexception3")
    public String throwexception3(){
        if (!"mybrother".equals("mmmmm")) {

            throw ExecptionUtils.businessException(BizExceptionConstant.EE4001, "tttt");
        }
        return "dddd";
    }

    @PostMapping("/throwexception4")
    public ResponseResult<CustomRequestDo> throwexception4(@RequestBody @Validated CustomRequestDo customRequestDo){

        return ResponseResult.success(customRequestDo);
    }


    @Autowired
    private PlaceHolderConfig placeHolderConfig;
    @GetMapping("/throwexception5")
    public PlaceHolderConfig throwexception5(){

        return placeHolderConfig;
    }
}
