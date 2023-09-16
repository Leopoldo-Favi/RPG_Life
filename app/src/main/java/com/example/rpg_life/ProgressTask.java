package com.example.rpg_life;

import com.google.gson.annotations.Expose;

public class ProgressTask extends Task{
    @Expose
    private int maxProgress;
    @Expose
    private int currentProgress;

    public ProgressTask(SkillActivity sa, String nameP, int rewardExperienceP, int maxProgressP, int currentProgressP){//P as in parameter
        super(sa, nameP, rewardExperienceP, R.layout.progress_task);
        this.maxProgress = maxProgressP;
        this.currentProgress = currentProgressP;
    }

    public int getMaxProgress(){ return maxProgress; }
    public int getCurrentProgress(){ return currentProgress; }
    public void setCurrentProgress(int progress){this.currentProgress = progress; }

}
