package com.caskalexa.service;

import com.caskalexa.service.dto.Beer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;


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

    public String topBeersSpeech() throws IOException {

        List<String> describedBeers = getAllKegs()
                .parallelStream()
                .limit(5)
                .map(this::describeBeer)
                .collect(Collectors.toList());

        return describedBeers.stream()
                .skip(1)
                .collect(Collectors.joining()) + " And finally, there is " + describedBeers.get(0);
    }

    private String describeBeer(Beer beer) {
        return format("%s, which is a %s. ", beer.getName(), beer.getStyle());
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
