package com.caskalexa;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;
import com.caskalexa.service.CaskSpeechlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlexaConfig {

    @Autowired
    private CaskSpeechlet caskSpeechlet;

    @Bean
    public ServletRegistrationBean registerServlet() {

        SpeechletServlet speechletServlet = new SpeechletServlet();
        speechletServlet.setSpeechlet(caskSpeechlet);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(speechletServlet, "/alexa");
        return servletRegistrationBean;
    }
}