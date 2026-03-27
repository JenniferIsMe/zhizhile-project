package com.zzl.zhizhile.configstate.model.dto;

public class SavePatternConfigRequest {
    private Integer currentPage;
    private Integer maskTopOffset;
    private Integer maskHeight;

    public Integer getCurrentPage() { return currentPage; }
    public void setCurrentPage(Integer currentPage) { this.currentPage = currentPage; }
    public Integer getMaskTopOffset() { return maskTopOffset; }
    public void setMaskTopOffset(Integer maskTopOffset) { this.maskTopOffset = maskTopOffset; }
    public Integer getMaskHeight() { return maskHeight; }
    public void setMaskHeight(Integer maskHeight) { this.maskHeight = maskHeight; }
}
