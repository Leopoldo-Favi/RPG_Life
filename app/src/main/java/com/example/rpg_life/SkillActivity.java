package com.example.rpg_life;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.rpg_life.R.id;

public class SkillActivity extends AppCompatActivity {

    TableLayout tl;

    MainActivity mainActivity = new MainActivity();
    SavedData savedData = mainActivity.savedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skill);

        //per salvare
        final SharedPreferences sharedPreferences = AppPreferences.getInstance(this).getSharedPreferences();
        loadSharedPreferences(sharedPreferences);

        //getIntent()
        String titleValue = getIntent().getStringExtra("titleValue");
        int progressValue = getIntent().getIntExtra("progressValue", 0);

        //toolbar
        Toolbar actionBar = (Toolbar) findViewById(R.id.ActionBar);
        setSupportActionBar(actionBar);

        //title textview and progress bar
        TextView title = (TextView) findViewById(R.id.Title);
        title.setText(titleValue);
        ProgressBar progressBar = (ProgressBar) findViewById(id.activity_pb);
        progressBar.setProgress(progressValue);


         tl = findViewById(id.tableLayout_skill);


        ImageButton addButton = findViewById(id.addBook_btn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog( getLayoutInflater().inflate(R.layout.add_book_dialog, null) );

            }
        });
    }


    private void loadSharedPreferences(SharedPreferences sharedPreferences) {
        String[] names = mainActivity.correctlyConvertStringSets(sharedPreferences, SavedData.NAMES_OF_ADDED_VIEWS, savedData.getNamesOfAddedViews());

        for(int c=0; c<names.length; c++){
            Log.d("dioporco", names[c] + "zoozo");
        }
    }

    public void saveBooksData(String title, String bookName, SharedPreferences sharedPreferences){ //per ora mi sto preoccupando solo di tenere li il nome poi progress bar e data ci si pensa

        savedData.addValueToSet(savedData.getNamesOfAddedViews(), "[" + title + "]" + bookName);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(SavedData.NAMES_OF_ADDED_VIEWS, savedData.getNamesOfAddedViews());
        editor.apply();

    }

    AlertDialog dialog;

    public void createDialog(View inflatedView) {
        dialog = new AlertDialog.Builder(SkillActivity.this)
                .setView(inflatedView)
                .setTitle("Add new book")
                .setNegativeButton("cancel", null)
                .setPositiveButton("add", null) //è null ma lo definiamo dopo
                .create();

        final EditText selectBookName = inflatedView.findViewById(R.id.select_book_name); //EditText input utente
        final EditText selectBookTotPages = inflatedView.findViewById(R.id.select_book_totPages); //EditText input utente

        //definisci positive button
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String bookName = selectBookName.getText().toString(); //prendi il testo scritto dall'utente
                        String totPages_string = selectBookTotPages.getText().toString();


                        if(bookName.length() > 0 && totPages_string.length() > 0) { //se l'utente ha messo tutto
                            int totPages = Integer.parseInt(totPages_string);

                            View bookView = getLayoutInflater().inflate(R.layout.add_book, null, false);
                            tl.addView(bookView, tl.getChildCount() - 1); //add the view but not at the top of the page
                            TextView bookNameView = (TextView) bookView.findViewById(id.book_name);
                            bookNameView.setText(bookName);  //change the name of the book

                            //ADESSO MANCA SOLO DI FARE LA PROGRESS BAR IN BASE A QUANTE PAGINE HA IL LIBRO

                            dialog.dismiss(); //esci dal dialogo solo se c'è effitamente un nome

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
}
