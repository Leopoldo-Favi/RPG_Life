package com.example.rpg_life;

public class ProgressTask extends Task{
    int maxProgress;
    int currentProgress;

    public ProgressTask(String nameP, int rewardExperienceP, int maxProgressP, int currentProgressP){//P as in parameter
        super(nameP, rewardExperienceP);
        maxProgress = maxProgressP;
        currentProgress = currentProgressP;
    }

    public int getMaxProgress(){ return maxProgress; }
    public int getCurrentProgress(){ return currentProgress; }

}
