package com.caskalexa.service;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.filter.TextRegionEventFilter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.lang.String.format;

@Slf4j
@Service
public class MenuDownloaderService {

    public static final String MENU_PDF_URL = "http://www.caskpubandkitchen.com/s/Todays-Beerlist.pdf";

    @Autowired
    RestTemplate restTemplate;

    public byte[] getMenu() {
        ResponseEntity<byte[]> response = restTemplate.getForEntity(MENU_PDF_URL, byte[].class);
        return response.getBody();
    }

    /**
     * @deprecated There is no actual text in a Cask Menu
     * @param pdfData
     * @throws IOException
     */
    @Deprecated
    public void parsePdfData(byte[] pdfData) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfData);

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(byteArrayInputStream));
        PdfPage page1 = pdfDoc.getPage(1);

        Rectangle rect = new Rectangle(0, 0, 1000, 1000);
        FontFilter fontFilter = new FontFilter(rect);
        FilteredEventListener listener = new FilteredEventListener();
        LocationTextExtractionStrategy extractionStrategy = listener.attachEventListener(new LocationTextExtractionStrategy(), fontFilter);
        LocationTextExtractionStrategy extractionStrategy2 = new LocationTextExtractionStrategy();
        new PdfCanvasProcessor(listener).processPageContent(page1);
        new PdfCanvasProcessor(extractionStrategy2).processPageContent(page1);

        System.out.println("Parsing");
        System.out.println(extractionStrategy2.getResultantText());


        String actualText = extractionStrategy.getResultantText();

        System.out.println(format("TEXT = %s", actualText));
        pdfDoc.close();

    }

    public static class FontFilter extends TextRegionEventFilter {
        public FontFilter(Rectangle filterRect) {
            super(filterRect);
        }

        @Override
        public boolean accept(IEventData data, EventType type) {
            if (type.equals(EventType.RENDER_TEXT)) {
                TextRenderInfo renderInfo = (TextRenderInfo) data;

                return true;
//                TextRenderInfo renderInfo = (TextRenderInfo) data;
//
//                PdfFont font = renderInfo.getFont();
//                if (null != font) {
//                    String fontName = font.getFontProgram().getFontNames().getFontName();
//                    return fontName.endsWith("Bold") || fontName.endsWith("Oblique");
//                }
            }
            return false;
        }
    }


}
