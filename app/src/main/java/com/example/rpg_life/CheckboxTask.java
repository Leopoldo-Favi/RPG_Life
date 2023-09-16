package com.example.rpg_life;

import android.util.Log;

public class CheckboxTask extends Task{

    boolean isChecked = false;
    public CheckboxTask(String nameP, int rewardExperienceP, boolean isCheckedP){
        super(nameP, rewardExperienceP, R.layout.checkbox_task);
        this.isChecked = isCheckedP;
    }
}
