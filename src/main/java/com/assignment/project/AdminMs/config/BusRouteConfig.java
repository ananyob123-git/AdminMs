package com.assignment.project.AdminMs.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BusRouteConfig {

    //@LoadBalanced
    @Bean
    public WebClient.Builder webClientBuilder(){
        return WebClient.builder();
    }
}
