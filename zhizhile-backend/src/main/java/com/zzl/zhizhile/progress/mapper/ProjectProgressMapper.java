package com.zzl.zhizhile.progress.mapper;

import com.zzl.zhizhile.progress.model.entity.ProjectProgressEntity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 项目进度 DAO，按项目维度保存计数和计时结果。
 */
@Component
public class ProjectProgressMapper {
    private final AtomicLong idGen = new AtomicLong(1);
    private final Map<Long, ProjectProgressEntity> store = new ConcurrentHashMap<>();

    /**
     * 保存或覆盖项目进度。
     */
    public ProjectProgressEntity upsert(ProjectProgressEntity entity) {
        ProjectProgressEntity existing = store.get(entity.getProjectId());
        if (existing == null) {
            entity.setId(idGen.getAndIncrement());
        } else {
            entity.setId(existing.getId());
        }
        entity.setUpdateTime(LocalDateTime.now());
        store.put(entity.getProjectId(), entity);
        return entity;
    }

    /**
     * 按项目 ID 查询项目进度。
     */
    public Optional<ProjectProgressEntity> findByProjectId(Long projectId) {
        return Optional.ofNullable(store.get(projectId));
    }
}
