package zeanzai.me.member.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zeanzai.me.common.exception.BusinessException;
import zeanzai.me.member.entity.Vehicle;
import zeanzai.me.member.entity.VehicleExample;
import zeanzai.me.member.mapper.VehicleMapper;
import zeanzai.me.member.service.VehicleService;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {


    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public List<Vehicle> list() {
        return vehicleMapper.selectByExample(new VehicleExample());
    }

    @Override
    public int addVehicle(String json) throws BusinessException {
        Vehicle vehicle = JSONObject.parseObject(json, Vehicle.class);
        return vehicleMapper.insertSelective(vehicle);
    }
}
