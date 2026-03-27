package com.zzl.zhizhile.configstate.model.entity;

import java.time.LocalDateTime;

/**
 * 阅读配置实体，按项目和图解维度保存阅读位置与遮罩状态。
 */
public class PatternConfigEntity {
    private Long id;
    private Long projectId;
    private Long patternId;
    private Integer currentPage;
    private Integer maskTopOffset;
    private Integer maskHeight;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getPatternId() { return patternId; }
    public void setPatternId(Long patternId) { this.patternId = patternId; }
    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    public Integer getMaskTopOffset() { return maskTopOffset; }
    public void setMaskTopOffset(Integer maskTopOffset) { this.maskTopOffset = maskTopOffset; }
    public Integer getMaskHeight() { return maskHeight; }
    public void setMaskHeight(Integer maskHeight) { this.maskHeight = maskHeight; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
