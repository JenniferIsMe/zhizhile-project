package com.zzl.zhizhile.pattern.service;

import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.pattern.model.vo.PatternVO;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PatternServiceUploadTest {

    @Autowired
    private PatternService patternService;
    @Autowired
    private ProjectService projectService;

    @Test
    void shouldUploadSuccess() {
        Long projectId = createProject("upload-ok");
        MockMultipartFile file = new MockMultipartFile("file", "a.pdf", "application/pdf", "123".getBytes());

        PatternVO vo = patternService.upload(projectId, file);

        assertNotNull(vo.getId());
        assertEquals("UPLOAD", vo.getSourceType());
    }

    @Test
    void shouldFailWhenDuplicateUpload() {
        Long projectId = createProject("upload-dup");
        MockMultipartFile file1 = new MockMultipartFile("file", "same.pdf", "application/pdf", "1234".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "same.pdf", "application/pdf", "9999".getBytes());

        patternService.upload(projectId, file1);
        BizException ex = assertThrows(BizException.class, () -> patternService.upload(projectId, file2));
        assertEquals("DUPLICATE_UPLOAD", ex.getErrorCode().getCode());
    }

    @Test
    void shouldFailWhenFileTypeNotSupported() {
        Long projectId = createProject("upload-invalid-type");
        MockMultipartFile file = new MockMultipartFile("file", "a.txt", "text/plain", "123".getBytes());

        BizException ex = assertThrows(BizException.class, () -> patternService.upload(projectId, file));
        assertEquals("FILE_INVALID", ex.getErrorCode().getCode());
    }

    private Long createProject(String name) {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName(name);
        return projectService.createProject(req).getId();
    }
}
