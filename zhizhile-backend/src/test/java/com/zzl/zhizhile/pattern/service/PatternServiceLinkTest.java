package com.zzl.zhizhile.pattern.service;

import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.pattern.model.vo.PatternVO;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PatternServiceLinkTest {

    @Autowired
    private PatternService patternService;
    @Autowired
    private ProjectService projectService;

    @Test
    void shouldCreateLinkPatternSuccess() {
        Long projectId = createProject("link-ok");
        CreatePatternLinkRequest req = new CreatePatternLinkRequest();
        req.setUrl("https://example.com/a");

        PatternVO vo = patternService.createLink(projectId, req);

        assertNotNull(vo.getId());
        assertEquals("LINK", vo.getSourceType());
    }

    @Test
    void shouldFailWhenLinkInvalid() {
        Long projectId = createProject("link-invalid");
        CreatePatternLinkRequest req = new CreatePatternLinkRequest();
        req.setUrl("abc");

        BizException ex = assertThrows(BizException.class, () -> patternService.createLink(projectId, req));
        assertEquals("INVALID_LINK", ex.getErrorCode().getCode());
    }

    @Test
    void shouldFailWhenProjectNotFound() {
        CreatePatternLinkRequest req = new CreatePatternLinkRequest();
        req.setUrl("https://example.com/a");

        BizException ex = assertThrows(BizException.class, () -> patternService.createLink(999999L, req));
        assertEquals("PROJECT_NOT_FOUND", ex.getErrorCode().getCode());
    }

    private Long createProject(String name) {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName(name);
        return projectService.createProject(req).getId();
    }
}
