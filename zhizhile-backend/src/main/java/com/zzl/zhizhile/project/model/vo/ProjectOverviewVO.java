package com.zzl.zhizhile.project.model.vo;

import com.zzl.zhizhile.configstate.model.entity.PatternConfigEntity;
import com.zzl.zhizhile.pattern.model.vo.PatternVO;
import com.zzl.zhizhile.progress.model.entity.ProjectProgressEntity;
import java.util.ArrayList;
import java.util.List;

public class ProjectOverviewVO {
    private ProjectDetailVO project;
    private PatternVO currentPattern;
    private List<PatternVO> patterns = new ArrayList<>();
    private PatternConfigEntity patternConfig;
    private ProjectProgressEntity progress;

    public ProjectDetailVO getProject() { return project; }
    public void setProject(ProjectDetailVO project) { this.project = project; }
    public PatternVO getCurrentPattern() { return currentPattern; }
    public void setCurrentPattern(PatternVO currentPattern) { this.currentPattern = currentPattern; }
    public List<PatternVO> getPatterns() { return patterns; }
    public void setPatterns(List<PatternVO> patterns) { this.patterns = patterns; }
    public PatternConfigEntity getPatternConfig() { return patternConfig; }
    public void setPatternConfig(PatternConfigEntity patternConfig) { this.patternConfig = patternConfig; }
    public ProjectProgressEntity getProgress() { return progress; }
    public void setProgress(ProjectProgressEntity progress) { this.progress = progress; }
}
