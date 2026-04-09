package com.zzl.zhizhile.project.model.vo;

public class ProjectDetailVO {
    private Long id;
    private String name;
    // 项目类型（0:钩针 1:棒针）
    private Long type;
    private Long currentPatternId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getType() { return type; }
    public void setType(Long type) { this.type = type; }
    public Long getCurrentPatternId() { return currentPatternId; }
    public void setCurrentPatternId(Long currentPatternId) { this.currentPatternId = currentPatternId; }
}
