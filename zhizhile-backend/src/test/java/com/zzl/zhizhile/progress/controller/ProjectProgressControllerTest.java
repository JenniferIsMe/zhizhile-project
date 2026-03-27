package com.zzl.zhizhile.progress.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzl.zhizhile.progress.model.dto.SaveRowProgressRequest;
import com.zzl.zhizhile.progress.model.dto.SaveTimeProgressRequest;
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
class ProjectProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSyncRowTimeAndGet() throws Exception {
        SaveRowProgressRequest row = new SaveRowProgressRequest();
        row.setCurrentRowIndex(7);
        row.setTotalCount(70);
        SaveTimeProgressRequest time = new SaveTimeProgressRequest();
        time.setTotalSeconds(700L);

        mockMvc.perform(put("/api/projects/1/progress/row")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(row)))
                .andExpect(status().isOk());

        mockMvc.perform(put("/api/projects/1/progress/time")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(time)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/1/progress"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.currentRowIndex").value(7))
                .andExpect(jsonPath("$.data.totalSeconds").value(700));
    }
}
