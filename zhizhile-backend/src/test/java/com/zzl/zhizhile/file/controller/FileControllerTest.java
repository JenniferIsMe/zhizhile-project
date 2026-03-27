package com.zzl.zhizhile.file.controller;

import com.zzl.zhizhile.file.mapper.FileResourceMapper;
import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FileControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FileResourceMapper fileResourceMapper;

    @Test
    void shouldAccessFileWhenExists() throws Exception {
        Path p = Files.createTempFile("zzl", ".pdf");
        Files.writeString(p, "dummy");

        FileResourceEntity file = new FileResourceEntity();
        file.setOriginalName("x.pdf");
        file.setContentType("application/pdf");
        file.setRelativePath(p.toString());
        file.setFileSize(Files.size(p));
        fileResourceMapper.insert(file);

        mockMvc.perform(get("/api/files/" + file.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnErrorWhenFileNotExists() throws Exception {
        mockMvc.perform(get("/api/files/999999"))
                .andExpect(status().isBadRequest());
    }
}
