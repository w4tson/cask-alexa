package com.caskalexa;

import com.caskalexa.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MenuService menuService;

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        try {
            menuService.topBeersSpeech();
        } catch (IOException e) {
            log.info("Problem optimistically loading beers on startup", e);
        }
    }

}   