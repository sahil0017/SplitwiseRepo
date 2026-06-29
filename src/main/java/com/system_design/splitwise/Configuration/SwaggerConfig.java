package com.system_design.splitwise.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        Contact contact = new Contact();
        contact.setName("SplitWise System");
        contact.setEmail("support@splitwise.local");

        Info info = new Info()
                .title("SplitWise API")
                .version("1.0")
                .description("System Design of SplitWise Backend APIs")
                .contact(contact);

        return new OpenAPI()
                .info(info);
    }
}
