package com.caskalexa.service;

import com.caskalexa.RestTemplateConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MenuService.class, RestTemplateConfig.class })
public class MenuServiceTest {

    @Autowired
    MenuService menuService;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer server;

    @Before
    public void setup() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testDownloadPdf() throws Exception {

        byte[] data = Files.readAllBytes(Paths.get("src/test/resources/cask-menu.pdf"));

        server.expect(requestTo(MenuService.MENU_PDF_URL))
                .andExpect(method(GET))
                .andRespond(withSuccess(data, MediaType.APPLICATION_PDF));

        byte[] responseData = menuService.getMenu();
        assertThat(responseData).isEqualTo(data);
    }
}