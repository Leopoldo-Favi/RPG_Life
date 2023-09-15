package com.example.rpg_life;

import static com.example.rpg_life.SkillActivity.findTaskPosByName;
import static com.example.rpg_life.SkillActivity.saveCurrentBookProgresses;
import static  com.example.rpg_life.SkillActivity.correctlySetMainPbProgress;
import static com.example.rpg_life.SkillActivity.checkProgressBar;
import static com.example.rpg_life.SkillActivity.removeElementAtIndex;

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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;


public class ViewBookDialog extends DialogFragment {

    private static final String ARG_TASKNAME = "taskName_key";
    private static final String ARG_MAXPROGRESS = "maxProgress_key";

    static SkillActivity context;

    String taskName;
    int maxProgress;

    Task[] tasks = {};

    private SharedPreferences sharedPreferences;
    private SavedActivityData savedActivityData;

    ProgressBar progressBar;
    ProgressBar mainProgressBar;
    TextView mainProgressBarText;
    TextView levelText;

    int taskCompletedReward = 50;

    TableLayout tl;
    View bookView;

    //prendi dati da skill activity
    public static ViewBookDialog newInstance(String taskName, int totPages /*String[] bookNames, String[] bookProgress, String[] bookPages, Task[] tasks*/) {
        ViewBookDialog fragment = new ViewBookDialog();
        Bundle args = new Bundle();

        args.putString(ARG_TASKNAME, taskName);
        args.putInt(ARG_MAXPROGRESS, totPages);
        fragment.setArguments(args);

        return fragment;
    }

    //prendi dati da skillActivity
    public void setReferences(SharedPreferences sharedPreferences, SavedActivityData savedActivityData, SkillActivity context, ProgressBar progressBar, ProgressBar mainProgressBar, TextView mainProgressBarText, TextView levelText, TableLayout tl, View bookView, Task[] tasks){
        this.sharedPreferences = sharedPreferences;
        this.savedActivityData = savedActivityData;
        this.context = context;
        this.progressBar = progressBar;
        this.mainProgressBar = mainProgressBar;
        this.mainProgressBarText = mainProgressBarText;
        this.levelText = levelText;
        this.tl = tl;
        this.bookView = bookView;
        this.tasks = tasks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.view_book_dialog, container, false);

        // Retrieve data from arguments
        taskName = getArguments().getString(ARG_TASKNAME);
        maxProgress = getArguments().getInt(ARG_MAXPROGRESS);

        //make the data visible by puttin it in the views
        TextView dialogTitle = (TextView) rootView.findViewById(R.id.dialog_title);
        dialogTitle.setText(taskName);

        //progress bar stuff
        final ProgressBar dialogProgressBar = (ProgressBar) rootView.findViewById(R.id.dialog_progress_bar);
        dialogProgressBar.setMax(maxProgress); //set the number of pages in the progress bar
        Log.d("dioporco", new Gson().toJson(tasks) + " " + taskName);
        Log.d("dioporco", String.valueOf(findTaskPosByName(tasks, taskName)));
        int progress = tasks[findTaskPosByName(tasks, taskName)].getCurrentProgress();
        dialogProgressBar.setProgress(progress); //also set the progress
        TextView dialogTextView = (TextView) rootView.findViewById(R.id.dialog_text_view);
        dialogTextView.setText(progress + "/" + maxProgress); //set it also in the textview


        //SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG
        LinearLayout progressShower = (LinearLayout) rootView.findViewById(R.id.dialog_progress_shower);
        progressShower.setOnClickListener( numberPickerDialog( taskName, dialogTextView, 0, maxProgress, "A che pagina sei arrivato?", dialogProgressBar, progressBar));
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

    public void checkTaskCompleted(ProgressBar pb, Context context){

        if(checkProgressBar(pb)){

            Toast toast = Toast.makeText(context /* SkillActivty */, "Hai finito la task!!!!", Toast.LENGTH_SHORT);
            toast.show();

            correctlySetMainPbProgress(mainProgressBar.getProgress() + taskCompletedReward, context); //exp reward

            //delete task data
            int position = findTaskPosByName(tasks, taskName);
            tasks = removeElementAtIndex(tasks, position);

            String jsonTasks = new Gson().toJson(tasks);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(SavedActivityData.TASKS, jsonTasks);
            editor.apply();
            //delete task data

            tl.removeView(bookView); //once u have completed the task remove it
            this.dismiss(); //dismiss the dialog, manca solo di far partire loadSharedPreferences di
        }
    }

    public void makeProgressChange(int progress, int mainProgressBarProgress, TextView dialogTextView, final ProgressBar pb1, final ProgressBar pb2){

        //set the numbers everywhere
        pb1.setProgress(progress);
        pb2.setProgress(progress);
        dialogTextView.setText(pb1.getProgress() + "/" + maxProgress);
        correctlySetMainPbProgress(mainProgressBarProgress, context);
        checkTaskCompleted(pb1, context);

        //save the new numbers
        if(!checkProgressBar(pb1)){//se la task non è completa (otherwise it throws an error)

            int posToChange = findTaskPosByName(tasks, taskName);
            //bookProgresses[posToChange] = String.valueOf(pb1.getProgress());
            tasks[posToChange].setCurrentProgress( pb1.getProgress() );
            saveCurrentBookProgresses(  new Gson().toJson(tasks), savedActivityData, sharedPreferences); //save the new array

        }

    }

    View.OnClickListener addProgress(int howMuch, TextView dialogTextView, final ProgressBar pb1, final ProgressBar pb2){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) { //pb1 and pb2 are basically always at the same progress

                //check that clicking the button makes sense
                if(pb2.getProgress() + howMuch <= pb2.getMax() && pb2.getProgress() + howMuch >= 0){

                    makeProgressChange(pb2.getProgress() + howMuch, mainProgressBar.getProgress() + howMuch, dialogTextView, pb1, pb2);
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

                                //dati per la main progress bar
                                int oldprogress = pb1.getProgress(); //pb1 è quella del dialogo
                                int difference = progress - oldprogress;

                                makeProgressChange(progress, mainProgressBar.getProgress() + difference, dialogTextView, pb1, pb2);

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
