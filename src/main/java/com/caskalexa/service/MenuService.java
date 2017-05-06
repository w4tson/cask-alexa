package com.caskalexa.service;

import com.caskalexa.service.dto.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MenuService {

    @Autowired
    MenuDownloaderService menuDownloaderService;

    @Autowired
    GoogleVisionService googleVisionService;

    public List<Beer> getAllKegs() throws IOException {
        byte[] menuPdfBytes = menuDownloaderService.getMenu();
        byte[] kegsPdfBytes = PdfSupport.cropKegs(menuPdfBytes);
        byte[] imageOfKegs = PdfSupport.convertPdfToImage(kegsPdfBytes);
        String extractedText = googleVisionService.getText(imageOfKegs);
        return convertExtractedKegText(extractedText);
    }

    private List<Beer> convertExtractedKegText(String text) {
        String beers = text.split("KEG\n")[1];

        String[] lines = beers.split("\n");
        int numberOfKegs = (lines.length)/ 2;

        return IntStream.range(0, numberOfKegs)
                .mapToObj(i -> {
                    return  new Beer("Keg", lines[i * 2], lines[i * 2 + 1]);
                })
                .collect(Collectors.toList());
    }


}
