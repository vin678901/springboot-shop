package com.example.springbootshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing//이게 없으면 BaseEntity를 사용할 수 없다.
public class AuditConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {return new AuditorAwareImpl();}
}
