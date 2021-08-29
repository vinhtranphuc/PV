package com.so;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.so.security.AppProperties;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        PVApplication.class,
        Jsr310JpaConverters.class,
})
@EnableConfigurationProperties(AppProperties.class)
public class PVApplication {

    public static void main(String[] args) {
       SpringApplication.run(PVApplication.class, args);
    }

    // repository exception
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}