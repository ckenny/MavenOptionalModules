package com.study;

import com.study.controller.FeatureController;
import com.study.feature.Feature;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
		basePackageClasses = {Feature.class, FeatureController.class}
)
public class BootWebApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(BootWebApplication.class, args);
	}

}
