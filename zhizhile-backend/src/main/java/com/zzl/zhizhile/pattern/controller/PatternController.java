package com.zzl.zhizhile.pattern.controller;

import com.zzl.zhizhile.common.api.ApiResponse;
import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.pattern.model.vo.PatternVO;
import com.zzl.zhizhile.pattern.service.PatternService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图解管理接口。
 */
@RestController
@RequestMapping("/api/projects/{projectId}/patterns")
public class PatternController {
    private final PatternService patternService;

    public PatternController(PatternService patternService) {
        this.patternService = patternService;
    }

    /**
     * 创建链接图解接口。
     */
    @PostMapping("/link")
    public ApiResponse<PatternVO> createLink(@PathVariable Long projectId, @RequestBody CreatePatternLinkRequest request) {
        return ApiResponse.success(patternService.createLink(projectId, request));
    }

    /**
     * 上传图解接口。
     */
    @PostMapping("/upload")
    public ApiResponse<PatternVO> upload(@PathVariable Long projectId, @RequestParam("file") MultipartFile file) {
        return ApiResponse.success(patternService.upload(projectId, file));
    }

    /**
     * 图解列表查询接口。
     */
    @GetMapping
    public ApiResponse<List<PatternVO>> list(@PathVariable Long projectId) {
        return ApiResponse.success(patternService.listByProject(projectId));
    }

    /**
     * 设置当前图解接口。
     */
    @PutMapping("/{patternId}/current")
    public ApiResponse<Void> switchCurrent(@PathVariable Long projectId, @PathVariable Long patternId) {
        patternService.switchCurrent(projectId, patternId);
        return ApiResponse.success(null);
    }

    /**
     * 删除图解接口。
     */
    @DeleteMapping("/{patternId}")
    public ApiResponse<Void> delete(@PathVariable Long projectId, @PathVariable Long patternId) {
        patternService.deletePattern(projectId, patternId);
        return ApiResponse.success(null);
    }
}
