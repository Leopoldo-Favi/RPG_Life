package com.example.rpg_life;

import static com.example.rpg_life.MainActivity.addValueToArray;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.rpg_life.R.id;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

public class SkillActivity extends AppCompatActivity implements CallLoadSharedPreferences {

    ProgressTask progressTask1 = new ProgressTask("ciao", 50, 100);
    ProgressTask progressTask2 = new ProgressTask("caca", 50, 200);
    Task[] tasks = { progressTask1 , progressTask2 };

    TableLayout tl;

    static SavedData mainActivitySavedDataInstance;
    SharedPreferences mainActivitySharedPreferencesInstance;
    MainActivity mainActivityInstance;
    String[] skillNames;
    String[] level;
    Integer[] maxProgress;
    String[] progress;


    String titleValue;
    String levelValue;
    int maxProgressValue;
    int progressValue;

    private SavedActivityData savedActivityData;
    SharedPreferences sharedPreferences;
    private String instaceId;
    Boolean somethingSaved;
    //int[] int_currentBookProgresses;
    String[] bookNames = {};
    String[] bookPages = {}; //TRY TO IMPLEMENT THE PROGRESS TASK FIRST //DO I WANT A BOOK CLASS THAT EXTENDS ProgressTask??
    String[] bookProgress = {};

    static ProgressBar mainProgressBar;
    static TextView mainProgressBarText;
    static TextView levelText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        tl = findViewById(id.tableLayout_skill);

        //getIntent()
        titleValue = getIntent().getStringExtra("titleValue");
        levelValue = getIntent().getStringExtra("levelValue");
        maxProgressValue = getIntent().getIntExtra("maxProgressValue", 250);
        progressValue = getIntent().getIntExtra("progressValue", 0);

        //per salvare e cancellare
        mainActivityInstance = MainActivity.getMainActivityInstance();
        mainActivitySavedDataInstance = mainActivityInstance.getSavedDataInstance();
        mainActivitySharedPreferencesInstance = mainActivityInstance.getSharedPreferencesInstance();

        instaceId = "instance_" + titleValue; //ogni instanza di SkillActivity ha un instanceId
        savedActivityData = new SavedActivityData(getApplicationContext(), "MyPrefs_" + instaceId); //Qui apro l'instance di savedActivityData con nome MyPrefs_instanceId
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPrefs_" + instaceId, Context.MODE_PRIVATE); //apro lo stesso file di SharedPreferences
        loadSharedPreferences(sharedPreferences);

