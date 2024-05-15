//package me.zeanzai.nacosglobalexception.configs;
//
//import com.alibaba.nacos.api.NacosFactory;
//import com.alibaba.nacos.api.PropertyKeyConst;
//import com.alibaba.nacos.api.config.ConfigService;
//import com.alibaba.nacos.api.config.listener.Listener;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Locale;
//import java.util.Properties;
//import java.util.concurrent.Executor;
//
///**
// * @author shawnwang
// * @version 1.0
// * @describe
// * @date 2023/4/7
// */
//@Slf4j
//@Component
//public class NacosConfig {
//
//    /**
//     * 应用名称
//     */
//    @Value("${spring.application.name}")
//    private String applicationName;
//
//    /**
//     * 服务器地址
//     */
//    @Value("${spring.cloud.nacos.config.server-addr}")
//    private String serverAddr;
//
//    /**
//     * 命名空间
//     */
//    @Value("${spring.cloud.nacos.config.namespace}")
//    private String dNamespace;
//
//    @Value("${spring.cloud.nacos.config.group}")
//    private String group;
//
//
//    @Autowired
//    private MessageConfig messageConfig;
//
//
//    /**
//     * init 静态获取bean的方式；
//     *      https://developer.aliyun.com/article/1133692
//     *      https://cloud.tencent.com/developer/article/1895540
//     */
//    @Autowired
//    public void init() {
//        initTip(null);
//        initTip(Locale.CHINA);
//        initTip(Locale.US);
//        log.info("init {} success", messageConfig.getBasename() + ".properties");
//    }
//
//    private void initTip(Locale locale) {
//        String content = null;
//        String dataId = null;
//        ConfigService configService = null;
//        try {
//            if (locale == null) {
//                dataId = messageConfig.getBasename() + ".properties";
//            } else {
//                dataId = messageConfig.getBasename() + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties";
//            }
//            Properties properties = new Properties();
//            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
//            properties.put(PropertyKeyConst.NAMESPACE, dNamespace);
//            configService = NacosFactory.createConfigService(properties);
//
//            content = configService.getConfig(dataId, group, 5000);
//            if (StringUtils.isEmpty(content)) {
//                log.warn("config is null! dataId: {}", dataId);
//                return;
//            }
//            log.info("init config: {}, content: {}", dataId, content);
//            saveAsFileWriter(dataId, content);
//            setListener(configService, dataId, locale);
//        } catch (Exception e) {
//            log.error("init config error: {}", e);
//        }
//    }
//
//    private void setListener(ConfigService configService, String dataId, Locale locale) throws com.alibaba.nacos.api.exception.NacosException {
//        configService.addListener(dataId, group, new Listener() {
//            @Override
//            public void receiveConfigInfo(String configInfo) {
//                log.info("receive config properties : {}", configInfo);
//                try {
//                    initTip(locale);
//                } catch (Exception e) {
//                    log.error("init config properties error {}", e);
//                }
//            }
//
//            @Override
//            public Executor getExecutor() {
//                return null;
//            }
//        });
//    }
//
//    private void saveAsFileWriter(String fileName, String content) {
//        String path = System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder();
//        try {
//            fileName = path + File.separator + fileName;
//            File file = new File(fileName);
//            FileUtils.writeStringToFile(file, content);
//            log.info("save config file to {}", fileName);
//        } catch (IOException e) {
//            log.error("init config properties error: {} , message: {}", fileName, e);
//        }
//    }
//
//}
