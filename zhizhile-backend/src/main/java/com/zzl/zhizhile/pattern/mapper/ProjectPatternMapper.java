package com.zzl.zhizhile.pattern.mapper;

import com.zzl.zhizhile.pattern.model.entity.ProjectPatternEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 图解 DAO，提供项目图解的内存化增删查能力。
 */
@Component
public class ProjectPatternMapper {
    private final AtomicLong idGen = new AtomicLong(1);
    private final Map<Long, ProjectPatternEntity> byId = new ConcurrentHashMap<>();

    /**
     * 新增图解记录。
     */
    public ProjectPatternEntity insert(ProjectPatternEntity entity) {
        long id = idGen.getAndIncrement();
        entity.setId(id);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        byId.put(id, entity);
        return entity;
    }

    /**
     * 按图解 ID 查询图解。
     */
    public Optional<ProjectPatternEntity> findById(Long id) {
        return Optional.ofNullable(byId.get(id));
    }

    /**
     * 查询项目下全部图解，按创建时间升序返回。
     */
    public List<ProjectPatternEntity> findByProjectId(Long projectId) {
        List<ProjectPatternEntity> list = new ArrayList<>();
        for (ProjectPatternEntity entity : byId.values()) {
            if (projectId.equals(entity.getProjectId())) {
                list.add(entity);
            }
        }
        list.sort(Comparator.comparing(ProjectPatternEntity::getCreateTime));
        return list;
    }

    /**
     * 按图解 ID 删除图解。
     */
    public void deleteById(Long id) {
        byId.remove(id);
    }
}
