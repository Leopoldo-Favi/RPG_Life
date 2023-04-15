package com.example.rpg_life;

import android.app.Application;
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
    public static final String LEVEL_OF_ADDED_VIEWS = "levelOfAddedViews";


    private Set<String> namesOfAddedViews = new HashSet<String>();;
    private Set<String> progressOfAddedViews = new HashSet<String>();;
    private Set<String> levelOfAddedViews = new HashSet<String>();;

    public Set<String> getNamesOfAddedViews(){ return namesOfAddedViews; }
    public Set<String> getProgressOfAddedViews(){ return progressOfAddedViews; }
    public Set<String> getLevelOfAddedViews(){ return levelOfAddedViews; }

    public void addValueToSet(Set<String> set, String value){ set.add(value); }

}
