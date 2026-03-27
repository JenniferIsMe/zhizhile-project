package com.zzl.zhizhile.project.model.entity;

import java.time.LocalDateTime;

/**
 * 项目实体，对应一个手作项目的主信息。
 */
public class ProjectEntity {
    private Long id;
    private Long userId;
    private String name;
    private String status;
    private Long currentPatternId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCurrentPatternId() { return currentPatternId; }
    public void setCurrentPatternId(Long currentPatternId) { this.currentPatternId = currentPatternId; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}
