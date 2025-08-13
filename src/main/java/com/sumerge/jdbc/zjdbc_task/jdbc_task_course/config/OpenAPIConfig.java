package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${spring.application.name:Course API}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Course API")
                        .version("v1")
                        .description("Course Management API")
                        .contact(new Contact()
                                .name("Support Team")
                                .email("support@example.com")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")
                ));
    }
}
