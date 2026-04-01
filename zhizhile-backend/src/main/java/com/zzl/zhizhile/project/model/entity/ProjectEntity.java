package com.zzl.zhizhile.project.model.entity;

import java.time.LocalDateTime;

/**
 * 项目实体，对应一个手作项目的主信息。
 */
public class ProjectEntity {
    private Long id;
    private Long userId;
    private String name;
    // 项目类型（0:钩针 1:棒针 2:其他）
    private Long type;

    /**
     * 
     * status字段
        当前状态：这是一个"预留"字段，目前只有在创建项目时被赋值一次（"ACTIVE"），后续代码中没有实际检查或更新这个字段。
        它不参与任何业务逻辑判断（如列表过滤、状态转换等）。
        建议用途：该字段最可能被设计用于支持以下功能（当前未实现）：
        •  项目的生命周期管理（如 ACTIVE、ARCHIVED、DELETED 等状态）
        •  软删除机制（查询时过滤非 ACTIVE 项目）
        •  项目暂停/禁用功能
     */
    private String status;
    // 关联的图解ID
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

    public Long getType(){
        return type;
    }
    public void setType(Long type){
        this.type = type;
    }
}
