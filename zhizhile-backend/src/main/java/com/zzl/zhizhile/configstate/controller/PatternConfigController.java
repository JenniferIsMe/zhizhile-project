package com.zzl.zhizhile.configstate.controller;

import com.zzl.zhizhile.common.api.ApiResponse;
import com.zzl.zhizhile.configstate.model.dto.SavePatternConfigRequest;
import com.zzl.zhizhile.configstate.model.entity.PatternConfigEntity;
import com.zzl.zhizhile.configstate.service.PatternConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 阅读配置接口。
 */
@RestController
@RequestMapping("/api/projects/{projectId}/patterns/{patternId}/config")
public class PatternConfigController {
    private final PatternConfigService service;

    public PatternConfigController(PatternConfigService service) {
        this.service = service;
    }

    /**
     * 查询阅读配置接口。
     */
    @GetMapping
    public ApiResponse<PatternConfigEntity> get(@PathVariable Long projectId, @PathVariable Long patternId) {
        return ApiResponse.success(service.get(projectId, patternId));
    }

    /**
     * 保存阅读配置接口。
     */
    @PutMapping
    public ApiResponse<PatternConfigEntity> save(@PathVariable Long projectId,
                                                 @PathVariable Long patternId,
                                                 @RequestBody SavePatternConfigRequest request) {
        return ApiResponse.success(service.save(projectId, patternId, request));
    }
}
