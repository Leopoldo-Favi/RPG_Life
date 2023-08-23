package com.example.rpg_life;

import static com.example.rpg_life.SkillActivity.getLevelInt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;


public class MainActivity extends AppCompatActivity{

    Handler mHandler = new Handler();

    //variabili di funzionamento per l'animazione dell'aggiungimento delle attività
    TextView actitivyNameView;
    TextView levelNumber;
    ProgressBar pb;
    ProgressBar pbl;
    ProgressBar pbr;
    TextView levelNumberL;
    TextView levelNumberR;

    View firstButton;
    View pbBtn;
    View btn;

    //per salvare
    private static MainActivity instance;
    private static SavedData savedData;
    private static SharedPreferences sharedPreferences;
    Boolean somethingSaved;
    String[] names = {};
    String[] progress = {};
    Integer[] maxProgress = {};
    String[] level = {};

    Button b;

    private boolean comingFromAnotherActivity = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected void onResume() {
        super.onResume();

        if(comingFromAnotherActivity){

            LinearLayout tl = findViewById(R.id.linearLayout);
            //remove all views except 5 (all activities)
            int childCount = tl.getChildCount();
            int viewsToKeep = 5; //ci sono 5 views a caso in questo momento
            if (childCount > viewsToKeep) {
                for (int i = childCount - 1; i >= viewsToKeep; i--) {
                    View childView = tl.getChildAt(i);
                    tl.removeView(childView);
                }
            }
            //and then reput them in
            loadSharedPreferences(sharedPreferences);

            comingFromAnotherActivity = false;
        }
    }


