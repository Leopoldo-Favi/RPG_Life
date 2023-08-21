package com.example.rpg_life;

import static com.example.rpg_life.SkillActivity.findStringPosition;
import static com.example.rpg_life.SkillActivity.jsonToStringArray;
import static com.example.rpg_life.SkillActivity.saveCurrentBookProgresses;
import static  com.example.rpg_life.SkillActivity.correctlySetMainPbProgress;


import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class ViewBookDialog extends DialogFragment {

    private static final String ARG_BOOKNAME = "bookName_key";
    private static final String ARG_TOTPAGES = "totPages_key";
    private static final String ARG_BOOKNAMES = "BookNames_key";
    private static final String ARG_BOOKPROGRESS = "BookProgress_key";

    static Context context;

    String bookName;
    int totPages;
    String[] bookNames;
    String[] bookProgress;

    private SharedPreferences sharedPreferences;
    private SavedActivityData savedActivityData;

    ProgressBar progressBar;
    ProgressBar mainProgressBar;
    TextView mainProgressBarText;
    TextView levelText;

    //prendi dati da skill activity
    public static ViewBookDialog newInstance(String bookName, int totPages, String[] bookNames, String[] bookProgress) {
        ViewBookDialog fragment = new ViewBookDialog();
        Bundle args = new Bundle();

        args.putString(ARG_BOOKNAME, bookName);
        args.putInt(ARG_TOTPAGES, totPages);
        args.putStringArray(ARG_BOOKNAMES, bookNames);
        args.putStringArray(ARG_BOOKPROGRESS, bookProgress);
        fragment.setArguments(args);

        return fragment;
    }

    //prendi dati da skillActivity
    public void setReferences(SharedPreferences sharedPreferences, SavedActivityData savedActivityData, Context context, ProgressBar progressBar, ProgressBar mainProgressBar, TextView mainProgressBarText, TextView levelText){
        this.sharedPreferences = sharedPreferences;
        this.savedActivityData = savedActivityData;
        this.context = context;
        this.progressBar = progressBar;
        this.mainProgressBar = mainProgressBar;
        this.mainProgressBarText = mainProgressBarText;
        this.levelText = levelText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.view_book_dialog, container, false);

        // Retrieve data from arguments
        bookName = getArguments().getString(ARG_BOOKNAME);
        totPages = getArguments().getInt(ARG_TOTPAGES);
        bookNames = getArguments().getStringArray(ARG_BOOKNAMES);
        bookProgress = getArguments().getStringArray(ARG_BOOKPROGRESS);

        //make the data visible by puttin it in the views
        TextView dialogTitle = (TextView) rootView.findViewById(R.id.dialog_title);
        dialogTitle.setText(bookName);

        //progress bar stuff
        final ProgressBar dialogProgressBar = (ProgressBar) rootView.findViewById(R.id.dialog_progress_bar);
        dialogProgressBar.setMax(totPages); //set the number of pages in the progress bar
        int progress = Integer.parseInt( bookProgress[findStringPosition(bookNames, bookName)] );
        dialogProgressBar.setProgress(progress); //also set the progress
        TextView dialogTextView = (TextView) rootView.findViewById(R.id.dialog_text_view);
        dialogTextView.setText(progress + "/" + totPages); //set it also in the textview


        //SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG
        LinearLayout progressShower = (LinearLayout) rootView.findViewById(R.id.dialog_progress_shower);
        progressShower.setOnClickListener( numberPickerDialog( bookName, dialogTextView, 0, totPages, "A che pagina sei arrivato?", dialogProgressBar, progressBar));
        //non è molto pulito che passi esattamente 2 progress bar e non quante me ne servono caso per caso però era più semplice


        //metti gli on click listener sui bottoni per aggiungere o diminuire pagine
        ImageButton minus10Button = (ImageButton) rootView.findViewById(R.id.minus_10_button);
        ImageButton minus1Button = (ImageButton) rootView.findViewById(R.id.minus_1_button);
        ImageButton plus1Button = (ImageButton) rootView.findViewById(R.id.plus_1_button);
        ImageButton plus10Button = (ImageButton) rootView.findViewById(R.id.plus_10_button);

        minus10Button.setOnClickListener( addProgress(-10, dialogTextView, dialogProgressBar, progressBar));
        minus1Button.setOnClickListener( addProgress( -1, dialogTextView, dialogProgressBar, progressBar));
        plus1Button.setOnClickListener( addProgress(1, dialogTextView, dialogProgressBar, progressBar));
        plus10Button.setOnClickListener( addProgress( 10, dialogTextView, dialogProgressBar, progressBar));

        return rootView;
    }





    View.OnClickListener addProgress(int howMuch, TextView dialogTextView, final ProgressBar pb1, final ProgressBar pb2){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) { //pb1 and pb2 are basically always at the same progress

                //check that clicking the button makes sense
                if(pb2.getProgress() + howMuch <= pb2.getMax() && pb2.getProgress() + howMuch >= 0){

                    //set the numbers everywhere
                    pb1.setProgress(pb1.getProgress() + howMuch);
                    pb2.setProgress(pb2.getProgress() + howMuch);
                    dialogTextView.setText(pb1.getProgress() + "/" + totPages);
                    correctlySetMainPbProgress(mainProgressBar.getProgress() + howMuch, context);

                    //save the new numbers
                    if(sharedPreferences.getBoolean(SavedActivityData.SOMETHING_SAVED, Boolean.FALSE)){//serve sto something saved? Comunque quando arrivi qua qualcosa l'hai gia salvato in teoria

                        int posToChange = findStringPosition(bookNames, bookName);
                        bookProgress[posToChange] = String.valueOf(pb1.getProgress());
                        saveCurrentBookProgresses(  new Gson().toJson(bookProgress), savedActivityData, sharedPreferences); //save the new array

                    }

                }

            }
        };
    }

    //quando clicchi apri un dialogo con un number Picker e metti il numero dentro a *2* progress bar (da mettere apposto) ---> //non è molto pulito che passi esattamente 2 progress bar e non quante me ne servono caso per caso però era più semplice
    public View.OnClickListener numberPickerDialog (final String bookName, TextView dialogTextView, final int min, final int max, final String title, final ProgressBar pb1, final ProgressBar pb2){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View setProgress = getLayoutInflater().inflate(R.layout.set_progress_dialog, null);

                final NumberPicker numberPicker = (NumberPicker) setProgress.findViewById(R.id.progressPicker); //imposta il number picker
                numberPicker.setMinValue(min);
                numberPicker.setMaxValue(max);

                AlertDialog set_progress_dialog = new AlertDialog.Builder(context) //sto context è SkillActivity.this
                        .setView(setProgress)
                        .setTitle(title)
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("ok", null) //è null ma lo definiamo qua sotto
                        .create();

                set_progress_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button b = ((AlertDialog) set_progress_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int progress = numberPicker.getValue(); //prendi il valore scelto dall'utente

                                //setta la main progress bar
                                int oldprogress = pb1.getProgress(); //pb1 è quella del dialogo
                                int difference = progress - oldprogress;
                                correctlySetMainPbProgress(mainProgressBar.getProgress() + difference, context);

                                pb1.setProgress(progress); //metti il nuovo valore di progress sulla progress bar lì e pure dentro la instance di skillActivity
                                pb2.setProgress(progress);
                                dialogTextView.setText(progress + "/" + max); //max è totpages

                                //change just the position where bookNames[pos] = bookName with the new page the user has arrived to if somethingSaved
                                if(sharedPreferences.getBoolean(SavedActivityData.SOMETHING_SAVED, Boolean.FALSE)){//serve sto something saved? Comunque quando arrivi qua qualcosa l'hai gia salvato in teoria

                                    int posToChange = findStringPosition(bookNames, bookName);
                                    bookProgress[posToChange] = String.valueOf(progress);
                                    saveCurrentBookProgresses(  new Gson().toJson(bookProgress), savedActivityData, sharedPreferences); //save the new array

                                }

                                set_progress_dialog.dismiss(); //esci dal dialogo
                            }
                        });
                    }
                });

                set_progress_dialog.show();
            }
        };
    }

}
