package com.caskalexa.service;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.String.format;

@SuppressWarnings("Duplicates")
public class PdfSupportTest {

    public static final String CASK_MENU_PDF = "src/test/resources/cask-menu.pdf";
    public static final String OUTPUT_PDF = "build/output.pdf";

    static byte[] pdfData;

    PdfSupport pdfSupport = new PdfSupport();

    @BeforeClass
    public static void beforeClass() throws IOException {
        pdfData = Files.readAllBytes(Paths.get(CASK_MENU_PDF));
    }

    @Test
    public void testParseTextUsingPdfBox() throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfData);
        PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(byteArrayInputStream));

        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDoc = new PDDocument(cosDoc);
        String text = pdfTextStripper.getText(pdDoc);
        System.out.println("Text = " + text.length());
    }

    @Test
    public void name() throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfData);
        PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(byteArrayInputStream));
//        parser.getPDDocument().getPage(1).

    }

    @Test
    public void testCrop() throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfData);

        PdfReader reader = new PdfReader(byteArrayInputStream);
        PdfWriter writer = new PdfWriter(OUTPUT_PDF);
        PdfDocument caskMenu = new PdfDocument(reader, writer);
        Rectangle rect = new Rectangle(0, 200, 304, 500);

        PdfPage firstPage = caskMenu.getFirstPage();

        firstPage.setCropBox(rect);

        caskMenu.close();
    }

    @Test
    public void testCrop2() throws Exception {

        byte[] bytes = pdfSupport.cropKegs(pdfData);
        byte[] croppedImage = pdfSupport.convertPdfToImage(bytes);

        System.out.println(format("bytes = %s", bytes.length));
        System.out.println(format("croppedImage = %s", croppedImage.length));

        FileOutputStream fileOutputStream = new FileOutputStream("build/myfile.png");
        fileOutputStream.write(croppedImage);
        fileOutputStream.flush();
        fileOutputStream.close();


    }

}
