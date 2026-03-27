package com.zzl.zhizhile.pattern.service;

import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PatternServiceValidationTest {

    @Autowired
    private PatternService patternService;
    @Autowired
    private ProjectService projectService;

    @Test
    void shouldFailWhenLinkBlank() {
        Long projectId = createProject("val-link-blank");
        CreatePatternLinkRequest req = new CreatePatternLinkRequest();
        req.setUrl(" ");

        BizException ex = assertThrows(BizException.class, () -> patternService.createLink(projectId, req));
        assertEquals("INVALID_LINK", ex.getErrorCode().getCode());
    }

    @Test
    void shouldFailWhenUploadFileNull() {
        Long projectId = createProject("val-file-null");

        BizException ex = assertThrows(BizException.class, () -> patternService.upload(projectId, null));
        assertEquals("FILE_INVALID", ex.getErrorCode().getCode());
    }

    @Test
    void shouldFailWhenDeleteNotExistPattern() {
        Long projectId = createProject("val-delete");

        BizException ex = assertThrows(BizException.class, () -> patternService.deletePattern(projectId, 777777L));
        assertEquals("PATTERN_NOT_FOUND", ex.getErrorCode().getCode());
    }

    private Long createProject(String name) {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName(name);
        return projectService.createProject(req).getId();
    }
}
