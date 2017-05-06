package com.caskalexa.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Beer {
    private String type;
    private String name;
    private String style;
}
