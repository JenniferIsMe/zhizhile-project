package com.zzl.zhizhile.progress.service;

import com.zzl.zhizhile.progress.mapper.ProjectProgressMapper;
import com.zzl.zhizhile.progress.model.dto.SaveRowProgressRequest;
import com.zzl.zhizhile.progress.model.dto.SaveTimeProgressRequest;
import com.zzl.zhizhile.progress.model.entity.ProjectProgressEntity;
import org.springframework.stereotype.Service;

/**
 * 项目进度服务，负责计数和计时结果同步。
 */
@Service
public class ProjectProgressService {
    private final ProjectProgressMapper progressMapper;

    public ProjectProgressService(ProjectProgressMapper progressMapper) {
        this.progressMapper = progressMapper;
    }

    /**
     * 保存计数进度，按最新值覆盖。
     */
    public ProjectProgressEntity saveRowProgress(Long projectId, SaveRowProgressRequest request) {
        ProjectProgressEntity entity = progressMapper.findByProjectId(projectId).orElseGet(ProjectProgressEntity::new);
        entity.setProjectId(projectId);
        entity.setCurrentRowIndex(request.getCurrentRowIndex());
        entity.setTotalCount(request.getTotalCount());
        if (entity.getTotalSeconds() == null) {
            entity.setTotalSeconds(0L);
        }
        return progressMapper.upsert(entity);
    }

    /**
     * 保存计时进度，按最新值覆盖。
     */
    public ProjectProgressEntity saveTimeProgress(Long projectId, SaveTimeProgressRequest request) {
        ProjectProgressEntity entity = progressMapper.findByProjectId(projectId).orElseGet(ProjectProgressEntity::new);
        entity.setProjectId(projectId);
        if (entity.getCurrentRowIndex() == null) {
            entity.setCurrentRowIndex(0);
        }
        if (entity.getTotalCount() == null) {
            entity.setTotalCount(0);
        }
        entity.setTotalSeconds(request.getTotalSeconds());
        return progressMapper.upsert(entity);
    }

    /**
     * 查询项目当前进度，不存在时返回默认值。
     */
    public ProjectProgressEntity get(Long projectId) {
        return progressMapper.findByProjectId(projectId).orElseGet(() -> {
            ProjectProgressEntity def = new ProjectProgressEntity();
            def.setProjectId(projectId);
            def.setCurrentRowIndex(0);
            def.setTotalCount(0);
            def.setTotalSeconds(0L);
            return def;
        });
    }
}
