package me.specter.spring_boot_demo_02;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SpringBootDemo02Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemo02Application.class, args);
	}

    @Bean WebMvcConfigurer corsConfigurer(
        @Value("${application.allowed-origin}") String ALLOWED_ORIGIN
    ) {
		return new WebMvcConfigurer(){
			public void addCorsMappings(CorsRegistry registry){
				registry
				.addMapping("/**")
				.allowedMethods("*")
				.allowedOrigins(ALLOWED_ORIGIN)
				;
			}
		};
	}

}
