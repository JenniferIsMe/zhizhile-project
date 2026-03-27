package com.zzl.zhizhile.configstate.service;

import com.zzl.zhizhile.configstate.mapper.PatternConfigMapper;
import com.zzl.zhizhile.configstate.model.dto.SavePatternConfigRequest;
import com.zzl.zhizhile.configstate.model.entity.PatternConfigEntity;
import org.springframework.stereotype.Service;

/**
 * 阅读配置服务，负责阅读状态保存与恢复。
 */
@Service
public class PatternConfigService {
    private final PatternConfigMapper patternConfigMapper;

    public PatternConfigService(PatternConfigMapper patternConfigMapper) {
        this.patternConfigMapper = patternConfigMapper;
    }

    /**
     * 保存图解阅读配置，采用覆盖写入。
     */
    public PatternConfigEntity save(Long projectId, Long patternId, SavePatternConfigRequest request) {
        PatternConfigEntity entity = new PatternConfigEntity();
        entity.setProjectId(projectId);
        entity.setPatternId(patternId);
        entity.setCurrentPage(request.getCurrentPage());
        entity.setMaskTopOffset(request.getMaskTopOffset());
        entity.setMaskHeight(request.getMaskHeight());
        return patternConfigMapper.upsert(entity);
    }

    /**
     * 查询图解阅读配置，不存在时返回默认值。
     */
    public PatternConfigEntity get(Long projectId, Long patternId) {
        return patternConfigMapper.findByProjectIdAndPatternId(projectId, patternId)
                .orElseGet(() -> {
                    PatternConfigEntity def = new PatternConfigEntity();
                    def.setProjectId(projectId);
                    def.setPatternId(patternId);
                    def.setCurrentPage(1);
                    def.setMaskTopOffset(0);
                    def.setMaskHeight(0);
                    return def;
                });
    }
}
