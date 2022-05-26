package zeanzai.me.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import zeanzai.me.common.entity.CommonResult;
import zeanzai.me.common.exception.BusinessException;
import zeanzai.me.member.entity.Vehicle;
import zeanzai.me.member.service.VehicleService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<Integer> addVehicle(String json) throws BusinessException {
        log.debug("add vehicle = " + json);
        CommonResult<Integer> result = new CommonResult<>();
        int rtn = vehicleService.addVehicle(json);
        result.setRespData(rtn);
        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Vehicle> list() throws BusinessException {
        return vehicleService.list();
    }
}
