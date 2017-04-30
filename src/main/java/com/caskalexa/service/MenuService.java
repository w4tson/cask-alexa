package com.caskalexa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MenuService {

    public static final String MENU_PDF_URL = "http://www.caskpubandkitchen.com/s/Todays-Beerlist.pdf";

    @Autowired
    RestTemplate restTemplate;

    public byte[] getMenu() {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(MENU_PDF_URL, byte[].class);
        return response.getBody();
    }

}
