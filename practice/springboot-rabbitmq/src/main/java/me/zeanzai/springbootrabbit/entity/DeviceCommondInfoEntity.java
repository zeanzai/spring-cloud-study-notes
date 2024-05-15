package me.zeanzai.springbootrabbit.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/11
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DeviceCommondInfoEntity implements Serializable {

    private String id;
    private String deviceTypeNo;
    private String deviceTypeName;
    private String deviceNo;
    private String deviceName;
    private String cmdInfo;

}
