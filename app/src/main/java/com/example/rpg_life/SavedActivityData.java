package com.example.rpg_life;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class SavedActivityData {
    private static final String BOOK_KEY = "book_key";
    private SharedPreferences sharedPreferences;

    public SavedActivityData(Context context, String fileName) { //appena creo un instanza di SavedActivityData passo da qui e apro lo SharedPreferences Giusto
        // Initialize the SharedPreferences object with the provided file name
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }


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

    //BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS//BOOKS
    public static final String NAMES_OF_ADDED_BOOKS = "namesOfAddedBooks";
    public static final String PAGES_OF_ADDED_BOOKS = "pagesOfAddedBooks";
    public static final String CURRENT_BOOK_PROGRESSES = "currentBookProgresses";

    private String namesOfAddedBooks = new String();
    private String pagesOfAddedBooks = new String();
    private String currentBookProgresses = new String();

    public String getNamesOfAddedBooks(){ return namesOfAddedBooks; }
    public String getPagesOfAddedBooks(){ return pagesOfAddedBooks; }
    public String getCurrentBookProgresses(){ return currentBookProgresses; }

    public void setCurrentBookProgresses(String jsonArray){ this.currentBookProgresses = jsonArray; }

}


/* --------------------------------------------------------------------------
    private Set<String> namesOfAddedBooks = new LinkedHashSet<String>();
    private Set<String> pagesOfAddedBooks = new LinkedHashSet<String>();
    private Set<String> currentBookProgresses = new LinkedHashSet<String>();

    public Set<String> getNamesOfAddedBooks(){ return namesOfAddedBooks; }
    public Set<String> getPagesOfAddedBooks(){ return pagesOfAddedBooks; }

    public Set<String> getCurrentBookProgresses(){ return currentBookProgresses; }
    public void setCurrentBookProgresses(int[] int_currentBookProgresses){

        //convert an integer array into a Set<String>
        Set<String> stringSet = new LinkedHashSet<String>();
        for (int i : int_currentBookProgresses) {
            stringSet.add(String.valueOf(i));
        }

        this.currentBookProgresses = stringSet;
    }



    public void addValueToSet(Set<String> set, String value){ set.add(value); }*/