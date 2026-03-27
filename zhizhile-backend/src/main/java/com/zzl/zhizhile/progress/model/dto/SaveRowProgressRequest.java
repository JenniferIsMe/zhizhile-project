package com.zzl.zhizhile.progress.model.dto;

public class SaveRowProgressRequest {
    private Integer currentRowIndex;
    private Integer totalCount;

    public Integer getCurrentRowIndex() { return currentRowIndex; }
    public void setCurrentRowIndex(Integer currentRowIndex) { this.currentRowIndex = currentRowIndex; }
    public Integer getTotalCount() { return totalCount; }
    public void setTotalCount(Integer totalCount) { this.totalCount = totalCount; }
}
