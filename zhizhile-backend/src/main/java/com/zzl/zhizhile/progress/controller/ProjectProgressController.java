package com.zzl.zhizhile.progress.controller;

import com.zzl.zhizhile.common.api.ApiResponse;
import com.zzl.zhizhile.progress.model.dto.SaveRowProgressRequest;
import com.zzl.zhizhile.progress.model.dto.SaveTimeProgressRequest;
import com.zzl.zhizhile.progress.model.entity.ProjectProgressEntity;
import com.zzl.zhizhile.progress.service.ProjectProgressService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目进度接口。
 */
@RestController
@RequestMapping("/api/projects/{projectId}/progress")
public class ProjectProgressController {
    private final ProjectProgressService service;

    public ProjectProgressController(ProjectProgressService service) {
        this.service = service;
    }

    /**
     * 同步计数进度接口。
     */
    @PutMapping("/row")
    public ApiResponse<ProjectProgressEntity> saveRow(@PathVariable Long projectId, @RequestBody SaveRowProgressRequest request) {
        return ApiResponse.success(service.saveRowProgress(projectId, request));
    }

    /**
     * 同步计时进度接口。
     */
    @PutMapping("/time")
    public ApiResponse<ProjectProgressEntity> saveTime(@PathVariable Long projectId, @RequestBody SaveTimeProgressRequest request) {
        return ApiResponse.success(service.saveTimeProgress(projectId, request));
    }

    /**
     * 查询项目进度接口。
     */
    @GetMapping
    public ApiResponse<ProjectProgressEntity> get(@PathVariable Long projectId) {
        return ApiResponse.success(service.get(projectId));
    }
}
