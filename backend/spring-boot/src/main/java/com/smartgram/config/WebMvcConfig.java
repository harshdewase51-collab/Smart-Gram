package com.smartgram.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path base = Paths.get("").toAbsolutePath();
        if (!base.resolve("frontend").toFile().exists()) {
            base = Paths.get("D:\\SmartGram-main").toAbsolutePath();
        }
        String staticLocation = "file:" + base.resolve("frontend").resolve("static").toAbsolutePath() + "/";
        registry.addResourceHandler("/static/**")
                .addResourceLocations(staticLocation);
    }
}
