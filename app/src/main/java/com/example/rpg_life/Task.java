package com.example.rpg_life;

public class Task{

    String name;
    int rewardExperience;
    // type? date; ??

    public Task(String nameP, int rewardExperienceP){ //P as in parameter
        name = nameP;
        rewardExperience = rewardExperienceP;
    }

   //REFERENCE FUNCTIONS
    public int getMaxProgress(){ return 0; }  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask
    public int getCurrentProgress(){ return 0; }  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask
    public void setCurrentProgress(int progress){}  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask

}
