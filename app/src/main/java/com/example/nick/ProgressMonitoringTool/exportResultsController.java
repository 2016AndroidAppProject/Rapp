package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class exportResultsController extends AppCompatActivity {
    ArrayList<String> testNames;
    ArrayAdapter<String> testAdapter;
    String selectedTestName;
    DatabaseOperations dop;
    Context ctx;
    boolean testSelected;
    String selectedTest;
    String resultsMode;
    Cursor settings;
    Cursor tests;
    Cursor results;
    Spinner testSpinner;

    String[][] resultsTable;

    Button exportButton;

    ListView resultsView;

    int recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_screen);

        ctx = this;
        dop = new DatabaseOperations(ctx);

        testSpinner = (Spinner) findViewById(R.id.exportSpinner);
        exportButton = (Button) findViewById(R.id.exportButton);


        tests = dop.getTests(dop);
        testNames = dop.getTestNamesForAdministrators(tests);
        testAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, testNames);
        testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testSpinner.setAdapter(testAdapter);
        testSelected = false;
        settings = dop.getResultMode(dop);
        settings.moveToFirst();
        resultsMode = settings.getString(0);

        selectedTest = "";



        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> testNames;
                ArrayAdapter<String> testAdapter;
                //Need to, first off, prevent behavior from automatically being triggered
                //when screen is created.
                if (testSelected == true) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    selectedTest = (String) parent.getItemAtPosition(position);
                    results = dop.getResults(dop, selectedTest);
                } else {
                    testSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int test = 0;


                if (selectedTest.equals("")){
                    Toast.makeText(getBaseContext(), "Please select a test to export results.", Toast.LENGTH_LONG).show();
                } else {
                    if (results.getCount() == 0){
                        Toast.makeText(getBaseContext(), "There are no results available for that test.", Toast.LENGTH_LONG).show();
                    } else {
                        exportResults(results);
                    }
                }




            }
        });
    }



    public void exportResults(Cursor results){
        //Before we can export results, we must populate our results table with the right values.
        //To have a list of string arrays that we can print.

        //Create table
        resultsTable = new String[results.getCount() + 4][results.getCount() + 4];


        //Create a top row string array. It is as long as the results cursor is to ensure it
        //can accomodate enough words, even if every result is a new and unique word.
        String[] topRow = new String[results.getCount() + 4];

        results.moveToFirst();

        topRow[0] = "STUDENT";
        topRow[1] = "DATE RECORDED";
        topRow[2] = "TEST GIVER";
        resultsTable[0] = topRow;


        //How many words and students have so far been calculated.
        int numWords = 2;
        int numRecords = 0;

        //Number of practice item tries, so we can report all practice item attempts
        int numAttempts = 1;
        HashMap<String, Integer> words = new HashMap<String, Integer>();
        HashMap<String, Integer> students = new HashMap<String, Integer>();
        HashMap<String, Integer> dates = new HashMap<String, Integer>();



        int recordID = 0;
        String date = "";

        do {



            recordID =  Integer.valueOf(results.getString(1));
            Cursor completionRecord = dop.getCompletionRecordByID(dop, recordID);
            completionRecord.moveToFirst();
            date = completionRecord.getString(3);
            String word = results.getString(2);
            String studentName = results.getString(3);
            String correct = "INCORRECT";
            String testGiver = completionRecord.getString(5);
            String type = results.getString(6);
            int correctInt = results.getInt(4);
            if (correctInt == 1){
                correct = "CORRECT";
            }
            String answer = results.getString(5);
            //Check if that word is currently in the top row, if not, add it at the end.





            if (words.get(word) == null){
                numAttempts = 1;
                numWords++;
                words.put(word, numWords);
                resultsTable[0][numWords] = word;
            } else if (type.equalsIgnoreCase("Practice")){
                numWords++;
                numAttempts++;
                words.put(word + String.valueOf(numAttempts), numWords);
                resultsTable[0][numWords] = word + String.valueOf(numAttempts);
            }

            if (students.get(studentName) == null){
                numRecords++;
                students.put(studentName, numRecords);
                dates.put(date, numRecords);
                resultsTable[numRecords][0] = studentName;
                resultsTable[numRecords][1] = date;
                resultsTable[numRecords][2] = testGiver;
            }

            if (dates.get(date) == null){
                numRecords++;
                dates.put(date, numRecords);
                students.put(studentName, numRecords);
                resultsTable[numRecords][0] = studentName;
                resultsTable[numRecords][1] = date;
                resultsTable[numRecords][2] = testGiver;
            }
            if ((type.equalsIgnoreCase("Practice") && (numAttempts > 1))){
                    resultsTable[dates.get(date)][words.get(word + String.valueOf(numAttempts))] = answer;
            } else {
                resultsTable[dates.get(date)][words.get(word)] = answer;
            }
        } while (results.moveToNext());

        results.close();




        File dbFile=getDatabasePath("Rapp_info.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());


        File file = new File(exportDir, selectedTest + strDate + ".csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dop.getReadableDatabase();
            for (int i = 0; i < resultsTable.length; i++){
                csvWrite.writeNext(resultsTable[i]);
            }
//            {
//                //Which column you want to exprort
//                String arrStr[] ={results.getString(3), results.getString(2),results.getString(4),
//                        results.getString(2)};
//                csvWrite.writeNext(arrStr);
//            }
            csvWrite.close();
            Toast.makeText(getBaseContext(), "File " + selectedTest + strDate + ".csv exported to local directory", Toast.LENGTH_LONG).show();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
