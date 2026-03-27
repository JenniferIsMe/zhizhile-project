package com.zzl.zhizhile.project.service;

import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.model.vo.ProjectDetailVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectService projectService;

    @Test
    void shouldCreateProjectSuccess() {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName("project-a");

        ProjectDetailVO vo = projectService.createProject(req);

        assertNotNull(vo.getId());
        assertEquals("project-a", vo.getName());
    }

    @Test
    void shouldFailWhenProjectNameBlank() {
        CreateProjectRequest req = new CreateProjectRequest();
        req.setName(" ");

        BizException ex = assertThrows(BizException.class, () -> projectService.createProject(req));
        assertEquals("BAD_REQUEST", ex.getErrorCode().getCode());
    }

    @Test
    void shouldFailWhenProjectNotFound() {
        BizException ex = assertThrows(BizException.class, () -> projectService.getDetail(999999L));
        assertEquals("PROJECT_NOT_FOUND", ex.getErrorCode().getCode());
    }
}
