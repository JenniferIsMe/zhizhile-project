package com.zzl.zhizhile.project.mapper;

import com.zzl.zhizhile.project.model.entity.ProjectEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 项目 DAO，提供项目数据的持久化读写能力。
 */
@Mapper
public interface ProjectMapper {

    /**
     * 新增项目记录。
     */
    @Insert("""
            INSERT INTO projects (user_id, name, type, status, current_pattern_id, create_time, update_time)
            VALUES (#{userId}, #{name}, #{type}, #{status}, #{currentPatternId}, NOW(), NOW())
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insert(ProjectEntity entity);

    /**
     * 按项目 ID 查询项目。
     */
    @Select("""
            SELECT id, user_id, name, type, status, current_pattern_id, create_time, update_time
            FROM projects
            WHERE id = #{id}
            """)
    ProjectEntity selectById(Long id);

    default Optional<ProjectEntity> findById(Long id) {
        return Optional.ofNullable(selectById(id));
    }

    /**
     * 更新项目记录。
     */
    @Update("""
            UPDATE projects
            SET user_id = #{userId},
                name = #{name},
                type = #{type},
                status = #{status},
                current_pattern_id = #{currentPatternId},
                update_time = NOW()
            WHERE id = #{id}
            """)
    int update(ProjectEntity entity);
}
