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
import java.util.ArrayList;

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
        testNames = dop.getTestNamesForTeachers(tests);
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
                    exportResults(results);
                }




            }
        });
    }


    public void exportResults(Cursor results){
        File dbFile=getDatabasePath("Rapp_info.db");
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists())
        {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, selectedTest + ".csv");
        try
        {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
            SQLiteDatabase db = dop.getReadableDatabase();
            csvWrite.writeNext(results.getColumnNames());
            while(results.moveToNext())
            {
                //Which column you want to exprort
                String arrStr[] ={results.getString(3), results.getString(2),results.getString(4),
                        results.getString(2)};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();
            results.close();
            Toast.makeText(getBaseContext(), "File " + selectedTest + ".csv exported to local directory", Toast.LENGTH_LONG).show();
        }
        catch(Exception sqlEx)
        {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
    }
}
