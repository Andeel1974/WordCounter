package com.companyname.projectname.wordcounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = {"com.companyname.projectname.wordcounter"})
public class WordCounterApplication extends SpringBootServletInitializer {
	
	public static void main(String[] args) {
		SpringApplication.run(WordCounterApplication.class, args);
	}
}
