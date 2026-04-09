package com.zzl.zhizhile.project.model.dto;

public class CreateProjectRequest {
    private String name;
    // 项目类型（0:钩针 1:棒针）
    private Long type;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getType() { return type; }
    public void setType(Long type) { this.type = type; }
}
