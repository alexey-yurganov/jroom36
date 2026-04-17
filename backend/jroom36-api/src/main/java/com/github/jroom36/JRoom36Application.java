/* (c) 2026 alexey-yurganov, MIT License */

package com.github.jroom36;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class JRoom36Application {
    static void main(String[] args) {
        SpringApplication.run(JRoom36Application.class, args);
    }
}
