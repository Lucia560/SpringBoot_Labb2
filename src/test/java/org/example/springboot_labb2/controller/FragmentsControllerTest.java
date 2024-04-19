package org.example.springboot_labb2.controller;

import org.example.springboot_labb2.controller.web.FragmentsController;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class FragmentsControllerTest {

    @Test
    void testMenuEndpoint() throws Exception {
        FragmentsController controller = new FragmentsController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/menu"))
                .andExpect(status().isOk())
                .andExpect(view().name("menu.html"));
    }

    @Test
    void testHeadEndpoint() throws Exception {
        FragmentsController controller = new FragmentsController();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        mockMvc.perform(get("/head"))
                .andExpect(status().isOk())
                .andExpect(view().name("head.html"));
    }
}