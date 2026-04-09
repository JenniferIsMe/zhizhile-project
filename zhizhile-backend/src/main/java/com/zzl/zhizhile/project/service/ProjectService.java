package com.zzl.zhizhile.project.service;

import com.zzl.zhizhile.common.constant.ErrorCode;
import com.zzl.zhizhile.common.exception.BizException;
import com.zzl.zhizhile.configstate.service.PatternConfigService;
import com.zzl.zhizhile.pattern.mapper.ProjectPatternMapper;
import com.zzl.zhizhile.pattern.model.entity.ProjectPatternEntity;
import com.zzl.zhizhile.pattern.model.vo.PatternVO;
import com.zzl.zhizhile.progress.service.ProjectProgressService;
import com.zzl.zhizhile.project.mapper.ProjectMapper;
import com.zzl.zhizhile.project.model.dto.CreateProjectRequest;
import com.zzl.zhizhile.project.model.entity.ProjectEntity;
import com.zzl.zhizhile.project.model.vo.ProjectDetailVO;
import com.zzl.zhizhile.project.model.vo.ProjectOverviewVO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 项目服务，负责项目主流程和项目总览聚合。
 */
@Service
public class ProjectService {
    private final ProjectMapper projectMapper;
    private final ProjectPatternMapper patternMapper;
    private final PatternConfigService patternConfigService;
    private final ProjectProgressService progressService;

    @Value("${zhizhile.user.test-user-id}")
    private Long testUserId;

    public ProjectService(ProjectMapper projectMapper,
                          ProjectPatternMapper patternMapper,
                          PatternConfigService patternConfigService,
                          ProjectProgressService progressService) {
        this.projectMapper = projectMapper;
        this.patternMapper = patternMapper;
        this.patternConfigService = patternConfigService;
        this.progressService = progressService;
    }

    /**
     * 创建项目。
     */
    public ProjectDetailVO createProject(CreateProjectRequest request) {
        if (request == null || request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BizException(ErrorCode.BAD_REQUEST, "项目名称必填");
        }
        ProjectEntity entity = new ProjectEntity();
        entity.setUserId(testUserId);
        entity.setName(request.getName().trim());
        entity.setType(request.getType());
        entity.setStatus("ACTIVE");
        projectMapper.insert(entity);
        return toDetail(entity);
    }

    /**
     * 查询项目详情。
     */
    public ProjectDetailVO getDetail(Long projectId) {
        ProjectEntity entity = getProjectEntity(projectId);
        return toDetail(entity);
    }

    /**
     * 查询项目总览，返回页面恢复所需聚合信息。
     */
    public ProjectOverviewVO getOverview(Long projectId) {
        ProjectEntity project = getProjectEntity(projectId);
        ProjectOverviewVO vo = new ProjectOverviewVO();
        vo.setProject(toDetail(project));

        List<PatternVO> patterns = patternMapper.findByProjectId(projectId).stream()
                .map(this::toPatternVO)
                .collect(Collectors.toList());
        vo.setPatterns(patterns);

        if (project.getCurrentPatternId() != null) {
            PatternVO current = patterns.stream()
                    .filter(p -> project.getCurrentPatternId().equals(p.getId()))
                    .findFirst()
                    .orElse(null);
            vo.setCurrentPattern(current);
            if (current != null) {
                vo.setPatternConfig(patternConfigService.get(projectId, current.getId()));
            }
        }

        vo.setProgress(progressService.get(projectId));
        return vo;
    }

    /**
     * 查询项目实体，不存在时抛业务异常。
     */
    public ProjectEntity getProjectEntity(Long projectId) {
        return projectMapper.findById(projectId)
                .orElseThrow(() -> new BizException(ErrorCode.PROJECT_NOT_FOUND));
    }

    /**
     * 更新项目实体。
     */
    public void updateProject(ProjectEntity entity) {
        projectMapper.update(entity);
    }

    private ProjectDetailVO toDetail(ProjectEntity entity) {
        ProjectDetailVO vo = new ProjectDetailVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setType(entity.getType());
        vo.setCurrentPatternId(entity.getCurrentPatternId());
        return vo;
    }

    private PatternVO toPatternVO(ProjectPatternEntity entity) {
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
