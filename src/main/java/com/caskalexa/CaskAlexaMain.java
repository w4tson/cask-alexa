package com.caskalexa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.caskalexa" })
public class CaskAlexaMain {

    public static void main(String[] args) {
        SpringApplication.run(CaskAlexaMain.class, args);
    }
}