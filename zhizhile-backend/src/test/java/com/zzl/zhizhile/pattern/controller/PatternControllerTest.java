package com.zzl.zhizhile.pattern.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PatternControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldHandlePatternApis() throws Exception {
        Long projectId = createProject();

        CreatePatternLinkRequest req = new CreatePatternLinkRequest();
        req.setUrl("https://example.com/p");
        String linkResp = mockMvc.perform(post("/api/projects/" + projectId + "/patterns/link")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        long linkId = objectMapper.readTree(linkResp).path("data").path("id").asLong();

        MockMultipartFile file = new MockMultipartFile("file", "b.pdf", "application/pdf", "abc".getBytes());
        mockMvc.perform(multipart("/api/projects/" + projectId + "/patterns/upload").file(file))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/projects/" + projectId + "/patterns"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2));

        mockMvc.perform(put("/api/projects/" + projectId + "/patterns/" + linkId + "/current"))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/projects/" + projectId + "/patterns/" + linkId))
                .andExpect(status().isOk());
    }

    private Long createProject() throws Exception {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName("pattern-controller");
        String body = mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(body).path("data").path("id").asLong();
    }
}
