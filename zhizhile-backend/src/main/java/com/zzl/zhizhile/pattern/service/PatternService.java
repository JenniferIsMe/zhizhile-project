package com.zzl.zhizhile.pattern.service;

import com.zzl.zhizhile.common.constant.ErrorCode;
import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.file.mapper.FileResourceMapper;
import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import com.zzl.zhizhile.file.service.FileStorageService;
import com.zzl.zhizhile.pattern.mapper.ProjectPatternMapper;
import com.zzl.zhizhile.pattern.model.dto.CreatePatternLinkRequest;
import com.zzl.zhizhile.pattern.model.entity.ProjectPatternEntity;
import com.zzl.zhizhile.pattern.model.vo.PatternVO;
import com.zzl.zhizhile.project.model.entity.ProjectEntity;
import com.zzl.zhizhile.project.service.ProjectService;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图解服务，负责图解新增、上传、切换、删除等核心规则。
 */
@Service
public class PatternService {
    private final ProjectPatternMapper patternMapper;
    private final FileResourceMapper fileResourceMapper;
    private final FileStorageService fileStorageService;
    private final ProjectService projectService;

    public PatternService(ProjectPatternMapper patternMapper,
                          FileResourceMapper fileResourceMapper,
                          FileStorageService fileStorageService,
                          ProjectService projectService) {
        this.patternMapper = patternMapper;
        this.fileResourceMapper = fileResourceMapper;
        this.fileStorageService = fileStorageService;
        this.projectService = projectService;
    }

    /**
     * 新增链接图解。
     */
    public PatternVO createLink(Long projectId, CreatePatternLinkRequest request) {
        ProjectEntity project = projectService.getProjectEntity(projectId);
        if (request == null || request.getUrl() == null || request.getUrl().trim().isEmpty()) {
            throw new BizException(ErrorCode.INVALID_LINK, "链接不能为空");
        }
        if (!isHttpUrl(request.getUrl())) {
            throw new BizException(ErrorCode.INVALID_LINK);
        }

        ProjectPatternEntity entity = new ProjectPatternEntity();
        entity.setProjectId(project.getId());
        entity.setSourceType("LINK");
        entity.setExternalUrl(request.getUrl().trim());
        entity.setDisplayName(request.getDisplayName() == null || request.getDisplayName().isBlank()
                ? request.getUrl().trim() : request.getDisplayName().trim());
        patternMapper.insert(entity);
        return toVO(entity);
    }

    /**
     * 上传本地图解，并执行重复上传校验。
     */
    public PatternVO upload(Long projectId, MultipartFile file) {
        ProjectEntity project = projectService.getProjectEntity(projectId);
        if (file == null || file.isEmpty()) {
            throw new BizException(ErrorCode.FILE_INVALID, "上传文件不能为空");
        }

        List<ProjectPatternEntity> patterns = patternMapper.findByProjectId(projectId);
        String filename = file.getOriginalFilename();
        long size = file.getSize();
        for (ProjectPatternEntity pattern : patterns) {
            if (!"UPLOAD".equals(pattern.getSourceType()) || pattern.getFileId() == null) {
                continue;
            }
            FileResourceEntity existing = fileResourceMapper.findById(pattern.getFileId()).orElse(null);
            if (existing != null
                    && filename != null
                    && filename.equals(existing.getOriginalName())
                    && size == existing.getFileSize()) {
                throw new BizException(ErrorCode.DUPLICATE_UPLOAD);
            }
        }

        FileResourceEntity savedFile = fileStorageService.save(file);
        fileResourceMapper.insert(savedFile);

        ProjectPatternEntity pattern = new ProjectPatternEntity();
        pattern.setProjectId(project.getId());
        pattern.setSourceType("UPLOAD");
        pattern.setDisplayName(savedFile.getOriginalName());
        pattern.setFileId(savedFile.getId());
        patternMapper.insert(pattern);
        return toVO(pattern);
    }

    /**
     * 查询项目下图解列表。
     */
    public List<PatternVO> listByProject(Long projectId) {
        projectService.getProjectEntity(projectId);
        return patternMapper.findByProjectId(projectId).stream().map(this::toVO).collect(Collectors.toList());
    }

    /**
     * 手动切换当前图解。
     */
    public void switchCurrent(Long projectId, Long patternId) {
        ProjectEntity project = projectService.getProjectEntity(projectId);
        ProjectPatternEntity pattern = patternMapper.findById(patternId)
                .orElseThrow(() -> new BizException(ErrorCode.PATTERN_NOT_FOUND));
        if (!projectId.equals(pattern.getProjectId())) {
            throw new BizException(ErrorCode.PATTERN_SWITCH_FAILED);
        }
        project.setCurrentPatternId(patternId);
        projectService.updateProject(project);
    }

    /**
     * 删除图解；若删除的是当前图解，自动切换到最近添加图解。
     */
    public void deletePattern(Long projectId, Long patternId) {
        ProjectEntity project = projectService.getProjectEntity(projectId);
        ProjectPatternEntity target = patternMapper.findById(patternId)
                .orElseThrow(() -> new BizException(ErrorCode.PATTERN_NOT_FOUND));
        if (!projectId.equals(target.getProjectId())) {
            throw new BizException(ErrorCode.PATTERN_NOT_FOUND);
        }

        patternMapper.deleteById(patternId);
        if (patternId.equals(project.getCurrentPatternId())) {
            List<ProjectPatternEntity> remains = patternMapper.findByProjectId(projectId);
            Long fallback = remains.stream()
                    .max(Comparator.comparing(ProjectPatternEntity::getCreateTime))
                    .map(ProjectPatternEntity::getId)
                    .orElse(null);
            project.setCurrentPatternId(fallback);
            projectService.updateProject(project);
        }
    }

    private boolean isHttpUrl(String input) {
        try {
            URI uri = new URI(input);
            String scheme = uri.getScheme();
            return "http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme);
        } catch (Exception e) {
            return false;
        }
    }

    private PatternVO toVO(ProjectPatternEntity entity) {
        PatternVO vo = new PatternVO();
        vo.setId(entity.getId());
        vo.setProjectId(entity.getProjectId());
        vo.setSourceType(entity.getSourceType());
        vo.setDisplayName(entity.getDisplayName());
        vo.setFileId(entity.getFileId());
        vo.setExternalUrl(entity.getExternalUrl());
        return vo;
    }
}
