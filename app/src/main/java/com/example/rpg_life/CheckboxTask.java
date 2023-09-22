package com.example.rpg_life;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class CheckboxTask extends Task {

    @Expose
    boolean isChecked;
    CheckBox checkbox; //JSON ISN'T CAPABLE OF SAVING THESE COMPLEX TYPES
    public CheckboxTask(SkillActivity sa, String nameP, int rewardExperienceP, boolean isCheckedP, ProgressBar SAmainProgressBarP, TextView SAmainProgressBarTextP, TextView SAlevelTextP){
        super(sa, nameP, rewardExperienceP, R.layout.checkbox_task, SAmainProgressBarP, SAmainProgressBarTextP, SAlevelTextP);
        this.isChecked = isCheckedP;
    }

    public View.OnClickListener onClickListener(final Class activity, final String title, final String levelValue){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(skillActivityInstance, activity);
                intent.putExtra("titleValue", title);
                intent.putExtra("levelValue", levelValue);
                intent.putExtra("maxProgressValue", 100);
                intent.putExtra("progressValue", 0);

                startActivity(skillActivityInstance, intent, null);
            }
        };
    }
}
