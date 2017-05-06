package com.caskalexa.controller;

import com.caskalexa.RestTemplateConfig;
import com.caskalexa.service.MenuService;
import com.caskalexa.service.dto.Beer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@EnableWebMvc
@ContextConfiguration(classes = { CaskAlexaController.class, RestTemplateConfig.class })
public class CaskAlexaControllerTest {

    @MockBean
    private MenuService menuService;

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
    }

    @Test
    public void getAllBeers() throws Exception {
        Beer beer = new Beer("Keg", "Rothaus", "Pilsner");
        Beer cider = new Beer("Keg", "Pheasant Plucker Lilley's", "Cider");
        when(menuService.getAllKegs()).thenReturn(asList(beer, cider));

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/kegs")
                .accept(MediaTypes.HAL_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("Keg")))
                .andExpect(jsonPath("$[0].name", is("Rothaus")))
                .andExpect(jsonPath("$[0].style", is("Pilsner")))
                .andExpect(jsonPath("$[1].type", is("Keg")))
                .andExpect(jsonPath("$[1].name", is("Pheasant Plucker Lilley's")))
                .andExpect(jsonPath("$[1].style", is("Cider")));

    }

}