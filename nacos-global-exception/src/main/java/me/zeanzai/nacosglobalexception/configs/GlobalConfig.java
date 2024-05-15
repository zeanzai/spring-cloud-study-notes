//package me.zeanzai.nacosglobalexception.configs;
//
//import lombok.extern.slf4j.Slf4j;
//import me.zeanzai.nacosglobalexception.resolver.MessageResolver;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.context.annotation.Primary;
//import org.springframework.context.support.ReloadableResourceBundleMessageSource;
//import org.springframework.util.ResourceUtils;
//import org.springframework.web.servlet.LocaleResolver;
//
//import java.io.File;
//
///**
// * @author shawnwang
// * @version 1.0
// * @describe
// * @date 2023/4/7
// */
//@Slf4j
////@Configuration
//public class GlobalConfig {
//
//    @Autowired
//    private MessageConfig messageConfig;
//
//    @Bean
//    public LocaleResolver localeResolver(){
//        return new MessageResolver();
//    }
//
//    @Primary
//    @Bean(name = "messageSource")
//    @DependsOn(value = "messageConfig")
//    public ReloadableResourceBundleMessageSource messageSource() {
//        String path = ResourceUtils.FILE_URL_PREFIX + System.getProperty("user.dir") + File.separator + messageConfig.getBaseFolder() + File.separator + messageConfig.getBasename();
//        log.info("message content: {}", messageConfig);
//        log.info("message path: {}", path);
//        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
//        messageSource.setBasename(path);
//        messageSource.setDefaultEncoding(messageConfig.getEncoding());
//        messageSource.setCacheMillis(messageConfig.getCacheMillis());
//        return messageSource;
//    }
//
//}
