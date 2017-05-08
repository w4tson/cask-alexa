package com.caskalexa.service;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;

public class GoogleVisionServiceTest {

    public static final String CASK_MENU_PDF = "src/test/resources/cask-menu.pdf";

    static byte[] pdfData;

    PdfSupport pdfSupport = new PdfSupport();

    @BeforeClass
    public static void beforeClass() throws IOException {
        pdfData = Files.readAllBytes(Paths.get(CASK_MENU_PDF));
    }

    @Test
    @Ignore
    public void testGoogleVision() throws Exception {

        byte[] bytes = pdfSupport.cropKegs(pdfData);
        byte[] croppedImage = pdfSupport.convertPdfToImage(bytes);
        String text = new GoogleVisionService().getText(croppedImage);

        assertThat(text, startsWith("KEG"));
    }
}
