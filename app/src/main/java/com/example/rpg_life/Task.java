package com.example.rpg_life;

public class Task{

    String name;
    int rewardExperience;
    int taskLayout;
    // type? date; ??

    public Task(String nameP, int rewardExperienceP, int taskLayoutP){ //P as in parameter
        this.name = nameP;
        this.rewardExperience = rewardExperienceP;
        this.taskLayout = taskLayoutP;
    }

   //REFERENCE FUNCTIONS
    public int getMaxProgress(){ return 0; }  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask
    public int getCurrentProgress(){ return 0; }  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask
    public void setCurrentProgress(int progress){}  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask

}
