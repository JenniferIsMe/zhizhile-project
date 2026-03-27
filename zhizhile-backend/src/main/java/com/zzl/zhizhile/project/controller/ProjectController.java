package com.zzl.zhizhile.project.controller;

import com.zzl.zhizhile.common.api.ApiResponse;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.model.vo.ProjectDetailVO;
import com.zzl.zhizhile.project.model.vo.ProjectOverviewVO;
import com.zzl.zhizhile.project.service.ProjectService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目接口。
 */
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    /**
     * 创建项目接口。
     */
    @PostMapping
    public ApiResponse<ProjectDetailVO> create(@RequestBody CreateProjectRequest request) {
        return ApiResponse.success(projectService.createProject(request));
    }

    /**
     * 查询项目详情接口。
     */
    @GetMapping("/{projectId}")
    public ApiResponse<ProjectDetailVO> detail(@PathVariable Long projectId) {
        return ApiResponse.success(projectService.getDetail(projectId));
    }

    /**
     * 查询项目总览接口。
     */
    @GetMapping("/{projectId}/overview")
    public ApiResponse<ProjectOverviewVO> overview(@PathVariable Long projectId) {
        return ApiResponse.success(projectService.getOverview(projectId));
    }
}
