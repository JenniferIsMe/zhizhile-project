package com.zzl.zhizhile.project.mapper;

import com.zzl.zhizhile.project.model.entity.ProjectEntity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 项目 DAO，提供项目数据的内存化读写能力。
 */
@Component
public class ProjectMapper {
    private final AtomicLong idGen = new AtomicLong(1);
    private final Map<Long, ProjectEntity> store = new ConcurrentHashMap<>();

    /**
     * 新增项目记录。
     */
    public ProjectEntity insert(ProjectEntity entity) {
        long id = idGen.getAndIncrement();
        entity.setId(id);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        store.put(id, entity);
        return entity;
    }

    /**
     * 按项目 ID 查询项目。
     */
    public Optional<ProjectEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /**
     * 更新项目记录。
     */
    public void update(ProjectEntity entity) {
        entity.setUpdateTime(LocalDateTime.now());
        store.put(entity.getId(), entity);
    }
}
