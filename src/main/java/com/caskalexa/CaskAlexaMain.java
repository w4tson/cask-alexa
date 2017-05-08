package com.caskalexa;

import com.caskalexa.GoogleCredentialsSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication(scanBasePackages = { "com.caskalexa" })
public class CaskAlexaMain {

    public static void main(String[] args) {
        GoogleCredentialsSupport.configure();
        SpringApplication.run(CaskAlexaMain.class, args);
    }

}