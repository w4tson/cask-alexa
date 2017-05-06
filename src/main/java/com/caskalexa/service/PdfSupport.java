package com.caskalexa.service;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@SuppressWarnings("Duplicates")
@Service
public class PdfSupport {

    public static byte[] convertPdfToImage(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(byteArrayInputStream));

        parser.parse();
        COSDocument cosDoc = parser.getDocument();
        PDDocument pdDoc = new PDDocument(cosDoc);

        PDFRenderer pdfRenderer = new PDFRenderer(pdDoc);

        // note that the page number parameter is zero based
        BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);

        // suffix in filename will be used as the file format
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIOUtil.writeImage(bim, "png", outputStream, 300);

        pdDoc.close();
        byte[] bytes1 = outputStream.toByteArray();
        System.out.println("b len = "+bytes1.length);
        return bytes1;
    }

    public static byte[] cropKegs(byte[] pdfBytes) throws IOException {
        return cropPdf(pdfBytes, 0, 200, 228, 500);
    }

    /**
     * Crops a pdf using a bounding box
     * @param pdfBytes a byte array representation of PDF
     * @param x x coord of bottom left of crop box
     * @param y y coord of bottom left of crop box
     * @param width width of the crop box
     * @param height height of the crop box
     * @return a new pdf
     * @throws IOException
     */
    private static byte[] cropPdf(byte[] pdfBytes, float x, float y, float width, float height) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);

        PdfReader reader = new PdfReader(byteArrayInputStream);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(os);
        PdfDocument caskMenu = new PdfDocument(reader, writer);
        Rectangle rect = new Rectangle(x, y, width, height);

        PdfPage firstPage = caskMenu.getFirstPage();

        firstPage.setCropBox(rect);

        caskMenu.close();

        byte[] croppedPdf = os.toByteArray();

        return croppedPdf;
    }

}
