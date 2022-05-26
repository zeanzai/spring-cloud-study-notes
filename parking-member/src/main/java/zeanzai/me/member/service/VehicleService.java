package zeanzai.me.member.service;

import zeanzai.me.common.exception.BusinessException;
import zeanzai.me.member.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    List<Vehicle> list();

    int addVehicle(String json) throws BusinessException;
}
