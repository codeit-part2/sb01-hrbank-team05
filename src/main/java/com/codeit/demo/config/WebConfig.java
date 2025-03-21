package com.codeit.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    // 올바른 패턴으로 수정
    registry.addViewController("/{spring:[a-zA-Z0-9-]+}").setViewName("forward:/index.html");
    registry.addViewController("/employees").setViewName("forward:/index.html");
    registry.addViewController("/departments").setViewName("forward:/index.html");
  }
}
