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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Type;

public class SkillActivity extends AppCompatActivity implements CallLoadSharedPreferences {

    /*
    ProgressTask progressTask1 = new ProgressTask("ciao", 50, 100);
    ProgressTask progressTask2 = new ProgressTask("caca", 50, 200);
    Task[] tasks = { progressTask1 , progressTask2 };
     */
    Task[] tasks = {};

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

    SavedActivityData savedActivityData;
    SharedPreferences sharedPreferences;
    private String instaceId;
    Boolean somethingSaved;
    //int[] int_currentBookProgresses;
    Gson Gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

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
                createDialog( getLayoutInflater().inflate(R.layout.add_task_dialog, null) );

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

    public static int findTaskPosByName(Task[] tasks, String taskName){
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].name.equals(taskName)) {
                return i; // Return the index when a match is found
            }
        }
        return -1; // Return -1 if no match is found
    }


    public void loadSharedPreferences(SharedPreferences sharedPreferences) {

        //have you saved something?//have you saved something
        somethingSaved = sharedPreferences.getBoolean(SavedActivityData.SOMETHING_SAVED, Boolean.FALSE);

        tasks = jsonToTaskArray(sharedPreferences.getString(SavedActivityData.TASKS, "[]"));

        //NUOVO SISTEMA TASK, LE TASK EFFETTIVAMENTE APPAIONO
        for(int i=0; i<tasks.length; i++){
            View bookView = getLayoutInflater().inflate(tasks[i].taskLayout, null, false);
            tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
            TextView bookNameView = (TextView) bookView.findViewById(id.task_name);
            bookNameView.setText(tasks[i].name);  //change the name of the book
            Log.d("dioporco", String.valueOf(tasks[i].getClass()));

            if(tasks[i] instanceof ProgressTask){
                ProgressBar progressBar = (ProgressBar) bookView.findViewById(id.progress);
                progressBar.setMax( tasks[i].getMaxProgress() ); //cambia massimo della progress bar
                progressBar.setProgress( tasks[i].getCurrentProgress() ); //metti il progresso giusto nella progress bar

                //fai in modo che sia cliccabile e succeda roba sennò che palle
                TableRow bookClickArea = (TableRow) bookView.findViewById(id.tableRow);
                bookClickArea.setOnClickListener(progress_taskOnClickListener(tasks[i].name, tasks[i].getMaxProgress(), tasks[i].rewardExp, (ProgressBar) bookView.findViewById(id.progress), bookView));
            }else if(tasks[i] instanceof CheckboxTask){
                Log.d("dioporco", tasks[i].name);
                CheckBox checkbox = (CheckBox) bookView.findViewById(id.checkBox);
                checkbox.setChecked(((CheckboxTask) tasks[i]).isChecked);
                checkbox.setOnCheckedChangeListener(checkbox_taskOnCheckedChangeListener((CheckboxTask) tasks[i]));
            }
        }
    }

    //salva i vari dati che bisogna salvera per fare loadSharedPreferences
    public void saveBooksData(/*String title, String bookName, int nPages, SharedPreferences sharedPreferences*/ Task newTask){

        //aggiungi la nuova task
        tasks = addValueToArray(tasks, newTask);
        //trasforma in json
        String jsonTasks = Gson.toJson(tasks);
        Log.d("dioporcoJson", jsonTasks);

        //edita i file di salvataggio
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SavedData.SOMETHING_SAVED, Boolean.TRUE); //hai salvato qualcosa = true
        editor.putString(SavedActivityData.TASKS, jsonTasks);
        editor.apply();
    }

    public static void saveCurrentTaskArray(String jsonTasks, SavedActivityData sad, SharedPreferences sharedPreferences){ //used to save the full array not just to add values one by one

        sad.setTasks(jsonTasks);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SavedActivityData.TASKS, sad.getTasks() );
        editor.apply();
    }



    public static String[] jsonToStringArray(String jsonArray){
        // Convert the JSON string back to an array using Gson
        Type type = new TypeToken<String[]>() {}.getType();
        return new Gson().fromJson(jsonArray, type);
    }

    public Task[] jsonToTaskArray(String jsonTasks) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter( Task[].class, new TaskArrayDeserializer() )
                .create();

        return gson.fromJson(jsonTasks, Task[].class);
    }

    //this class makes jsonToTaskArray work
    class TaskArrayDeserializer implements JsonDeserializer<Task[]> {
        @Override
        public Task[] deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            Task[] tasks = new Task[jsonArray.size()];

            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

                //common properties for all tasks
                String name = jsonObject.get("name").getAsString();
                int rewardExperience = jsonObject.get("rewardExp").getAsInt();

                if (jsonObject.has("maxProgress")) { //it is a ProgressTask
                    int maxProgress = jsonObject.get("maxProgress").getAsInt();
                    int currentProgress = jsonObject.get("currentProgress").getAsInt();
                    tasks[i] = new ProgressTask(SkillActivity.this, name, rewardExperience, maxProgress, currentProgress);
                } else if (jsonObject.has("isChecked")) { //this is if i needed other data (CheckboxTask data for ex), for now no
                    boolean isChecked = jsonObject.get("isChecked").getAsBoolean();
                    tasks[i] = new CheckboxTask(SkillActivity.this, name, rewardExperience, isChecked);
                } else { //its not a subclass
                    tasks[i] = new Task(SkillActivity.this, name, rewardExperience, R.layout.checkbox_task); //we put checkbox_task as layout just cuz i think it doesn't really matter
                }
            }

            return tasks;
        }
    }
    AlertDialog add_book_dialog;
    boolean isProgressTask; //we need this later in the definition of positive button
    //Dialogo per mettere una nuova task
    public void createDialog(View inflatedView) {

        //ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK
        add_book_dialog = new AlertDialog.Builder(SkillActivity.this)
                .setView(inflatedView)
                .setTitle("Add new book")
                .setNegativeButton("cancel", null)
                .setPositiveButton("add", null) //è null ma lo definiamo dopo
                .create();

        final Spinner taskTypeSelector = inflatedView.findViewById(id.spinner);
        final EditText taskNameSelector = inflatedView.findViewById(R.id.select_book_name); //EditText input utente (ex selectBookName)
        final EditText taskRewardExpSelector = inflatedView.findViewById(id.select_task_rewardExperience);
        final EditText taskMaxProgressSelector = inflatedView.findViewById(R.id.select_task_maxProgress); //EditText input utente (ex selectBookTotPages)

        taskTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { //Fai vedere taskMaxProgressSelector solo se l'utente vuole creare una ProgressTask
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Check which case is selected
                String selectedCase = parentView.getItemAtPosition(position).toString();

                // Check if the selected case is the one where taskMaxProgressSelector should be visible
                if ("Add a task with a progress bar".equals(selectedCase)) {
                    // Make the invisibleLayout visible
                    taskMaxProgressSelector.setVisibility(View.VISIBLE);
                    isProgressTask = true;
                } else {
                    // Hide the invisibleLayout for the other case
                    taskMaxProgressSelector.setVisibility(View.GONE);
                    isProgressTask = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle when nothing is selected, if needed
            }
        });

        //definisci positive button
        add_book_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) add_book_dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String taskName = taskNameSelector.getText().toString(); //prendi il testo scritto dall'utente
                        final int rewardExperience = Integer.parseInt(taskRewardExpSelector.getText().toString());

                        if(isProgressTask){

                            String newTaskMaxProgress_string = taskMaxProgressSelector.getText().toString(); //questo serve per non avere beghe se l'utente non lo mette

                            if(taskName.length() > 0 && newTaskMaxProgress_string.length() > 0) { //se l'utente ha messo tutto

                                int newTaskMaxProgress = Integer.parseInt(newTaskMaxProgress_string);

                                ProgressTask newTask = new ProgressTask(SkillActivity.this, taskName, rewardExperience, newTaskMaxProgress, 0);

                                View bookView = getLayoutInflater().inflate(R.layout.progress_task, null, false);
                                tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
                                TextView bookNameView = (TextView) bookView.findViewById(id.task_name);
                                bookNameView.setText(taskName);  //change the name of the book
                                final ProgressBar progressBar = (ProgressBar) bookView.findViewById(id.progress);
                                progressBar.setMax(newTaskMaxProgress); //set the number of pages in the progress bar


                                //VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG//VIEW_BOOK_DIALOG
                                //dai la possibilità di premere e fare roba
                                TableRow bookClickArea = (TableRow) bookView.findViewById(id.tableRow);
                                bookClickArea.setOnClickListener(progress_taskOnClickListener(taskName, newTaskMaxProgress, rewardExperience, progressBar, bookView));

                                //ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK//ADD_TASK

                                //Per salvare
                                saveBooksData(newTask);

                                add_book_dialog.dismiss(); //esci dal dialogo solo se c'è effitamente un nome

                                //ERRORI
                            } else if(taskName.length() > 0){
                                taskMaxProgressSelector.setHint("You must enter the number of pages number here"); //dai un errore se non viene scelto un numero di pagine
                                taskMaxProgressSelector.setHintTextColor(Color.RED);

                            } else if (newTaskMaxProgress_string.length() > 0){
                                taskNameSelector.setHint("You must enter the name of your book"); //dai un errore se non viene scelto nessun nome
                                taskNameSelector.setHintTextColor(Color.RED);

                            } else { //dai entrambi gli errori
                                taskMaxProgressSelector.setHint("You must enter the number of pages number here");
                                taskMaxProgressSelector.setHintTextColor(Color.RED);

                                taskNameSelector.setHint("You must enter the name of your book");
                                taskNameSelector.setHintTextColor(Color.RED);
                            }
                        }else if(!isProgressTask){

                            if(taskName.length() > 0) { //se l'utente ha messo tutto

                                View bookView = getLayoutInflater().inflate(R.layout.checkbox_task, null, false);
                                tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
                                CheckBox checkbox = (CheckBox) bookView.findViewById(id.checkBox);

                                CheckboxTask newTask = new CheckboxTask(SkillActivity.this, taskName, rewardExperience, false);

                                TextView bookNameView = (TextView) bookView.findViewById(id.task_name);
                                bookNameView.setText(taskName);  //change the name of the book
                                checkbox.setOnCheckedChangeListener(checkbox_taskOnCheckedChangeListener(newTask));

                                //Per salvare
                                saveBooksData(newTask);

                                add_book_dialog.dismiss(); //esci dal dialogo solo se c'è effitamente un nome

                                //ERRORI
                            } else {
                                taskNameSelector.setHint("You must enter the name of your task"); //dai un errore se non viene scelto nessun nome
                                taskNameSelector.setHintTextColor(Color.RED);
                            }
                        }
                    }
                });
            }
        });

        add_book_dialog.show();
    }

    public CompoundButton.OnCheckedChangeListener checkbox_taskOnCheckedChangeListener(CheckboxTask checkboxTask){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Log.d("dioporco", "funziono");
                    checkboxTask.isChecked = true;
                    saveCurrentTaskArray(Gson.toJson(tasks), savedActivityData, sharedPreferences); //cambia isChecked attribute e salva

                    correctlySetMainPbProgress(mainProgressBar.getProgress() + checkboxTask.rewardExp, SkillActivity.this); //add experience to mainProgressBar
                    Toast.makeText(SkillActivity.this, "Task completed!!", Toast.LENGTH_SHORT).show();
                } else {
                    checkboxTask.isChecked = false;
                    saveCurrentTaskArray(Gson.toJson(tasks), savedActivityData, sharedPreferences);

                    correctlySetMainPbProgress(mainProgressBar.getProgress() - checkboxTask.rewardExp, SkillActivity.this); //get rid of the same experience if the user unchecks the checkbox
                }
            }
        };
    }

    //OnClickListener per add_book.xml
    public View.OnClickListener progress_taskOnClickListener(final String taskName, final int maxProgress, final int rewardExp, final ProgressBar progressBar, View bookView){ //qua invece ne prendo esattamente 1 tutto strano

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //apri classe dialogFragment ViewBookDialog
                ViewBookDialog dialogFragment = ViewBookDialog.newInstance(taskName, maxProgress);
                dialogFragment.setReferences(sharedPreferences, savedActivityData, SkillActivity.this, progressBar, mainProgressBar, mainProgressBarText, levelText, tl, bookView, tasks, rewardExp);
                dialogFragment.show(getSupportFragmentManager(), "custom_dialog");

            }
        };
    }


    public void saveDataForMainActivity(){

        String jsonSkillNames = Gson.toJson(skillNames);
        String jsonProgress = Gson.toJson(progress); //convert to json
        String jsonLevel = Gson.toJson(level);
        String jsonMaxProgress = Gson.toJson(maxProgress);

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