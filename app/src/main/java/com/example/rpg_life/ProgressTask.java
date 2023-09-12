package com.example.rpg_life;

public class ProgressTask extends Task{
    int maxProgress;
    int currentProgress;

    public ProgressTask(String nameP, int rewardExperienceP, int maxProgressP){//P as in parameter
        name = nameP;
        rewardExperience = rewardExperienceP;
        maxProgress = maxProgressP;
        //currentProgress = currentProgressP;
    }

    public int getMaxProgress(){ return maxProgress; }

}
