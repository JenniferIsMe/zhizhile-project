package com.zzl.zhizhile.configstate.mapper;

import com.zzl.zhizhile.configstate.model.entity.PatternConfigEntity;
import java.util.Optional;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

/**
 * 阅读配置 DAO，按项目与图解维度存取阅读配置。
 */
@Mapper
public interface PatternConfigMapper {

    /**
     * 保存或覆盖阅读配置。
     */
    @Insert("""
            INSERT INTO pattern_configs (project_id, pattern_id, current_page, mask_top_offset, mask_height, update_time)
            VALUES (#{projectId}, #{patternId}, #{currentPage}, #{maskTopOffset}, #{maskHeight}, NOW())
            ON DUPLICATE KEY UPDATE
              id = LAST_INSERT_ID(id),
              current_page = VALUES(current_page),
              mask_top_offset = VALUES(mask_top_offset),
              mask_height = VALUES(mask_height),
              update_time = NOW()
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int upsert(PatternConfigEntity entity);

    /**
     * 查询指定项目和图解的阅读配置。
     */
    @Select("""
            SELECT id, project_id, pattern_id, current_page, mask_top_offset, mask_height, update_time
            FROM pattern_configs
            WHERE project_id = #{projectId} AND pattern_id = #{patternId}
            """)
    PatternConfigEntity selectByProjectIdAndPatternId(Long projectId, Long patternId);

    default Optional<PatternConfigEntity> findByProjectIdAndPatternId(Long projectId, Long patternId) {
        return Optional.ofNullable(selectByProjectIdAndPatternId(projectId, patternId));
    }
}
