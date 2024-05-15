package me.zeanzai.nacosglobalexception.configs;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
@Data
@AllArgsConstructor
public class PlaceHolder {

    /*
    {
        "code": "E40001",
        "placeholder": [
            "place01",
            "place02"
        ]
     }
     */

    private String code;
    private String[] placeholder;


}
