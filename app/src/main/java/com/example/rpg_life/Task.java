package com.example.rpg_life;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.util.Arrays;

public class Task{

    Gson Gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    SkillActivity skillActivityInstance;  //JSON ISN'T CAPABLE OF SAVING THESE COMPLEX TYPES
    SharedPreferences SAsharedPreferences; //SA as in SkillActivity
    SavedActivityData SAsavedActivityData;
    TableLayout SAtl;
    Task[] SAtasks;
    ProgressBar SAmainProgressBar;
    TextView SAmainProgressBarText;
    TextView SAlevelText;

    @Expose
    String name;
    @Expose
    int rewardExp;
    @Expose
    int taskLayout;
    // type? date; ??

    public Task(SkillActivity SA, String nameP, int rewardExperienceP, int taskLayoutP, ProgressBar SAmainProgressBarP, TextView SAmainProgressBarTextP, TextView SAlevelTextP){ //P as in parameter
        //Parent skillActivityInstance
        skillActivityInstance = SA;
        SAsharedPreferences = skillActivityInstance.sharedPreferences; //SA as in SkillActivity
        SAsavedActivityData = skillActivityInstance.savedActivityData;
        SAtl = skillActivityInstance.tl;
        SAtasks = skillActivityInstance.tasks;
        Log.d("dioporcoTask", Arrays.toString(skillActivityInstance.tasks));
        SAmainProgressBar = SAmainProgressBarP;
        SAmainProgressBarText = SAmainProgressBarTextP;
        SAlevelText = SAlevelTextP;

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
