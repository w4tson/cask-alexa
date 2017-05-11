package com.caskalexa.service.ocr;

import com.google.cloud.vision.spi.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleVisionService {

    @Autowired
    RestTemplate restTemplate;

    public String getText(byte[] imageData) throws IOException {
        ImageAnnotatorClient vision = ImageAnnotatorClient.create();
        ByteString imgBytes = ByteString.copyFrom(imageData);

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);

        // Performs label detection on the image file
        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        return responses.stream()
                .findFirst()
                .map(AnnotateImageResponse::getFullTextAnnotation)
                .map(TextAnnotation::getText)
                .orElseThrow(() -> new RuntimeException("Problem calling google vision"));

    }
}
