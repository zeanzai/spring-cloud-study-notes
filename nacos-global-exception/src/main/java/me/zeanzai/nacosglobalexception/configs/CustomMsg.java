package me.zeanzai.nacosglobalexception.configs;

import lombok.Data;

import java.util.List;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */

@Data
public class CustomMsg {

    private List<CustomMessageSource> customMessageSources;

}
