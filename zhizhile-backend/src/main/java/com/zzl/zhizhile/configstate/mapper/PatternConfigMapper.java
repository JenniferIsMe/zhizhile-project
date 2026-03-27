package com.zzl.zhizhile.configstate.mapper;

import com.zzl.zhizhile.configstate.model.entity.PatternConfigEntity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 阅读配置 DAO，按项目与图解维度存取阅读配置。
 */
@Component
public class PatternConfigMapper {
    private final AtomicLong idGen = new AtomicLong(1);
    private final Map<String, PatternConfigEntity> store = new ConcurrentHashMap<>();

    /**
     * 保存或覆盖阅读配置。
     */
    public PatternConfigEntity upsert(PatternConfigEntity entity) {
        String key = key(entity.getProjectId(), entity.getPatternId());
        PatternConfigEntity existing = store.get(key);
        if (existing == null) {
            entity.setId(idGen.getAndIncrement());
        } else {
            entity.setId(existing.getId());
        }
        entity.setUpdateTime(LocalDateTime.now());
        store.put(key, entity);
        return entity;
    }

    /**
     * 查询指定项目和图解的阅读配置。
     */
    public Optional<PatternConfigEntity> findByProjectIdAndPatternId(Long projectId, Long patternId) {
        return Optional.ofNullable(store.get(key(projectId, patternId)));
    }

    private String key(Long projectId, Long patternId) {
        return projectId + "_" + patternId;
    }
}
