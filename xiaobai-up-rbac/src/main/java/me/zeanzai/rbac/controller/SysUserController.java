package me.zeanzai.rbac.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zeanzai.common.exception.BusinessException;
import me.zeanzai.rbac.entity.SysUser;
import me.zeanzai.rbac.entity.SysUserExample;
import me.zeanzai.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sysuser")
@Api(value = "用户管理", tags = { "用户管理" })
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/list")
    @ApiOperation(value = "查询", tags = {"标签"}, notes = "查询所有用户")
    public List<SysUser> list()  throws BusinessException {
        SysUserExample sysUserExample = new SysUserExample();

        return sysUserService.selectByExample(sysUserExample);
    }

}
