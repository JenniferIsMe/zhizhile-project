package com.zzl.zhizhile.progress.mapper;

import com.zzl.zhizhile.progress.model.entity.ProjectProgressEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 项目进度 DAO，按项目维度保存计数和计时结果。
 */
@Mapper
public interface ProjectProgressMapper {

    /**
     * 保存或覆盖项目进度。
     */
    @Insert("""
            INSERT INTO project_progress (project_id, current_row_index, total_count, total_seconds, update_time)
            VALUES (#{projectId}, #{currentRowIndex}, #{totalCount}, #{totalSeconds}, NOW())
            ON DUPLICATE KEY UPDATE
              id = LAST_INSERT_ID(id),
              current_row_index = VALUES(current_row_index),
              total_count = VALUES(total_count),
              total_seconds = VALUES(total_seconds),
              update_time = NOW()
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int upsert(ProjectProgressEntity entity);

    /**
     * 按项目 ID 查询项目进度。
     */
    @Select("""
            SELECT id, project_id, current_row_index, total_count, total_seconds, update_time
            FROM project_progress
            WHERE project_id = #{projectId}
            """)
    ProjectProgressEntity selectByProjectId(Long projectId);

    default Optional<ProjectProgressEntity> findByProjectId(Long projectId) {
        return Optional.ofNullable(selectByProjectId(projectId));
    }
}
