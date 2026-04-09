/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(
        scanBasePackages = "com.github.jroom36",
        exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class
        }
)
@EnableConfigurationProperties
public class JRoom36Application {
    public static void main(String[] args) {
        SpringApplication.run(JRoom36Application.class, args);
    }
}
