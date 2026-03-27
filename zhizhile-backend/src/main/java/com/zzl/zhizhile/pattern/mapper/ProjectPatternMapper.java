package com.zzl.zhizhile.pattern.mapper;

import com.zzl.zhizhile.pattern.model.entity.ProjectPatternEntity;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 图解 DAO，提供项目图解持久化增删查能力。
 */
@Mapper
public interface ProjectPatternMapper {

    /**
     * 新增图解记录。
     */
    @Insert("""
            INSERT INTO project_patterns (project_id, source_type, display_name, file_id, external_url, create_time, update_time)
            VALUES (#{projectId}, #{sourceType}, #{displayName}, #{fileId}, #{externalUrl}, NOW(), NOW())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(ProjectPatternEntity entity);

    /**
     * 按图解 ID 查询图解。
     */
    @Select("""
            SELECT id, project_id, source_type, display_name, file_id, external_url, create_time, update_time
            FROM project_patterns
            WHERE id = #{id}
            """)
    ProjectPatternEntity selectById(Long id);

    default Optional<ProjectPatternEntity> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }

    /**
     * 查询项目下全部图解，按创建时间升序返回。
     */
    @Select("""
            SELECT id, project_id, source_type, display_name, file_id, external_url, create_time, update_time
            FROM project_patterns
            WHERE project_id = #{projectId}
            ORDER BY create_time ASC, id ASC
            """)
    List<ProjectPatternEntity> findByProjectId(Long projectId);

    /**
     * 按图解 ID 删除图解。
     */
    @Delete("DELETE FROM project_patterns WHERE id = #{id}")
    int deleteById(Long id);
}
