package com.example.rpg_life;

import static com.example.rpg_life.SkillActivity.saveCurrentTaskArray;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class CheckboxTask extends Task{

    @Expose
    boolean isChecked;
    CheckBox checkbox; //JSON ISN'T CAPABLE OF SAVING THESE COMPLEX TYPES
    public CheckboxTask(SkillActivity sa, String nameP, int rewardExperienceP, boolean isCheckedP, ProgressBar SAmainProgressBarP, TextView SAmainProgressBarTextP, TextView SAlevelTextP){
        super(sa, nameP, rewardExperienceP, R.layout.checkbox_task, SAmainProgressBarP, SAmainProgressBarTextP, SAlevelTextP);
        this.isChecked = isCheckedP;
    }
}
