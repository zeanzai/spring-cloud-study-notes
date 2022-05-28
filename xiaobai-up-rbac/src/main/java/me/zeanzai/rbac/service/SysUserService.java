package me.zeanzai.rbac.service;

import me.zeanzai.common.exception.BusinessException;
import me.zeanzai.rbac.entity.SysUser;
import me.zeanzai.rbac.entity.SysUserExample;

import java.util.List;

public interface SysUserService {
    int insert(SysUser record) throws BusinessException;
    List<SysUser> selectByExample(SysUserExample example) throws BusinessException;
}
