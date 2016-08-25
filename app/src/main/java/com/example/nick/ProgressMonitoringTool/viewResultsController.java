package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class viewResultsController extends AppCompatActivity {

    Spinner testSpinner;
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
    TextView modeNotice;
    TextView aggregatedScore;

    ListView resultsView;

    int recordID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewresults_screen);
        resultsView = (ListView) findViewById(R.id.resultsList);
        ctx = this;
        dop = new DatabaseOperations(ctx);

        testSpinner = (Spinner) findViewById(R.id.testsSpinner);


        tests = dop.getTests(dop);
        testNames = dop.getTestNamesForTeachers(tests);
        testAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, testNames);
        testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        testSpinner.setAdapter(testAdapter);
        testSelected = false;
        settings = dop.getResultMode(dop);
        settings.moveToFirst();
        resultsMode = settings.getString(0);

        aggregatedScore = (TextView) findViewById(R.id.aggregated);

        modeNotice = (TextView) findViewById(R.id.modeNotice);
        modeNotice.setText(resultsMode);


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
                    if (resultsMode.equals("disaggregated")) {
                        results = dop.getResults(dop, selectedTest);
                        if (results.getCount() == 0){
                            Toast.makeText(getBaseContext(), "There are no results available for this test", Toast.LENGTH_LONG).show();
                            resultsCursorAdapter resultsAdapter = new resultsCursorAdapter(ctx, results, 0);
                            resultsView.setAdapter(resultsAdapter);
                        } else {
                            results.moveToFirst();
                            resultsCursorAdapter resultsAdapter = new resultsCursorAdapter(ctx, results, 0);
                            resultsView.setAdapter(resultsAdapter);

                        }
                    } else if (resultsMode.equals("word")){
                        results = dop.getResultWords(dop, selectedTest);
                        if (results.getCount() == 0){
                            Toast.makeText(getBaseContext(), "There are no results available for this test", Toast.LENGTH_LONG).show();
                            resultsCursorAdapter resultsAdapter = new resultsCursorAdapter(ctx, results, 0);
                            resultsView.setAdapter(resultsAdapter);
                        } else {
                            results.moveToFirst();
                            resultsCursorAdapter resultsAdapter = new resultsCursorAdapter(ctx, results, 0);
                            resultsView.setAdapter(resultsAdapter);

                        }
                    } else if (resultsMode.equals("child")){
                        results = dop.getCompletionRecordsByStudent(dop, selectedTest);
                        if (results.getCount() == 0){
                            Toast.makeText(getBaseContext(), "There are no results available for this test", Toast.LENGTH_LONG).show();
                            resultsCursorAdapter resultsAdapter = new resultsCursorAdapter(ctx, results, 0);
                            resultsView.setAdapter(resultsAdapter);
                        } else {
                            results.moveToFirst();
                            resultsCursorAdapter resultsAdapter = new resultsCursorAdapter(ctx, results, 0);
                            resultsView.setAdapter(resultsAdapter);

                        }
                    } else if (resultsMode.equals("wordAndChild")){
                        aggregatedScore.setText(" Students answered correctly "
                                + String.valueOf(dop.percentageCorrectResults(dop, selectedTest)) + "% of the time.");
                    }





                } else {
                    testSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
