package me.zeanzai.rbac.service.impl;

import me.zeanzai.common.exception.BusinessException;
import me.zeanzai.rbac.entity.SysUser;
import me.zeanzai.rbac.entity.SysUserExample;
import me.zeanzai.rbac.mapper.SysUserMapper;
import me.zeanzai.rbac.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public int insert(SysUser record) throws BusinessException {
        return sysUserMapper.insert(record);
    }

    @Override
    public List<SysUser> selectByExample(SysUserExample example) throws BusinessException {
        return sysUserMapper.selectByExample(example);
    }
}
