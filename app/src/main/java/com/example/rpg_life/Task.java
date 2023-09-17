package com.example.rpg_life;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Task{

    Gson Gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    SkillActivity skillActivityInstance;  //JSON ISN'T CAPABLE OF SAVING THESE COMPLEX TYPES

    @Expose
    String name;
    @Expose
    int rewardExp;
    @Expose
    int taskLayout;
    // type? date; ??

    public Task(SkillActivity sa, String nameP, int rewardExperienceP, int taskLayoutP){ //P as in parameter
        //Parent skillActivityInstance
        //skillActivityInstance = sa;

        //other attributes
        this.name = nameP;
        this.rewardExp = rewardExperienceP;
        this.taskLayout = taskLayoutP;
    }

   //REFERENCE FUNCTIONS
    public int getMaxProgress(){ return 0; }  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask
    public int getCurrentProgress(){ return 0; }  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask
    public void setCurrentProgress(int progress){}  //QUESTA FUNZIONE IN REALTA E' VIENE DEFINITA IN ProgressTask

}
