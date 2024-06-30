package com.cashme.tech_project.domain.config;

import com.cashme.tech_project.domain.Generated;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Generated
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/v1/**")
                .addOpenApiCustomiser(openApi -> openApi.info(new Info()
                        .title("CashMe")
                        .description("API created for a technical interview.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Augusto Ortolan")
                                .email("augustoortolan02@gmail.com")
                                .url("https://www.linkedin.com/in/augustoortolan/")
                        )
                ))
                .build();
    }
}
