package me.zeanzai.nacosglobalexception.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/21
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "placeholder")// prefix 的值就是控制台添加的配置文件的名称
//@PropertySource("placeholder.yml")
public class PlaceHolderConfig {

    private String zh;

    private String en;

}


