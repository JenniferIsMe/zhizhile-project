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
class PatternServiceSwitchTest {

    @Autowired
    private PatternService patternService;
    @Autowired
    private ProjectService projectService;

    @Test
    void shouldSwitchCurrentPatternSuccess() {
        Long p = createProject("switch-ok");
        Long id = createLink(p, "https://example.com/a");

        patternService.switchCurrent(p, id);

        assertEquals(id, projectService.getDetail(p).getCurrentPatternId());
    }

    @Test
    void shouldFailWhenPatternNotBelongToProject() {
        Long p1 = createProject("switch-p1");
        Long p2 = createProject("switch-p2");
        Long id = createLink(p2, "https://example.com/b");

        BizException ex = assertThrows(BizException.class, () -> patternService.switchCurrent(p1, id));
        assertEquals("PATTERN_SWITCH_FAILED", ex.getErrorCode().getCode());
    }

    private Long createProject(String name) {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName(name);
        return projectService.createProject(req).getId();
    }

    private Long createLink(Long projectId, String url) {
        CreatePatternLinkRequest req = new CreatePatternLinkRequest();
        req.setUrl(url);
        return patternService.createLink(projectId, req).getId();
    }
}
