package com.example.rpg_life;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SavedData extends Application {

    public static final String PREFERENCES = "preferences";

    //have you saved something?//have you saved something
    public static final String SOMETHING_SAVED = "somethingSaved";
    public static final Boolean TRUE = Boolean.TRUE;
    public static final Boolean FALSE = Boolean.FALSE;

    private String somethingSaved;

    /*private SharedPreferences sharedPreferences;

    public SavedData(Context applicationContext) {
        // Initialize the SharedPreferences object with the provided Context
        sharedPreferences = applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }*/

    public String getSomethingSaved(){
        return somethingSaved;
    }

    public void setSomethingSaved(String somethingSaved){
        this.somethingSaved = somethingSaved;
    }



    //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR //BUTTON COLOR
    public static final String CUSTOM_COLOR = "customColor";
    public static final String COLOR1 = "#6200EE";
    public static final String COLOR2 = "#03DAC5"; //accent

    private String customColor;

    public String getCustomColor(){
        return customColor;
    }

    public void setCustomColor(String customColor){
        this.customColor = customColor;
    }


    //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES //ACTIVITIES
    public static final String NAMES_OF_ADDED_VIEWS = "namesOfAddedViews";
    public static final String PROGRESS_OF_ADDED_VIEWS = "progressOfAddedViews";
    public static final String MAX_PROGRESS_OF_ADDED_VIEWS = "maxProgressOfAddedViews";
    public static final String LEVEL_OF_ADDED_VIEWS = "levelOfAddedViews";


    private String namesOfAddedViews = new String();
    private String progressOfAddedViews = new String();
    private String maxProgressOfAddedViews = new String();
    private String levelOfAddedViews = new String();


    public String getNamesOfAddedViews(){ return namesOfAddedViews; }
    public String getProgressOfAddedViews(){ return progressOfAddedViews; }
    public String getMaxProgressOfAddedViews(){ return maxProgressOfAddedViews; }
    public String getLevelOfAddedViews(){ return levelOfAddedViews; }


    public void setProgressOfAddedViews(String jsonArray){ this.progressOfAddedViews = jsonArray; }
    public void addValueToSet(Set<String> set, String value){ set.add(value); }

}
