package com.zzl.zhizhile.file.mapper;

import com.zzl.zhizhile.file.model.entity.FileResourceEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 文件资源 DAO，提供文件元数据持久化能力。
 */
@Mapper
public interface FileResourceMapper {

    /**
     * 新增文件资源记录。
     */
    @Insert("""
            INSERT INTO file_resources (original_name, stored_name, file_ext, content_type, file_size, storage_type, relative_path, create_time, update_time)
            VALUES (#{originalName}, #{storedName}, #{fileExt}, #{contentType}, #{fileSize}, #{storageType}, #{relativePath}, NOW(), NOW())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(FileResourceEntity entity);

    /**
     * 按文件 ID 查询文件资源。
     */
    @Select("""
            SELECT id, original_name, stored_name, file_ext, content_type, file_size, storage_type, relative_path, create_time, update_time
            FROM file_resources
            WHERE id = #{id}
            """)
    FileResourceEntity selectById(Long id);

    default Optional<FileResourceEntity> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }
}
