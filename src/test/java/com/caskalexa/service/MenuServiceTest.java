package com.caskalexa.service;

import com.caskalexa.service.dto.Beer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceTest {

    public static final String CASK_MENU_PDF = "src/test/resources/cask-menu.pdf";

    @InjectMocks
    MenuService menuService = new MenuService();

    @Mock
    GoogleVisionService googleVisionService;

    @Mock
    MenuDownloaderService menuDownloaderService;

    static byte[] pdfData;

    @BeforeClass
    public static void beforeClass() throws IOException {
        pdfData = Files.readAllBytes(Paths.get(CASK_MENU_PDF));
    }

    @Before
    public void reset() {
        Mockito.reset(googleVisionService, menuDownloaderService);
    }

    @Test
    public void testKegsExtracted() throws Exception {
        String extractedText = Files.readAllLines(Paths.get("src/test/resources/extract-text.txt"))
                .stream()
                .collect(Collectors.joining("\n"));

        when(menuDownloaderService.getMenu()).thenReturn(pdfData);
        when(googleVisionService.getText(any())).thenReturn(extractedText);

        List<Beer> allKegs = menuService.getAllKegs();
        assertThat(allKegs).hasSize(15);
    }
}