package zeanzai.me.member.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@EnableSwagger2
@Configuration
@EnableSwaggerBootstrapUI
// @Profile({ "dev", "test" }) // 表示只在这两个环境下使用 ， 注意：任何 @Componen t 或 @Configuration 注解的类都可以使用 @Profile 注解。
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder parameterBuilder = new ParameterBuilder();
        List<Parameter> parameters = new ArrayList<>();
        parameterBuilder.modelRef(new ModelRef("String")).name("token").parameterType("header").description("token").defaultValue("").
                //考虑到有些请求是不需要token的，此处不能必填
                        required(false).build();
        parameters.add(parameterBuilder.build());

        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build().globalOperationParameters(parameters).securitySchemes(getSecuritySchemes());
    }

    /**
     * 修改默认展示效果
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Member模块")
                .contact(new Contact("zeanzai", "https://zeanzai.me", "zeanzai.me@gmail.com"))
                .version("1.0")
                .description("member会员模块的所有接口内容").build();
    }


    private List<ApiKey> getSecuritySchemes() {
        List<ApiKey> keys = new ArrayList<>();
        ApiKey key = new ApiKey("token", "token", "header");
        keys.add(key);
        return keys;
    }
}
