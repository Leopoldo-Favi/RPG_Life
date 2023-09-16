package com.example.rpg_life;

public class ProgressTask extends Task{
    private int maxProgress;
    private int currentProgress;

    public ProgressTask(String nameP, int rewardExperienceP, int maxProgressP, int currentProgressP){//P as in parameter
        super(nameP, rewardExperienceP, R.layout.progress_task);
        this.maxProgress = maxProgressP;
        this.currentProgress = currentProgressP;
    }

    public int getMaxProgress(){ return maxProgress; }
    public int getCurrentProgress(){ return currentProgress; }
    public void setCurrentProgress(int progress){this.currentProgress = progress; }

}
