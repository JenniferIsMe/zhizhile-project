package com.zzl.zhizhile.configstate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzl.zhizhile.configstate.model.dto.SavePatternConfigRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PatternConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSaveAndGetConfig() throws Exception {
        SavePatternConfigRequest req = new SavePatternConfigRequest();
        req.setCurrentPage(6);
        req.setMaskTopOffset(11);
        req.setMaskHeight(99);

        mockMvc.perform(put("/api/projects/1/patterns/2/config")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        mockMvc.perform(get("/api/projects/1/patterns/2/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.currentPage").value(6));
    }
}
