package com.zzl.zhizhile.pattern.service;

import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class PatternServiceDeleteTest {

    @Autowired
    private PatternService patternService;
    @Autowired
    private ProjectService projectService;

    @Test
    void shouldDeleteNormalPatternSuccess() {
        Long p = createProject("delete-normal");
        Long a = createLink(p, "https://example.com/1");
        Long b = createLink(p, "https://example.com/2");

        patternService.deletePattern(p, a);

        assertEquals(1, patternService.listByProject(p).size());
        assertEquals(b, patternService.listByProject(p).get(0).getId());
    }

    @Test
    void shouldFallbackWhenDeleteCurrentPattern() {
        Long p = createProject("delete-current");
        Long a = createLink(p, "https://example.com/3");
        Long b = createLink(p, "https://example.com/4");
        patternService.switchCurrent(p, a);

        patternService.deletePattern(p, a);

        assertEquals(b, projectService.getDetail(p).getCurrentPatternId());
    }

    @Test
    void shouldSetCurrentNullWhenDeleteLastPattern() {
        Long p = createProject("delete-last");
        Long a = createLink(p, "https://example.com/5");
        patternService.switchCurrent(p, a);

        patternService.deletePattern(p, a);

        assertNull(projectService.getDetail(p).getCurrentPatternId());
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
