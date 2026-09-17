package com.vn.thu.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// URL /uploads/** <-> thu muc vat ly uploads/ trong project
		registry.addResourceHandler("/uploads/**")
				.addResourceLocations("file:uploads/");
	}
}
