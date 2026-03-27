package com.zzl.zhizhile.project.service;

import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.pattern.service.PatternService;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.model.vo.ProjectDetailVO;
import com.zzl.zhizhile.project.model.vo.ProjectOverviewVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
class ProjectServiceValidationTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PatternService patternService;

    @Test
    void shouldReturnNullCurrentPatternWhenCurrentPatternNotExists() {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName("overview-boundary");
        ProjectDetailVO project = projectService.createProject(req);

        CreatePatternLinkRequest link = new CreatePatternLinkRequest();
        link.setUrl("https://example.com/1");
        Long patternId = patternService.createLink(project.getId(), link).getId();
        patternService.switchCurrent(project.getId(), patternId);
        patternService.deletePattern(project.getId(), patternId);

        ProjectOverviewVO overview = projectService.getOverview(project.getId());

        assertNull(overview.getCurrentPattern());
        assertEquals(0, overview.getPatterns().size());
    }
}
