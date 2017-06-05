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
    boolean successfull;
    String strDate;

    String[][] resultsTable;

    Button exportButton;

    ListView resultsView;

    int recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_screen);

        successfull = false;

        ctx = this;
        dop = new DatabaseOperations(ctx);

        testSpinner = (Spinner) findViewById(R.id.exportSpinner);
        exportButton = (Button) findViewById(R.id.exportButton);


        tests = dop.getTests(dop);
        testNames = dop.getTestNamesForExport(dop);
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
                    results = dop.getResults(dop, selectedTest);
                    if (results.getCount() == 0){
                        Toast.makeText(getBaseContext(), "There are no results available for that test.", Toast.LENGTH_LONG).show();
                    } else {

                        exportResults(results);
                        if (successfull = true){
                            Toast.makeText(getBaseContext(), "File " + selectedTest + strDate + ".csv exported to local directory", Toast.LENGTH_LONG).show();
                            strDate = "";
                            successfull = false;
                        }
                    }
                }




            }
        });
    }


//    private String[] sortWordArray(String[] words){
//
//    }

    private HashMap<String, Integer> getWordIndexes(Cursor results){
        results.moveToFirst();
        int resultsCount = results.getCount();
        String[] words = new String[resultsCount];
        HashMap<String, Integer> wordIndexes = new HashMap<String, Integer>();

        int i = 0;
        do {
            words[i] = results.getString(2);
            i++;
        } while (results.moveToNext());

        //words = sortWordArray(words);

        for (i = 0; i < words.length; i++){
            wordIndexes.put(words[i], i);

        }


        return wordIndexes;
    }



    public void exportResults(Cursor results){
        //Before we can export results, we must populate our results table with the right values.
        //To have a list of string arrays that we can print.

        int resultCount = results.getCount();

        HashMap<String, Integer> wordIndexes = getWordIndexes(results);
        //Create table
        resultsTable = new String[(resultCount)][((resultCount) * 8) + 50];


        //Create a top row string array. It is as long as the results cursor is to ensure it
        //can accomodate enough words, even if every result is a new and unique word.


        results.moveToFirst();

        resultsTable[0][0] = "RECORD ID";
        resultsTable[0][1] = "STUDENT";
        resultsTable[0][2] = "DATE RECORDED";
        resultsTable[0][3] = "TEST GIVER";
        resultsTable[0][4] = "TEACHER";

        int numWords = 1; // corresponds to the number of words encountered
        //Only goes up by 1 when a new word is encountered.
        // If the new word is also a practice word, we will increment numWords by 4 instead of 1
        //starts at 1 to leave room for our header information.

        int numRecords = 0; //corresponds to the number of records encountered,
        // only goes up by 1 when a new recordID is encountered.

        int recordLocation = 0; //allows us to fetch the vertical location of a set of results for a student
        //if those results have already been added.

        int wordLocation = 0; //allows us to fetch the horizontal location of a word and its set of columns if
        //a result for that word was already processed.


        //A hashmap that keeps track of the words already encountered, and which column in the table the word for that result is.
        // (Note, it only stores one word for a practice word, even though multiple practice attempts on that same word may exist.
        // When a new word is encountered in a result, we add that word to the word hashmap. If that result is a practice item, we
        // will add space for four results on the table.
        HashMap<String, Integer> words = new HashMap<String, Integer>();
        //A hashmap that keeps track of the recordID's we have encountered.
        HashMap<Integer, Integer> recordIDs = new HashMap<Integer, Integer>();

        //A hashmap that stores any words that are practice words and the recordID together, and the number of times that
        //practice word has been attempted in that recordID. If the number of attempts is 1 but we are encountering that
        //word and recordID combo for the second time, we would increment numAttempts to 2 and so on. Then, we would get the horizontal
        //location of that word in the table from the word hashmap, and add (6 * num attempts)
        HashMap<String, Integer> numAttemptsIndex = new HashMap<String, Integer>();


        //Here we instantiate the variables which will make up the header information for each set of results
        int recordID = 0;
        int orderCompleted = 0;
        String date = "";

        //Here we instantiate the variables which will make up the location information for each specific result.
        String upperLeft = "";
        String upperRight = "";
        String lowerLeft = "";
        String lowerRight = "";

        do {


            //fetching the information from that particular result, and the connected recordID.
            recordID =  Integer.valueOf(results.getString(1));
            Cursor completionRecord = dop.getCompletionRecordByID(dop, recordID);
            if (completionRecord.getCount() == 1) {
                    completionRecord.moveToFirst();
                    date = completionRecord.getString(3);
                    String word = results.getString(2);
                    String studentName = results.getString(3);
                    int studentID = completionRecord.getInt(6);
                    String teacherName = dop.getTeacherNameByStudentID(studentID, dop, studentName);
                    String correct = "INCORRECT";
                    String testGiver = completionRecord.getString(5);
                    String type = results.getString(6);
                    int correctInt = results.getInt(4);
                    orderCompleted = results.getInt(7);
                    lowerRight = results.getString(8);
                    lowerLeft = results.getString(9);
                    upperLeft = results.getString(10);
                    upperRight = results.getString(11);
                    if (correctInt == 1) {
                        correct = "CORRECT";
                    }
                    String answer = results.getString(5);
                    //Check if that word is currently in the top row, if not, add it at the end.

                    //Code for adding a new header information and row when a different set of results is encountered.
                    if (recordIDs.get(recordID) == null) {
                        numRecords++;
                        recordIDs.put(recordID, numRecords);
                        resultsTable[numRecords][0] = String.valueOf(recordID);
                        resultsTable[numRecords][1] = studentName;
                        resultsTable[numRecords][2] = date;
                        resultsTable[numRecords][3] = testGiver;
                        resultsTable[numRecords][4] = teacherName;
                        recordLocation = numRecords;

                        //code for fetching the vertical location of the result set if we have already begun processing it.
                    } else {
                        recordLocation = recordIDs.get(recordID);

                    }

                    //Code for adding a new word if that word has not been encountered before.
                    if ((words.get(word) == null) && (words.get(word + "1") == null) && (words.get(word + "2") == null)
                            && (words.get(word + "3") == null) && (words.get(word + "4") == null)) {
                        //If it is not a practice word
                        if (!type.equalsIgnoreCase("Practice")) {
                            numWords++;
                            words.put(word, (numWords - 1) * 6);
                            if (words.get(word) == 179){
                                String heyhey = "HEYFUCKYOUMANG!";
                            }
                            resultsTable[0][words.get(word)] = word;
                            resultsTable[0][words.get(word) + 1] = "Order-completed-" + word;
                            resultsTable[0][words.get(word) + 2] = word + "-lower-left";
                            resultsTable[0][words.get(word) + 3] = word + "-lower-right";
                            resultsTable[0][words.get(word) + 4] = word + "-upper-left";
                            resultsTable[0][words.get(word) + 5] = word + "-upper-right";
                            wordLocation = words.get(word);

                        } else { //if word is a practice word
                            numWords++;
                            words.put(word + "1", (numWords - 1) * 6);
                            resultsTable[0][words.get(word + "1")] = word + "1";
                            resultsTable[0][words.get(word + "1") + 1] = "Order-completed-" + word;
                            resultsTable[0][words.get(word + "1") + 2] = word + "1" + "-lower-left";
                            resultsTable[0][words.get(word + "1") + 3] = word + "1" + "-lower-right";
                            resultsTable[0][words.get(word + "1") + 4] = word + "1" + "-upper-left";
                            resultsTable[0][words.get(word + "1") + 5] = word + "1" + "-upper-right";
                            numWords++;
                            words.put(word + "2", (numWords - 1) * 6);
                            resultsTable[0][words.get(word + "2")] = word + "2";
                            resultsTable[0][words.get(word + "2") + 1] = "Order-completed-" + word;
                            resultsTable[0][words.get(word + "2") + 2] = word + "2" + "-lower-left";
                            resultsTable[0][words.get(word + "2") + 3] = word + "2" + "-lower-right";
                            resultsTable[0][words.get(word + "2") + 4] = word + "2" + "-upper-left";
                            resultsTable[0][words.get(word + "2") + 5] = word + "2" + "-upper-right";
                            numWords++;
                            words.put(word + "3", (numWords - 1) * 6);
                            resultsTable[0][words.get(word + "3")] = word + "3";
                            resultsTable[0][words.get(word + "3") + 1] = "Order-completed-" + word;
                            resultsTable[0][words.get(word + "3") + 2] = word + "3" + "-lower-left";
                            resultsTable[0][words.get(word + "3") + 3] = word + "3" + "-lower-right";
                            resultsTable[0][words.get(word + "3") + 4] = word + "3" + "-upper-left";
                            resultsTable[0][words.get(word + "3") + 5] = word + "3" + "-upper-right";
                            numWords++;
                            words.put(word + "4", (numWords - 1) * 6);
                            resultsTable[0][words.get(word + "4")] = word + "4";
                            resultsTable[0][words.get(word + "4") + 1] = "Order-completed-" + word;
                            resultsTable[0][words.get(word + "4") + 2] = word + "4" + "-lower-left";
                            resultsTable[0][words.get(word + "4") + 3] = word + "4" + "-lower-right";
                            resultsTable[0][words.get(word + "4") + 4] = word + "4" + "-upper-left";
                            resultsTable[0][words.get(word + "4") + 5] = word + "4" + "-upper-right";
                            wordLocation = words.get(word + "1");
                            numAttemptsIndex.put(word + recordID, 1);
                        }
                    } else {
                        if (type.equalsIgnoreCase("Practice")) {
                            int numAttempts = 0;

                            if (numAttemptsIndex.get(word + recordID) == null) {
                                numAttemptsIndex.put(word + recordID, 0);
                            } else {
                                numAttempts = numAttemptsIndex.get(word + recordID);
                            }
                            switch (numAttempts) {
                                case 0:
                                    wordLocation = words.get(word + "1");
                                    numAttemptsIndex.put(word + recordID, 1);
                                    break;
                                case 1:
                                    wordLocation = words.get(word + "2");
                                    numAttemptsIndex.put(word + recordID, 2);
                                    break;

                                case 2:
                                    wordLocation = words.get(word + "3");
                                    numAttemptsIndex.put(word + recordID, 3);
                                    break;

                                case 3:
                                    wordLocation = words.get(word + "4");
                                    numAttemptsIndex.put(word + recordID, 3);
                                    break;
                            }
                        } else {
                            wordLocation = words.get(word);
                        }
                    }
                    resultsTable[recordLocation][wordLocation] = answer;
                    resultsTable[recordLocation][wordLocation + 1] = String.valueOf(orderCompleted);
                    resultsTable[recordLocation][wordLocation + 2] = lowerLeft;
                    resultsTable[recordLocation][wordLocation + 3] = lowerRight;
                    resultsTable[recordLocation][wordLocation + 4] = upperLeft;
                    resultsTable[recordLocation][wordLocation + 5] = upperRight;


            }
            completionRecord.close();
            } while (results.moveToNext());


        results.close();




        File dbFile=getDatabasePath("Rapp_info.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        strDate = sdf.format(new Date());

        strDate = strDate.replace(':', '-');
        strDate = strDate.replace(" ","");


        File file = new File(exportDir, selectedTest + strDate + ".csv");

        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dop.getReadableDatabase();
            for (int i = 0; i < resultsTable.length; i++){
                csvWrite.writeNext(resultsTable[i]);
            }
            successfull = true;
//            {
//                //Which column you want to exprort
//                String arrStr[] ={results.getString(3), results.getString(2),results.getString(4),
//                        results.getString(2)};
//                csvWrite.writeNext(arrStr);
//            }
            csvWrite.close();

        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
