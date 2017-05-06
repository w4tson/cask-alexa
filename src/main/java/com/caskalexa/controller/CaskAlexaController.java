package com.caskalexa.controller;

import com.caskalexa.service.MenuService;
import com.caskalexa.service.dto.Beer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path="/api", produces = "application/json")
public class CaskAlexaController {

    @Autowired
    MenuService menuService;

    @RequestMapping(value = "kegs")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<?> getAllBeers() throws IOException {
        List<Beer> allKegs = menuService.getAllKegs();
        return new ResponseEntity<>(allKegs, HttpStatus.OK);
    }
}