    protected void onPause() {
        super.onPause();
        comingFromAnotherActivity = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //scritto io
        final LinearLayout tl = findViewById(R.id.linearLayout);


        //metti il primo add_activitybtn
        firstButton = getLayoutInflater().inflate(R.layout.add_activitybtn, null, false);
        tl.addView(firstButton);


        //action bar support
        Toolbar actionBar = (Toolbar) findViewById(R.id.ActionBar);
        setSupportActionBar(actionBar);

        //bottoni inutili
        Button sceneButton = findViewById(R.id.sceneButton2);
        Button expButton = findViewById(R.id.button);
        ImageButton addActivity = findViewById(R.id.addActivity_btn);
        //final ProgressBar bar = findViewById(R.id.progressBar);

        //dovrebbe stare in per salvare ma proviamo qua
        sharedPreferences = AppPreferences.getInstance(this).getSharedPreferences();

        //add an activity
        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(1,firstButton, getLayoutInflater().inflate(R.layout.add_activity_dialog, null), sharedPreferences);
            }

        });

        //add progress
        expButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences(savedData.PREFERENCES, MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();

                /*bar.setProgress(bar.getProgress() + 10);

                //if the progress reaches max reset
                if(bar.getProgress() >= bar.getMax()){
                    bar.setProgress(0);
                    bar.setMax(bar.getMax() + 100);
                }*/
            }
        });


        //roba per salvare
        b = findViewById(R.id.dux);

        savedData = (SavedData) getApplicationContext();
        instance = this;
        loadSharedPreferences(sharedPreferences);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((ColorDrawable) v.getBackground()).getColor() == Color.parseColor("#03DAC5")){
                    savedData.setCustomColor(SavedData.COLOR1);
                }else{
                    savedData.setCustomColor(SavedData.COLOR2);
                }

                SharedPreferences.Editor editor = getSharedPreferences(savedData.PREFERENCES, MODE_PRIVATE).edit();
                editor.putString(SavedData.CUSTOM_COLOR, savedData.getCustomColor());
                editor.apply();
                updateView();

            }
        });

        //change activity
        sceneButton.setOnClickListener(openActivity(SkillActivity.class, "Ciao", "Level 1"));

    }

    public static SavedData getSavedDataInstance() {
        return savedData;
    }
    public static SharedPreferences getSharedPreferencesInstance(){
        return sharedPreferences;
    }
    public static MainActivity getMainActivityInstance(){ return instance;}

    private void updateView() {

        if(savedData.getCustomColor().equals(SavedData.COLOR1)){
            b.setBackgroundColor(Color.parseColor("#6200EE")); //primary
        }else{
            b.setBackgroundColor(Color.parseColor("#03DAC5")); //accent color2
        }
    }


    //SharedPreferences sharedPreferences, String sharedPrefKey, Set<String> setToRestore
    String[] jsonToStringArray(String jsonArray){
        // Convert the JSON string back to an array using Gson
        Type type = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(jsonArray, type);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void loadSharedPreferences(SharedPreferences sharedPreferences) {

        //have you saved something?//have you saved something
        Boolean somethingSaved = sharedPreferences.getBoolean(SavedData.SOMETHING_SAVED, Boolean.FALSE);

        //button color
        String bgc = sharedPreferences.getString(SavedData.CUSTOM_COLOR, ""); //non cambia nulla se metti COLOR1 o COLOR2
        savedData.setCustomColor(bgc);
        updateView();

        //activities
        names = jsonToStringArray(sharedPreferences.getString(SavedData.NAMES_OF_ADDED_VIEWS, "[]"));
        progress = jsonToStringArray(sharedPreferences.getString(SavedData.PROGRESS_OF_ADDED_VIEWS, "[]"));
        level = jsonToStringArray(sharedPreferences.getString(SavedData.LEVEL_OF_ADDED_VIEWS, "[]"));
        maxProgress = new Gson().fromJson(sharedPreferences.getString(SavedData.MAX_PROGRESS_OF_ADDED_VIEWS, "[]"), new TypeToken<Integer[]>() {}.getType()); //questo è praticamente jsonToIntArray ma mi faceva fatica scriverlo

        //names.length != 0 prima era somethingSaved
        if(names.length != 0){ //se ci sono activities salvate rimettile in pagina

            firstRecursiveFuncion((LinearLayout) findViewById(R.id.linearLayout), firstButton, names[0], Integer.parseInt(progress[0]), maxProgress[0], level[0], sharedPreferences); //il viewToRemove è firstButton
            //per ogni activty salvata (inizia da i=1: quindi il secondo oggetto
            for(int i=1; i<names.length; i++){

                //per i pari chiama first
                if (i % 2 == 0) {
                    firstRecursiveFuncion((LinearLayout) findViewById(R.id.linearLayout), btn, names[i], Integer.parseInt(progress[i]), maxProgress[i], level[i], sharedPreferences);
                }
                //per i dispari chiama second
                else {
                    secondRecursiveFuncion((LinearLayout) findViewById(R.id.linearLayout), pbBtn, names[i], Integer.parseInt(progress[i]), maxProgress[i], level[i], sharedPreferences);
                }
            }
        }else if(comingFromAnotherActivity){ //se non c'è nulla di salvato e stiamo comingFromAnotherActivity
            LinearLayout tl = findViewById(R.id.linearLayout);
            tl.addView(firstButton);
        }
    }

    public static <T> T[] addValueToArray(T[] array, T newValue) {

        Class<?> arrayType = array.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(arrayType, array.length + 1);

        // Copy existing elements to the new array
        for (int i = 0; i < array.length; i++) {
            newArray[i] = array[i];
        }

        // Add the new value to the end of the new array
        newArray[newArray.length - 1] = newValue;

        return newArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void saveActivityData(SharedPreferences sharedPreferences){

        names = addValueToArray(names, actitivyNameView.getText().toString());
        progress = addValueToArray(progress, "0");
        level = addValueToArray(level, "Level 1");
        maxProgress = addValueToArray(maxProgress, 250);

        //trasformali in json
        String jsonNames = new Gson().toJson(names);
        String jsonProgress = new Gson().toJson(progress);
        String jsonMaxProgress = new Gson().toJson(maxProgress);
        String jsonLevel = new Gson().toJson(level);

        /*
        savedData.addValueToSet(savedData.getNamesOfAddedViews(), actitivyNameView.getText().toString());
        savedData.addValueToSet(savedData.getProgressOfAddedViews(), Integer.toString(pb.getProgress()));
        savedData.addValueToSet(savedData.getLevelOfAddedViews(), levelNumber.getText().toString());*/

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SavedData.NAMES_OF_ADDED_VIEWS, jsonNames);
        editor.putString(SavedData.PROGRESS_OF_ADDED_VIEWS, jsonProgress);
        editor.putString(SavedData.MAX_PROGRESS_OF_ADDED_VIEWS, jsonMaxProgress);
        editor.putString(SavedData.LEVEL_OF_ADDED_VIEWS, jsonLevel);

        editor.putBoolean(SavedData.SOMETHING_SAVED, Boolean.TRUE);

        editor.apply();
    }


    //returns an onclick listener that when you click it goes to the activity you passed
    public View.OnClickListener openActivity(final Class activity, final String title, final String levelValue){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, activity);
                intent.putExtra("titleValue", title);
                intent.putExtra("levelValue", levelValue);
                intent.putExtra("maxProgressValue", maxProgress[findStringPosition(names, title)]);
                intent.putExtra("progressValue", Integer.parseInt(progress[findStringPosition(names, title)]));

                startActivity(intent);
            }
        };
    }

    public static int findStringPosition(String[] array, String searchString) { //finds the position in the array of a certain searchString
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(searchString)) {
                return i; // Return the position when the string is found
            }
        }

        return -1; // Return -1 if the string is not found in the array
    }

    String applySavedLevel(String savedValue){

        String[] parts = savedValue.split(" "); //this splits the string into parts[0] and parts[1]
        return "Lv " + parts[1];
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //ste due funzioni fanno funzionare l'animazione per le attivita che vengono create
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void firstRecursiveFuncion(final LinearLayout tl, final View viewToRemove, String activityName, int activityProgress, int activityMaxProgress, String activityLevel, final SharedPreferences sharedPreferences){
        tl.removeView(viewToRemove); //leva activitybtn

        //inflate add_activity_pbBtn
        pbBtn = getLayoutInflater().inflate(R.layout.add_activity_pbbtn, null, false);


        // prendi gli oggeti in add_activity_pbbtn come variabili
        actitivyNameView = (TextView) pbBtn.findViewById(R.id.activiy_name); //questo permetto di farlo cambiare all'interfaccia applyText
        pb = (ProgressBar) pbBtn.findViewById(R.id.pb);
        levelNumber = (TextView) pbBtn.findViewById(R.id.level_number);
        ImageButton addActivity_pbBtn = (ImageButton) pbBtn.findViewById(R.id.addActivity_pbBtn);

        //metti gli attributi giusti
        actitivyNameView.setText(activityName);
        pb.setMax(activityMaxProgress);
        pb.setProgress(activityProgress);
        levelNumber.setText(applySavedLevel(activityLevel));


        //definisci funzione del bottone
        addActivity_pbBtn.setOnClickListener(new View.OnClickListener() { //digli cosa fa il bottone di questa view
            @Override
            public void onClick(View v) {
                createDialog(2, firstButton, getLayoutInflater().inflate(R.layout.add_activity_dialog, null), sharedPreferences); //apri input utente //è indifferente che view gli metti
            }
        });

        //fai qualcosa se clicco sulla progress bar
        pb.setOnClickListener( openActivity(SkillActivity.class, activityName, activityLevel) );


        tl.addView(pbBtn); //metti add_activity_pbBtn
    }

    public void secondRecursiveFuncion(final LinearLayout tl, final View viewToRemove, String activityName, int activityProgress,  int activityMaxProgress, String activityLevel, final SharedPreferences sharedPreferences){
        tl.removeView(viewToRemove); //leva activity_pbBtn

        //inflate activity_2pb e sotto activity_btn
        View twoPb = getLayoutInflater().inflate(R.layout.add_activity2pb, null, false);
        btn = getLayoutInflater().inflate(R.layout.add_activitybtn, null, false);

        // prendi gli oggeti in add_activity_pbbtn come variabili
        pbl = (ProgressBar) twoPb.findViewById(R.id.pbl);
        pbr = (ProgressBar) twoPb.findViewById(R.id.pbr);
        levelNumberL = (TextView) twoPb.findViewById(R.id.level_number_l);
        levelNumberR = (TextView) twoPb.findViewById(R.id.level_number_r);
        ImageButton addActivity_Btn = (ImageButton) btn.findViewById(R.id.addActivity_btn);

        //definisci funzione del bottone
        addActivity_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog(1, btn, getLayoutInflater().inflate(R.layout.add_activity_dialog, null), sharedPreferences); //riparti da capo
            }

        });

        //rimetti a posto il nome dell'attività
        final String actitivyNameBackup = (String) actitivyNameView.getText();
        actitivyNameView = (TextView) twoPb.findViewById(R.id.activiy_name_l);
        actitivyNameView.setText(actitivyNameBackup);

        actitivyNameView = (TextView) twoPb.findViewById(R.id.activiy_name_r); // questo permetto di far cambiare quello di destra a actitivyNameView.setText
        actitivyNameView.setText(activityName);

        //rimetti i parametri della progress bar vecchia
        final int actitivyProgressBackup = (int) pb.getProgress();
        final int activityMaxProgressBackup = (int) pb.getMax();
        pbl.setMax(activityMaxProgressBackup);
        pbl.setProgress(actitivyProgressBackup);
        pbl.setOnClickListener( openActivity(SkillActivity.class, actitivyNameBackup, "Level " + getLevelInt( (String) levelNumber.getText()))); //qua metto due volte lo stesso activityLevel mi sa che è un bel prblema

        //fai qualcosa se clicco sulla progress bar
        pbr.setMax(activityMaxProgress);
        pbr.setProgress(activityProgress);
        pbr.setOnClickListener( openActivity(SkillActivity.class, activityName, activityLevel) ) ;      //qua metto due volte lo stesso activityLevel mi sa che è un bel prblema


        //rimetti il numero del livello
        levelNumberL.setText(levelNumber.getText());
        levelNumberR.setText(applySavedLevel(activityLevel));

        tl.addView(twoPb);
        tl.addView(btn);
    }


    AlertDialog dialog;

    //crea il dialogo per sapere che nuova attività si vuole creare
    public void createDialog(final int whichFunction, final View viewToRemove, View inflatedView, final SharedPreferences sharedPreferences){

        dialog = new AlertDialog.Builder(MainActivity.this)
                .setView(inflatedView)
                .setTitle("Add new activity")
                .setNegativeButton("cancel", null)
                .setPositiveButton("add", null) //è null ma lo definiamo dopo
                .create();


        final EditText selectActivityName = inflatedView.findViewById(R.id.select_activity_name); //EditText input utente

        //definisci positive button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick(View v) {

                        String activityName = selectActivityName.getText().toString(); //prendi il testo scritto dall'utente

                        if(activityName.length() > 0){
                            if(whichFunction == 1) {
                                firstRecursiveFuncion((LinearLayout) findViewById(R.id.linearLayout), viewToRemove, activityName.toUpperCase(), 0, 250, "Level 1", sharedPreferences); //fa funzionare l' "animazione" di aggiungere le attività
                                saveActivityData(sharedPreferences);
                            }else{
                                secondRecursiveFuncion((LinearLayout) findViewById(R.id.linearLayout), pbBtn, activityName.toUpperCase(), 0, 250, "Level 1", sharedPreferences); //fa funzionare l' "animazione" di aggiungere le attività
                                saveActivityData(sharedPreferences);
                            }
                            dialog.dismiss(); //esci dal dialogo solo se c'è effitamente un nome
                        }else{
                            selectActivityName.setHint("You must enter a name for your activity"); //dai un errore se non viene scelto nessun nome
                            selectActivityName.setHintTextColor(Color.RED);
                        }

                    }
                });
            }
        });

        dialog.show();
    }


    //crea la action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //cosa clicchi sull'action bar? ---> case
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                this.finish(); //system back button
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    //FORSE UN GIORNO MI SERVIRA (VECHHIA FUNZIONE ANIMAZIONE CREAZIONE ATTIVITA)
    /*public void addActivityRecursiveFunction(final LinearLayout tl, final View viewToRemove){

        tl.removeView(viewToRemove); //leva activitybtn

        //metti add_activity_pbBtn
        final View pbBtn = getLayoutInflater().inflate(R.layout.add_activity_pbbtn, null, false);
        ImageButton addActivity_pbBtn = (ImageButton) pbBtn.findViewById(R.id.addActivity_pbBtn);

        // prendi gli oggeti in add_activity_pbbtn come variabili
        actitivyNameView = (TextView) pbBtn.findViewById(R.id.activiy_name); //questo permetto di farlo cambiare all'interfaccia applyText
        pb = (ProgressBar) pbBtn.findViewById(R.id.pb);
        levelNumber = (TextView) pbBtn.findViewById(R.id.level_number);

        //definisci funzione del bottone
        addActivity_pbBtn.setOnClickListener(new View.OnClickListener() { //digli cosa fa il bottone di questa view
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {

                tl.removeView(pbBtn); //leva activity_pbBtn

                View twoPb = getLayoutInflater().inflate(R.layout.add_activity2pb, null, false); //metti activity_2pb

                final View btn = getLayoutInflater().inflate(R.layout.add_activitybtn, null, false); //mettici sotto activity_btn
                ImageButton addActivity_Btn = (ImageButton) btn.findViewById(R.id.addActivity_btn);
                addActivity_Btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addActivityRecursiveFunction(tl, btn); //riparti da capo
                    }

                });

                //rimetti a posto il nome dell'attività
                String actitivyNameBackup = (String) actitivyNameView.getText();
                actitivyNameView = (TextView) twoPb.findViewById(R.id.activiy_name_l);
                actitivyNameView.setText(actitivyNameBackup);

                actitivyNameView = (TextView) twoPb.findViewById(R.id.activiy_name_r); // questo permetto di far cambiare quello di destra all'interfaccia applyText

                //rimetti i parametri della progress bar
                ProgressBar pbl = (ProgressBar) twoPb.findViewById(R.id.pbl);
                pbl.setProgress(pb.getProgress());

                //rimetti il numero del livello
                TextView levelNumberL = (TextView) twoPb.findViewById(R.id.level_number_l);
                levelNumberL.setText(levelNumber.getText());

                tl.addView(twoPb);
                tl.addView(btn);

            }
        });



        //ProgressBar pbRight = (ProgressBar) activityView.findViewById(R.id.pbr); //progress bar nel layout add_activity2pb
        //ProgressBar pbLeft = (ProgressBar) activityView.findViewById(R.id.pbl);

        //così assegno una funzione alla progress bar (è la stessa della prima progress bar e va scritto del codice decente)
        //funziona solo su una delle due progress bar perchè hanno lo stesso id
                /*pbRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SkillActivity.class);
                        startActivity(intent);
                    }
                });
                /*pbLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SkillActivity.class);
                        startActivity(intent);
                    }
                });

        tl.addView(pbBtn);
    } */
}