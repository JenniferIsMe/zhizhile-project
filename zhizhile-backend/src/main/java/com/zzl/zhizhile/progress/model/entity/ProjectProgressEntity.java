package com.zzl.zhizhile.progress.model.entity;

import java.time.LocalDateTime;

/**
 * 项目进度实体，保存计数和计时的汇总结果。
 */
public class ProjectProgressEntity {
    private Long id;
    private Long projectId;
    private Integer currentRowIndex;
    private Integer totalCount;
    private Long totalSeconds;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Integer getCurrentRowIndex() { return currentRowIndex; }
    public void setCurrentRowIndex(Integer currentRowIndex) { this.currentRowIndex = currentRowIndex; }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
    public Long getTotalSeconds() { return totalSeconds; }
    public void setTotalSeconds(Long totalSeconds) { this.totalSeconds = totalSeconds; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
