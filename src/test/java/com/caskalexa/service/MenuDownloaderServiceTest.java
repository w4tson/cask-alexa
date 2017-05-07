package com.caskalexa.service;

import com.caskalexa.RestTemplateConfig;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.itextpdf.kernel.pdf.canvas.parser.EventType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { MenuDownloaderService.class, RestTemplateConfig.class })
public class MenuDownloaderServiceTest {

    public static final String CASK_MENU_PDF = "src/test/resources/cask-menu.pdf";
    @Autowired
    MenuDownloaderService menuDownloaderService;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer server;

    static byte[] pdfData;

    @BeforeClass
    public static void beforeClass() throws IOException {
        pdfData = Files.readAllBytes(Paths.get(CASK_MENU_PDF));
    }

    @Before
    public void setup() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testDownloadPdf() throws Exception {
        HttpHeaders headers  = new HttpHeaders();
        headers.add("ETag", "123");
        server.expect(requestTo(MenuDownloaderService.MENU_PDF_URL))
                .andExpect(method(GET))
                .andRespond(withSuccess(pdfData, MediaType.APPLICATION_PDF).headers(headers));

        byte[] responseData = menuDownloaderService.getMenu();
        assertThat(responseData).isEqualTo(pdfData);
    }

    @Test
    public void testParse() throws Exception {
        menuDownloaderService.parsePdfData(pdfData);

    }



}