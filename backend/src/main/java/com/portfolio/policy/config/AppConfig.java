package com.portfolio.policy.config;
import org.springframework.context.annotation.*; import org.springframework.web.client.RestClient;
@Configuration public class AppConfig { @Bean RestClient restClient(){ return RestClient.create(); } }
