package com.zzl.zhizhile.file.mapper;

import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * 文件资源 DAO，提供文件元数据的内存化持久化能力。
 */
@Component
public class FileResourceMapper {
    private final AtomicLong idGen = new AtomicLong(1);
    private final Map<Long, FileResourceEntity> store = new ConcurrentHashMap<>();

    /**
     * 新增文件资源记录。
     */
    public FileResourceEntity insert(FileResourceEntity entity) {
        long id = idGen.getAndIncrement();
        entity.setId(id);
        LocalDateTime now = LocalDateTime.now();
        entity.setCreateTime(now);
        entity.setUpdateTime(now);
        store.put(id, entity);
        return entity;
    }

    /**
     * 按文件 ID 查询文件资源。
     */
    public Optional<FileResourceEntity> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
