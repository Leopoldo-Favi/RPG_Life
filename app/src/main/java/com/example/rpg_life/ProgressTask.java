package com.example.rpg_life;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.annotations.Expose;

import java.util.Arrays;

public class ProgressTask extends Task{
    @Expose
    private int maxProgress;
    @Expose
    private int currentProgress;

    public ProgressTask(SkillActivity sa, String nameP, int rewardExperienceP, int maxProgressP, int currentProgressP, ProgressBar SAmainProgressBarP, TextView SAmainProgressBarTextP, TextView SAlevelTextP){//P as in parameter
        super(sa, nameP, rewardExperienceP, R.layout.progress_task, SAmainProgressBarP, SAmainProgressBarTextP, SAlevelTextP);
        this.maxProgress = maxProgressP;
        this.currentProgress = currentProgressP;
    }

    public int getMaxProgress(){ return maxProgress; }
    public int getCurrentProgress(){ return currentProgress; }
    public void setCurrentProgress(int progress){this.currentProgress = progress; }

    //OnClickListener per add_book.xml
    public View.OnClickListener onClickListener(final String taskName, final int maxProgress, final int rewardExp, final ProgressBar progressBar, View bookView){ //qua invece ne prendo esattamente 1 tutto strano

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SAtasks = skillActivityInstance.tasks; //update SAtasks

                //apri classe dialogFragment ViewBookDialog
                ViewBookDialog dialogFragment = ViewBookDialog.newInstance(taskName, maxProgress);
                dialogFragment.setReferences(SAsharedPreferences, SAsavedActivityData, skillActivityInstance, progressBar, SAmainProgressBar, SAmainProgressBarText, SAlevelText, SAtl, bookView, SAtasks, rewardExp);
                dialogFragment.show(skillActivityInstance.getSupportFragmentManager(), "custom_dialog");

            }
        };
    }

}
