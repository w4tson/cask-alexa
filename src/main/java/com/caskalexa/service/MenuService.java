package com.caskalexa.service;

import com.caskalexa.service.dto.Beer;
import com.caskalexa.service.ocr.GoogleVisionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@Service
public class MenuService {

    @Autowired
    MenuDownloaderService menuDownloaderService;

    @Autowired
    GoogleVisionService googleVisionService;

    List<Beer> cachedBeers;


    public List<Beer> getAllKegs() throws IOException {
        List<Beer> beers;
        if (isNotEmpty(cachedBeers) && menuDownloaderService.isMenuCurrent()) {
            log.debug(format("Menu unchanged, using %s beers from the cache", cachedBeers.size()));
            beers = cachedBeers;
        } else {
            byte[] menuPdfBytes = menuDownloaderService.getMenu();
            byte[] kegsPdfBytes = PdfSupport.cropKegs(menuPdfBytes);
            byte[] imageOfKegs = PdfSupport.convertPdfToImage(kegsPdfBytes);
            String extractedText = googleVisionService.getText(imageOfKegs);
            beers = convertExtractedKegText(extractedText);
            cachedBeers = beers;
        }

        return beers;
    }

    public String topBeersSpeech() throws IOException {

        List<String> describedBeers = getAllKegs()
                .parallelStream()
                .filter(Beer::isNonRegular)
                .limit(5)
                .map(this::describeBeer)
                .collect(Collectors.toList());

        return introduction() + describedBeers.stream()
                .skip(1)
                .collect(Collectors.joining()) + closingRemark() + describedBeers.get(0);
    }

    private String introduction() {
        int type = new Random().nextInt(4);
        String result = StringUtils.EMPTY;
        switch (type) {
            case 0:
                result = "At the Cask today we have: ";
                break;
            case 1:
                result = "Today at cask: ";
                break;
            case 2:
                result = "On the menu today there is: ";
                break;
            case 3:
                result = "Today on the menu:  ";
                break;
        }
        return result;
    }

    /**
     *
     * Describe a beer randomly one of several different ways:
     *
     * Edison Electric Bear which is a Czech Style lager
     * Edison Electric Bear, a Czech Style lager
     * A Czech Style lager, Edison Electric Bear
     * A Czech Style lager, by the name of Edison Electric Bear
     *
     * @param beer
     * @return
     */
    private String describeBeer(Beer beer) {
        int type = new Random().nextInt(4);
        String result = StringUtils.EMPTY;
        switch (type) {
            case 0:
                result = format("%s, which is a %s. ", beer.getName(), beer.getStyle());
                break;
            case 1:
                result = format("%s, a %s. ", beer.getName(), beer.getStyle());
                break;
            case 2:
                result = format("A %s, %s. ", beer.getStyle(), beer.getName());
                break;
            case 3:
                result = format("A %s, by the name of %s. ", beer.getStyle(), beer.getName());
                break;
        }
        return result;
    }

    private String closingRemark() {
        int type = new Random().nextInt(3);
        String result = StringUtils.EMPTY;
        switch (type) {
            case 0:
                result = " And finally, there is ";
                break;
            case 1:
                result = " As well as ";
                break;
            case 2:
                result = " Lastly there is ";
                break;
        }
        return result;
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
