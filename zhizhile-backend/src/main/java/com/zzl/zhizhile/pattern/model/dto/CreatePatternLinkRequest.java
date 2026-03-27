package com.zzl.zhizhile.pattern.model.dto;

public class CreatePatternLinkRequest {
    private String url;
    private String displayName;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
