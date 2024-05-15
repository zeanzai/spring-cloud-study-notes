package me.zeanzai.nacosglobalexception.configs;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
@Slf4j
@Component
public class NacosConfig2 implements InitializingBean {

    /**
     * 应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * 服务器地址
     */
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;

    /**
     * 命名空间
     */
    @Value("${spring.cloud.nacos.config.namespace}")
    private String dNamespace;

    @Value("${spring.cloud.nacos.config.group}")
    private String group;


//    @Autowired
//    private MessageConfig messageConfig;

    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    private ConfigService configService;

    private Map<String, Object> localCatchMap = new HashMap<>(16);


    @Override
    public void afterPropertiesSet() throws Exception {

        // 获取 Nacos 服务端 具体配置
        String serverAddr = nacosConfigProperties.getServerAddr();
        String namespace = nacosConfigProperties.getNamespace();

        // 封装 Nacos server 配置参数
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, namespace);
        if (configService == null) {
            configService = NacosFactory.createConfigService(properties);
        }

        ENacosJsonDefinition[] nacosJsonDefinitions = ENacosJsonDefinition.values();
        for (ENacosJsonDefinition definition : nacosJsonDefinitions) {
            String dataId = definition.getDataId();
            String config = configService.getConfig(dataId, group, 1000);
            this.jsonToObject(config, definition);

            // 创建监听对象
            Listener listener = new NacosJsonConfigDataListener(definition);
            // 配置 具体配置项的监听
            configService.addListener(definition.getDataId(), group, listener);
        }
//        initTip(null);
//        initTip(Locale.CHINA);
//        initTip(Locale.US);
    }

    public void jsonToObject(String json, ENacosJsonDefinition eNacosJsonDefinition) {
        Object data = JSON.parseObject(json, eNacosJsonDefinition.getCls());
        log.info("Get Nacos Config Data, dataId = {} --- data = {}",
                eNacosJsonDefinition.getDataId(), data);
        this.localCatchMap.put(eNacosJsonDefinition.getDataId(), data);
    }

    // 配置用于异步监听的线程池
    private static ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    2,
                    4,
                    1,
                    TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(100),
                    new ThreadPoolExecutor.CallerRunsPolicy()
            );

    // 具体监听类 动态监听Nacos配置
    public class NacosJsonConfigDataListener implements Listener {
        private ENacosJsonDefinition eNacosJsonDefinition;
        private NacosJsonConfigDataListener(ENacosJsonDefinition eNacosJsonDefinition) {
            this.eNacosJsonDefinition = eNacosJsonDefinition;
        }

        // 配置线程执行器 用于异步监听
        @Override
        public Executor getExecutor() {
            return executor;
        }

        // 动态监听到变动后的处理逻辑
        @Override
        public void receiveConfigInfo(String jsonStr) {
            jsonToObject(jsonStr, eNacosJsonDefinition);
        }
    }

    // 根据枚举 及 类型 获取 配置实体
    public <T> T getLocalCatchConfig(ENacosJsonDefinition eNacosJsonDefinition, Class<? extends T> cls) {
        Object data = this.localCatchMap.get(eNacosJsonDefinition.getDataId());
        // 判断data这个对象能不能被转化为cls这个类
        boolean instance = cls.isInstance(data);
        if(instance) {
            return (T) data;
        }
        throw new IllegalArgumentException("类型转换错误");
    }


/*
    private void initTip(Locale locale) {
        String content = null;
        String dataId = null;
        ConfigService configService = null;
        try {
            if (locale == null) {
                dataId = messageConfig.getBasename() + ".properties";
            } else {
                dataId = messageConfig.getBasename() + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties";
            }
            Properties properties = new Properties();
            properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
            properties.put(PropertyKeyConst.NAMESPACE, dNamespace);
            configService = NacosFactory.createConfigService(properties);

            content = configService.getConfig(dataId, group, 5000);
            if (StringUtils.isEmpty(content)) {
                log.warn("config is null! dataId: {}", dataId);
                return;
            }
            log.info("init config: {}, content: {}", dataId, content);
            saveAsFileWriter(dataId, content);
            setListener(configService, dataId, locale);
        } catch (Exception e) {
            log.error("init config error: {}", e);
        }
    }

    private void setListener(ConfigService configService, String dataId, Locale locale) throws com.alibaba.nacos.api.exception.NacosException {
        configService.addListener(dataId, group, new Listener() {
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("receive config properties : {}", configInfo);
                try {
                    initTip(locale);
                } catch (Exception e) {
                    log.error("init config properties error {}", e);
                }
            }

            @Override
            public Executor getExecutor() {
                return null;
            }
        });
    }

    private void saveAsFileWriter(String fileName, String content) {
        String path = System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder();
        try {
            fileName = path + File.separator + fileName;
            File file = new File(fileName);
            FileUtils.writeStringToFile(file, content);
            log.info("save config file to {}", fileName);
        } catch (IOException e) {
            log.error("init config properties error: {} , message: {}", fileName, e);
        }
    }
*/
}
