package com.caskalexa;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();


        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .build();

        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate.setRequestFactory(httpComponentsClientHttpRequestFactory);


        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        List<MediaType> supportedApplicationTypes = new ArrayList<>();
        MediaType pdfApplication = new MediaType("application","pdf");
        supportedApplicationTypes.add(pdfApplication);

        byteArrayHttpMessageConverter.setSupportedMediaTypes(supportedApplicationTypes);
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(byteArrayHttpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);

        return restTemplate;

    }
}
