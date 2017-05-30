package com.caskalexa.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.stream.Stream;

@Data
@AllArgsConstructor
public class Beer {
    private String type;
    private String name;
    private String style;

    public static final String[] regulars = {"Pheasant Plucker", "Wheat Rothaus", "Pils Rothaus", "Tzara Thornbridge"};

    public boolean isNonRegular() {
        return !Stream.of(regulars)
                .anyMatch(r -> name.contains(r));
    }

}