        skillNames = jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.NAMES_OF_ADDED_VIEWS, "[]"));
        level = jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.LEVEL_OF_ADDED_VIEWS, "[]"));
        maxProgress = new Gson().fromJson(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.MAX_PROGRESS_OF_ADDED_VIEWS, "[]"), new TypeToken<Integer[]>() {}.getType());
        progress = jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.PROGRESS_OF_ADDED_VIEWS, "[]"));

        //toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.ActionBar);
        setSupportActionBar(actionBar);

        //title textview
        TextView title = (TextView) findViewById(R.id.Title);
        title.setText(titleValue);

        /*
        //calculate activity progress by summing all book progresses
        int bookProgressSum = 0;
        for (String number : bookProgress) {
                int num = Integer.parseInt(number); // Convert string to integer
                bookProgressSum += num; // Add the integer to the sum
        }*/


        //level text
        levelText = (TextView) findViewById(id.LevelText);
        levelText.setText(levelValue);

        //progress bar
        mainProgressBar = (ProgressBar) findViewById(id.activity_pb);
        mainProgressBarText = (TextView) findViewById(id.activity_pb_text);
        correctlySetMainPbMax(maxProgressValue);
        correctlySetMainPbProgress(progressValue, SkillActivity.this);


        //button for adding tasks
        ImageButton addButton = findViewById(id.addBook_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog( getLayoutInflater().inflate(R.layout.add_book_dialog, null) );

            }
        });
    }


    //returns true if the mainProgressBar has reached its maximum
    public static boolean checkProgressBar(ProgressBar pb){

        int currentProgress = pb.getProgress();
        int max = pb.getMax();

        if(currentProgress >= max){  return true; }
        else{ return false; }
    }

    public static int getLevelInt(String textViewString){ //this is specific for the level textView in activity.skill_xml

        String[] parts = textViewString.split(" "); //this splits the string into parts[0] and parts[1]
        int lvl =  Integer.parseInt(parts[1]); //parts[1] should always be number
        return lvl;
    }

    public static void checkLevelCompleted(Context context){

        if(checkProgressBar(mainProgressBar)){

            Toast toast = Toast.makeText(context /* SkillActivty */, "Sei salito di livello!!!!", Toast.LENGTH_SHORT);
            toast.show();

            int lvl = getLevelInt( (String) levelText.getText() );
            lvl += 1;
            levelText.setText("Level " + lvl);

            mainProgressBar.setProgress(0); //return to 0 xp
            mainProgressBar.setMax(mainProgressBar.getMax() + (lvl-1)*20 ); //set new max for next level
            mainProgressBarText.setText( mainProgressBar.getProgress() + "/" + mainProgressBar.getMax());
        }
    }

    public static void correctlySetMainPbProgress(int progress, Context context){ //what if the prgress of that you put in is more than the max progress?

        if(progress < 0){ progress = 0;} //meglio non avere roba negativa
        mainProgressBar.setProgress(progress);
        mainProgressBarText.setText(progress + "/" + mainProgressBar.getMax());
        checkLevelCompleted(context); //also checks if you've reached the maximum in the progress bar
    }

    public static void correctlySetMainPbMax(int max){

        mainProgressBar.setMax(max);
        mainProgressBarText.setText(mainProgressBar.getProgress() + "/" + max);
    }


    public static <T> T[] removeElementAtIndex(T[] array, int index) {

        Class<?> arrayType = array.getClass().getComponentType();
        T[] newArray = (T[]) Array.newInstance(arrayType, array.length - 1);
        int newIndex = 0;

        for (int i = 0; i < array.length; i++) {
            if (i != index) {
                newArray[newIndex] = array[i];
                newIndex++;
            }
        }

        return newArray;
    }

    public static int findStringPosition(String[] array, String searchString) { //finds the position in the array of a certain book name
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(searchString)) {
                return i; // Return the position when the string is found
            }
        }

        return -1; // Return -1 if the string is not found in the array
    }


    public void loadSharedPreferences(SharedPreferences sharedPreferences) {

        //have you saved something?//have you saved something
        somethingSaved = sharedPreferences.getBoolean(SavedActivityData.SOMETHING_SAVED, Boolean.FALSE);

        //sharedPreferences.getString(SavedActivityData.CURRENT_BOOK_PROGRESSES, "[]");
        bookNames = jsonToStringArray(sharedPreferences.getString(SavedActivityData.NAMES_OF_ADDED_BOOKS, "[]"));
        bookPages = jsonToStringArray(sharedPreferences.getString(SavedActivityData.PAGES_OF_ADDED_BOOKS, "[]"));
        bookProgress = jsonToStringArray(sharedPreferences.getString(SavedActivityData.CURRENT_BOOK_PROGRESSES, "[]"));

        if(bookNames.length != 0){

            for(int c=0; c<bookNames.length; c++){ //for every book add a add_book view

                View bookView = getLayoutInflater().inflate(R.layout.add_book, null, false);
                tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
                TextView bookNameView = (TextView) bookView.findViewById(id.book_name);
                bookNameView.setText(bookNames[c]);  //change the name of the book

                ProgressBar progressBar = (ProgressBar) bookView.findViewById(id.progress);
                progressBar.setMax( Integer.parseInt(bookPages[c]) ); //cambia massimo della progress bar
                progressBar.setProgress(Integer.parseInt(bookProgress[c])); //metti il progresso giusto nella progress bar

                //fai in modo che sia cliccabile e succeda roba sennò che palle
                TableRow bookClickArea = (TableRow) bookView.findViewById(id.tableRow);
                bookClickArea.setOnClickListener(add_bookOnClickListener(bookNames[c], Integer.parseInt(bookPages[c]), (ProgressBar) bookView.findViewById(id.progress), bookView));
            }
        }
        //NUOVO SISTEMA TASK, LE TASK EFFETTIVAMENTE APPAIONO
        for(int i=0; i<tasks.length; i++){
            View bookView = getLayoutInflater().inflate(R.layout.add_book, null, false);
            tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
            TextView bookNameView = (TextView) bookView.findViewById(id.book_name);
            bookNameView.setText(tasks[i].name);  //change the name of the book

            ProgressBar progressBar = (ProgressBar) bookView.findViewById(id.progress);
            progressBar.setMax( tasks[i].getMaxProgress() ); //cambia massimo della progress bar
            progressBar.setProgress(50); //metti il progresso giusto nella progress bar

            //fai in modo che sia cliccabile e succeda roba sennò che palle
            TableRow bookClickArea = (TableRow) bookView.findViewById(id.tableRow);
            bookClickArea.setOnClickListener(add_bookOnClickListener(tasks[i].name, tasks[i].getMaxProgress() , (ProgressBar) bookView.findViewById(id.progress), bookView));
        }
    }

    //salva i vari dati che bisogna salvera per fare loadSharedPreferences
    public void saveBooksData(String title, String bookName, int nPages, SharedPreferences sharedPreferences){

        //aggiungi i nuovi valori
        bookNames = addValueToArray(bookNames, bookName);
        bookPages = addValueToArray( bookPages, String.valueOf(nPages));
        bookProgress = addValueToArray(bookProgress, "0"); //tutto parte da 0

        //trasformali in json
        String jsonBookNames = new Gson().toJson(bookNames);
        String jsonBookPages = new Gson().toJson(bookPages);
        String jsonBookProgress = new Gson().toJson(bookProgress);

        //edita i file di salvataggio
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SavedData.SOMETHING_SAVED, Boolean.TRUE); //hai salvato qualcosa = true

        editor.putString(SavedActivityData.NAMES_OF_ADDED_BOOKS, jsonBookNames);
        editor.putString(SavedActivityData.PAGES_OF_ADDED_BOOKS, jsonBookPages);
        editor.putString(SavedActivityData.CURRENT_BOOK_PROGRESSES, jsonBookProgress);

        editor.apply();
    }

    public static void saveCurrentBookProgresses(String jsonProgressesArray, SavedActivityData sad, SharedPreferences sharedPreferences){ //used to save the full array not just to add values one by one

        sad.setCurrentBookProgresses(jsonProgressesArray);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SavedActivityData.CURRENT_BOOK_PROGRESSES, sad.getCurrentBookProgresses());
        editor.apply();
    }



    public static String[] jsonToStringArray(String jsonArray){
        // Convert the JSON string back to an array using Gson
        Type type = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(jsonArray, type);
    }


    AlertDialog add_book_dialog;
    AlertDialog view_book_dialog;
    AlertDialog set_progress_dialog;

    //Dialogo per mettere un nuovo libro
    public void createDialog(View inflatedView) {  //Praticamente tutti gli onclick listener e dati che metto qua non vengono salvati

        //ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG
        add_book_dialog = new AlertDialog.Builder(SkillActivity.this)
                .setView(inflatedView)
                .setTitle("Add new book")
                .setNegativeButton("cancel", null)
                .setPositiveButton("add", null) //è null ma lo definiamo dopo
                .create();

        final EditText selectBookName = inflatedView.findViewById(R.id.select_book_name); //EditText input utente
        final EditText selectBookTotPages = inflatedView.findViewById(R.id.select_book_totPages); //EditText input utente

        //definisci positive button
        add_book_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) add_book_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String bookName = selectBookName.getText().toString(); //prendi il testo scritto dall'utente
                        String totPages_string = selectBookTotPages.getText().toString(); //non so se questo serve


                        if(bookName.length() > 0 && totPages_string.length() > 0) { //se l'utente ha messo tutto
                            final int totPages = Integer.parseInt(totPages_string);

                            View bookView = getLayoutInflater().inflate(R.layout.add_book, null, false);
                            tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
                            TextView bookNameView = (TextView) bookView.findViewById(id.book_name);
                            bookNameView.setText(bookName);  //change the name of the book
                            final ProgressBar progressBar = (ProgressBar) bookView.findViewById(id.progress);
                            progressBar.setMax(totPages); //set the number of pages in the progress bar


                            //VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG
                            //dai la possibilità di premere e fare roba
                            TableRow bookClickArea = (TableRow) bookView.findViewById(id.tableRow);
                            bookClickArea.setOnClickListener(add_bookOnClickListener(bookName, totPages, progressBar, bookView));

            //ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG//ADD_BOOK_DIALOG

                            //Per salvare
                            saveBooksData(titleValue, bookName, totPages, sharedPreferences);

                            add_book_dialog.dismiss(); //esci dal dialogo solo se c'è effitamente un nome

                        //ERRORI
                        } else if(bookName.length() > 0){
                            selectBookTotPages.setHint("You must enter the number of pages number here"); //dai un errore se non viene scelto un numero di pagine
                            selectBookTotPages.setHintTextColor(Color.RED);

                        } else if (totPages_string.length() > 0){
                            selectBookName.setHint("You must enter the name of your book"); //dai un errore se non viene scelto nessun nome
                            selectBookName.setHintTextColor(Color.RED);

                        } else { //dai entrambi gli errori
                            selectBookTotPages.setHint("You must enter the number of pages number here");
                            selectBookTotPages.setHintTextColor(Color.RED);

                            selectBookName.setHint("You must enter the name of your book");
                            selectBookName.setHintTextColor(Color.RED);
                        }
                    }
                });
            }
        });

        add_book_dialog.show();
    }


    //OnClickListener per add_book.xml
    public View.OnClickListener add_bookOnClickListener(final String bookName, final int totPages, final ProgressBar progressBar, View bookView){ //qua invece ne prendo esattamente 1 tutto strano

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //apri classe dialogFragment ViewBookDialog
                ViewBookDialog dialogFragment = ViewBookDialog.newInstance(bookName, totPages, bookNames, bookProgress, bookPages);
                dialogFragment.setReferences(sharedPreferences, savedActivityData, SkillActivity.this, progressBar, mainProgressBar, mainProgressBarText, levelText, tl, bookView);
                dialogFragment.show(getSupportFragmentManager(), "custom_dialog");

            }
        };
    }


    public void saveDataForMainActivity(){

        String jsonSkillNames = new Gson().toJson(skillNames);
        String jsonProgress = new Gson().toJson(progress); //convert to json
        String jsonLevel = new Gson().toJson(level);
        String jsonMaxProgress = new Gson().toJson(maxProgress);

        //resave the json array
        SharedPreferences.Editor editor = mainActivitySharedPreferencesInstance.edit();
        editor.putString(SavedData.NAMES_OF_ADDED_VIEWS, jsonSkillNames);
        editor.putString(SavedData.PROGRESS_OF_ADDED_VIEWS, jsonProgress);
        editor.putString(SavedData.LEVEL_OF_ADDED_VIEWS, jsonLevel);
        editor.putString(SavedData.MAX_PROGRESS_OF_ADDED_VIEWS, jsonMaxProgress);
        editor.apply();

    }

    public void goingBackToMainActivity(){
        // This code is executed before returning to MainActivity and is used to save changes
        //first you modify the arrays then call saveDataForMainActivity

        int totActivityProgress = mainProgressBar.getProgress(); //progress from the main progress bar
        int pbMax = mainProgressBar.getMax();

        int position = findStringPosition(jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.NAMES_OF_ADDED_VIEWS, "[]")), titleValue);

        //String[] level = jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.LEVEL_OF_ADDED_VIEWS, "[]"));
        level[position] = (String) levelText.getText();

        //int[] maxProgress = new Gson().fromJson(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.MAX_PROGRESS_OF_ADDED_VIEWS, "[]"), new TypeToken<int[]>() {}.getType());
        maxProgress[position] = pbMax;

        //set the right position of the mainActivity savedData array to the actual value (totActivityProgress)
        //String[] progress = jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.PROGRESS_OF_ADDED_VIEWS, "[]"));
        progress[position] = Integer.toString(totActivityProgress);

        saveDataForMainActivity();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBackPressed() {

        goingBackToMainActivity();
        super.onBackPressed();
    }


    //crea la action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //cosa clicchi sull'action bar? ---> case
    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:

                goingBackToMainActivity();
                this.finish(); // System back button
                return true;

            case R.id.delete: //cancella il file delle sharedPrefs e togli da tutti gli array di savedData la posizione corrispondente a questa skill
                File preferencesFile = new File(getFilesDir().getParent() + "/shared_prefs/", "MyPrefs_" + instaceId + ".xml");
                preferencesFile.delete();

                int position = findStringPosition(jsonToStringArray(mainActivitySharedPreferencesInstance.getString(mainActivitySavedDataInstance.NAMES_OF_ADDED_VIEWS, "[]")), titleValue);
                skillNames = removeElementAtIndex(skillNames, position);
                level = removeElementAtIndex(level, position);
                maxProgress = removeElementAtIndex(maxProgress, position);
                progress = removeElementAtIndex(progress, position);
                saveDataForMainActivity();

                this.finish();
                return true;


            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void callLoadSharedPreferences() {
        loadSharedPreferences(sharedPreferences);
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
/*
                                        //set onclicklistener sulla progressbar e la scritta per poter finalmente cambiare a che pagina del libro sei arrivato
                                        LinearLayout dialogProgressShower = (LinearLayout) viewBook.findViewById(id.dialog_progress_shower);
                                        dialogProgressShower.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                View setProgress = getLayoutInflater().inflate(R.layout.set_progress_dialog, null);

                                                final NumberPicker numberPicker = (NumberPicker) setProgress.findViewById(R.id.progressPicker); //number picker
                                                numberPicker.setMinValue(1);
                                                numberPicker.setMaxValue(totPages);

                                                set_progress_dialog = new AlertDialog.Builder(SkillActivity.this)
                                                        .setView(setProgress)
                                                        .setTitle("A che pagina sei arrivato?")
                                                        .setNegativeButton("cancel", null)
                                                        .setPositiveButton("ok", null) //è null ma lo definiamo qua sotto
                                                        .create();

                                                set_progress_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                                    @Override
                                                    public void onShow(DialogInterface dialog) { //positive button
                                                        Button b = ((AlertDialog) set_progress_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                                        b.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                int progress = numberPicker.getValue(); //prendi il valore scelto dall'utente
                                                                dialogProgressBar.setProgress(progress); //mettilo sulla progress bar lì e pure dentro la instance di skillActivity
                                                                progressBar.setProgress(progress);

                                                                set_progress_dialog.dismiss(); //esci dal dialogo
                                                            }
                                                        });
                                                    }
                                                });

                                                set_progress_dialog.show();
                                            }
                                        }); */


/*
    //VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG
    //dai la possibilità di premere e fare roba
    TableRow bookClickArea = (TableRow) bookView.findViewById(id.tableRow);
                            bookClickArea.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {

        View viewBook= getLayoutInflater().inflate(R.layout.view_book_dialog, null);
        TextView dialogTitle = (TextView) viewBook.findViewById(id.dialog_title);
        dialogTitle.setText(bookName);
final ProgressBar dialogProgressBar = (ProgressBar) viewBook.findViewById(id.dialog_progress_bar);
        dialogProgressBar.setMax(totPages); //set the number of pages in the progress bar


        //SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG//SET_PROGRESS_DIALOG
        LinearLayout progressShower = (LinearLayout) viewBook.findViewById(id.dialog_progress_shower);
        progressShower.setOnClickListener( numberPickerDialog( 1, totPages, "A che pagina sei arrivato?", dialogProgressBar, progressBar));
        //non è molto pulito che passi esattamente 2 progress bar e non quante me ne servono caso per caso però era più semplice


        //VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG

        view_book_dialog = new AlertDialog.Builder(SkillActivity.this)
        .setView( viewBook )
        .create();

        view_book_dialog.show();
        }
        });
*/