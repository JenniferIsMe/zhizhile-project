package com.zzl.zhizhile.project.service;

import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.pattern.service.PatternService;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.model.vo.ProjectDetailVO;
import com.zzl.zhizhile.project.model.vo.ProjectOverviewVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProjectOverviewServiceTest {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private PatternService patternService;

    @Test
    void shouldGetOverviewSuccess() {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName("overview-project");
        ProjectDetailVO project = projectService.createProject(req);

        CreatePatternLinkRequest link = new CreatePatternLinkRequest();
        link.setUrl("https://example.com/pattern");
        patternService.createLink(project.getId(), link);

        ProjectOverviewVO overview = projectService.getOverview(project.getId());

        assertNotNull(overview.getProject());
        assertEquals(project.getId(), overview.getProject().getId());
        assertEquals(1, overview.getPatterns().size());
        assertNotNull(overview.getProgress());
    }

    @Test
    void shouldFailWhenOverviewProjectNotFound() {
        BizException ex = assertThrows(BizException.class, () -> projectService.getOverview(888888L));
        assertEquals("PROJECT_NOT_FOUND", ex.getErrorCode().getCode());
    }
}
