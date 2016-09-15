package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.HashMap;

public class selectionController extends AppCompatActivity {
    TextView practiceNotice;
    Intent prevIntent;
    ExpandableListView testView;
    Spinner studentSpinner;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> testAdapter;
    currentUserData userData;
    String selectedTeacher;
    String selectedStudent;
    String selectedTest;
    LinearLayout studentSelectionLayout;
    LinearLayout teacherSelectionLayout;
    LinearLayout testSelectionLayout;
    Spinner teacherSpinner;
    boolean userIsInteracting;
    boolean teacherSelected;
    boolean studentSelected;
    boolean testSelected;
    Button beginButton;
    Button restartButton;
    Button continueButton;

    boolean partialOrFullyComplete;
    boolean continuingTest;
    boolean completedTest;


    Spinner testSpinner;

    HashMap<String, Boolean> partiallyComplete;
    HashMap<String, Boolean> complete;

    int numStudents;
    int numTests;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    DatabaseOperations dop;
    Context ctx = this;
    Intent intent;

    public void beginTest(View view) {
        if (userData.getInstance().getUserType().equals("Administrator") & ((studentSelected == false) || (teacherSelected == false) || (testSelected == false))){
            Toast.makeText(getBaseContext(), "Please make a selection in every category to begin testing", Toast.LENGTH_LONG).show();
        } else if ((userData.getInstance().getUserType().equals("Teacher")) &  ((studentSelected == false) || (testSelected == false))){
            Toast.makeText(getBaseContext(), "Please make a selection in every category to begin testing", Toast.LENGTH_LONG).show();
        } else {
            userData.getInstance().setContinueTest(false);
            startActivity(intent);
        }
    }

    public void continueTest(View view) {
        if (userData.getInstance().getUserType().equals("Administrator") & ((studentSelected == false) || (teacherSelected == false) || (testSelected == false))){
            Toast.makeText(getBaseContext(), "Please make a selection in every category to begin testing", Toast.LENGTH_LONG).show();
        } else if ((userData.getInstance().getUserType().equals("Teacher")) &  ((studentSelected == false) || (testSelected == false))){
            Toast.makeText(getBaseContext(), "Please make a selection in every category to begin testing", Toast.LENGTH_LONG).show();
        } else {
            userData.getInstance().setContinueTest(true);
            startActivity(intent);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_screen);

        intent = new Intent(this, proceedController.class);
        teacherSelected = false;
        studentSelected = false;
        testSelected = false;

        beginButton = (Button) this.findViewById(R.id.beginButton);
        continueButton = (Button) this.findViewById(R.id.continueButton);
        restartButton = (Button) this.findViewById(R.id.restartButton);

        beginButton.setVisibility(View.INVISIBLE);
        continueButton.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.INVISIBLE);


        partialOrFullyComplete = false;
        continuingTest = false;

        partiallyComplete = new HashMap<String, Boolean>();
        complete = new HashMap<String, Boolean>();
        practiceNotice = (TextView) this.findViewById(R.id.practiceNotice);
        practiceNotice.setVisibility(View.INVISIBLE);
        if (userData.getInstance().isPracticeMode() == true){
            practiceNotice.setVisibility(View.VISIBLE);
        }



        dop = new DatabaseOperations(ctx);
        studentSelectionLayout = (LinearLayout) this.findViewById(R.id.studentSelection);
        teacherSelectionLayout = (LinearLayout) this.findViewById(R.id.teacherSelection);
        testSelectionLayout = (LinearLayout) this.findViewById(R.id.testSelection);

        studentSelectionLayout.setVisibility(View.VISIBLE);
        testSelectionLayout.setVisibility(View.VISIBLE);


        teacherSpinner = (Spinner) findViewById(R.id.teacherSpinner);
        studentSpinner = (Spinner) findViewById(R.id.studentSpinner);
        testSpinner = (Spinner) findViewById(R.id.testsSpinner);

        teacherSelectionLayout.setVisibility(View.INVISIBLE);
        studentSelectionLayout.setVisibility(View.INVISIBLE);
        testSelectionLayout.setVisibility(View.INVISIBLE);

        if (currentUserData.getInstance().getUserType().equals("Administrator")) {
            prevIntent = new Intent(this, adminViewController.class);
            teacherSelectionLayout.setVisibility(View.VISIBLE);
            //need to set the teacher spinner and make the other layouts invisible
            ArrayList<String> teacherNames = dop.getTeacherNames(dop);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, teacherNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teacherSpinner.setAdapter(adapter);


            teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> testNames;
                    ArrayAdapter<String> testAdapter;
                    //Need to, first off, prevent behavior from automatically being triggered
                    //when screen is created.
                    if (teacherSelected == true) {
                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                        selectedTeacher = (String) parent.getItemAtPosition(position);
                        userData.getInstance().setSelectedTeacher(selectedTeacher);

                        Cursor tc = dop.getTests(dop);
                        testNames = dop.getTestNamesForAdministrators(tc);
                        testAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, testNames);
                        testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        testSpinner.setAdapter(testAdapter);

                        testSelectionLayout.setVisibility(View.VISIBLE);
                    } else {
                        teacherSelected = true;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            prevIntent = new Intent(this, teacherViewController.class);
            ArrayList<String> testNames;

            ArrayAdapter<String> testAdapter;
            Cursor tc = dop.getTests(dop);
            testSelectionLayout.setVisibility(View.VISIBLE);
            testNames = dop.getTestNamesForTeachers(tc);
            testAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, testNames);
            testAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            testSpinner.setAdapter(testAdapter);




            selectedTeacher = userData.getInstance().getUserRealName();
        }

        testSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            ArrayList<String> studentNames;
            ArrayAdapter<String> studentAdapter;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Need to, first off, prevent behavior from automatically being triggered
                //when screen is created.
                if (testSelected == true) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    selectedTest = (String) parent.getItemAtPosition(position);
                    userData.getInstance().setSelectedTest(selectedTest);
                    studentSelectionLayout.setVisibility(View.VISIBLE);

                    studentNames = dop.getActiveStudentNames(dop, selectedTeacher);

                    for (int i = 1; i < studentNames.size(); i++){
                        Cursor mostRecentRecord = dop.getMostRecentCompletionRecord(dop, studentNames.get(i), selectedTest);

                        if ((mostRecentRecord == null) || (mostRecentRecord.getCount() == 0)) {
                            complete.put(studentNames.get(i), false);
                            partiallyComplete.put(studentNames.get(i), false);
                            studentNames.set(i, studentNames.get(i) + " has not started the test");
                        } else {
                            mostRecentRecord.moveToFirst();

                            int testComplete = mostRecentRecord.getInt(0);
                            int testTotal = mostRecentRecord.getInt(1);
                            if (mostRecentRecord.getInt(0) == mostRecentRecord.getInt(1)) {
                                complete.put(studentNames.get(i), true);
                                partiallyComplete.put(studentNames.get(i), false);
                                studentNames.set(i, studentNames.get(i) + "   completed all questions");

                            }

                            if (mostRecentRecord.getInt(0) < mostRecentRecord.getInt(1)) {
                                partiallyComplete.put(studentNames.get(i), true);
                                complete.put(studentNames.get(i), false);
                                studentNames.set(i, studentNames.get(i) + "   completed " + mostRecentRecord.getInt(0) + " out of " +
                                        mostRecentRecord.getInt(1) + " questions");
                            }
                        }
                    }


                    studentAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, studentNames);
                    studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    studentSpinner.setAdapter(studentAdapter);

                } else {
                    testSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Need to, first off, prevent behavior from automatically being triggered
                //when screen is created.
                if (studentSelected == true) {
                    continueButton.setVisibility(View.INVISIBLE);
                    restartButton.setVisibility(View.INVISIBLE);
                    beginButton.setVisibility(View.INVISIBLE);

                    String[] selectedString = ((String) parent.getItemAtPosition(position)).split(" ");
                    selectedStudent = selectedString[0];
                    userData.getInstance().setSelectedStudent(selectedStudent);
                    if (partiallyComplete.get(selectedStudent) == true) {
                        continueButton.setVisibility(View.VISIBLE);
                        restartButton.setVisibility(View.VISIBLE);
                    } else if (complete.get(selectedStudent) == true) {
                        restartButton.setVisibility(View.VISIBLE);
                    } else {
                        beginButton.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(getBaseContext(), selectedStudent, Toast.LENGTH_LONG).show();
                } else {
                    studentSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

                    //Then we need to load the test options into a test array list with a cursor.

                    //Then we need to make the test selection layout visible. Once the layout is visible
                    //it will remain so even if we make another selection.

                    //Then we will set the adapter for the test selection layout.

                    //Then we will set the onItemSelectedListener




     }

//
//
//                    Cursor tests = dop.getTests(dop);
//                    ArrayList<String> testNames = dop.getTestNamesForAdministrators(tests);
//                    ArrayAdapter<String> testAdapter = new ArrayAdapter<String>
//                            (ctx, android.R.layout.simple_spinner_item, testNames);
//
//
//                    if (teacherSelected == true) {
//                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
//                        selectedTeacher = (String) parent.getItemAtPosition(position);
//
//
//
//                    }
//
//                    if (testNames.size() == 0){
//                            Toast.makeText(getApplicationContext(), "There are currently no tests loaded", Toast.LENGTH_SHORT).show();
//
//                        } else {
//
//                            Cursor CR = dop.getStudentInfo(dop);
//                            numStudents = CR.getCount();
//
//                            if (numStudents == 0) {
//                                Toast.makeText(getApplicationContext(), "There are currently no students registered", Toast.LENGTH_SHORT).show();
//                            } else {
//
//                                ArrayList<String> studentNames = dop.getActiveStudentNames(dop, selectedTeacher);
//                                if ((studentNames.size() == 0) && (currentUserData.getInstance().getUserType().equals("Teacher"))) {
//                                    Toast.makeText(getApplicationContext(), "There are no students registered in your class. Please" +
//                                            " contact the administrators for assistance.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                                            (ctx, android.R.layout.simple_spinner_item, studentNames);
//
//
//                                    //        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//
//                                    //     adapter = ArrayAdapter.createFromResource(this, R.array.students, android.R.layout.simple_spinner_item);
//                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                                    studentSpinner.setAdapter(adapter);
//                                    studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                        @Override
//                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                            if (studentSelected == true) {
//                                                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) +
//                                                        " selected", Toast.LENGTH_LONG).show();
//                                            }
//                                            studentSelected = true;
//                                        }
//
//                                        @Override
//                                        public void onNothingSelected(AdapterView<?> parent) {
//
//                                        }
//                                    });
//                                }
//
//
//                        }
//                    }
//                        teacherSelected = true;
//                }
//
//
//

//            });

//
//        } else {
////            selectedTeacher = userData.getInstance().getUserRealName();
////            teacherSelectionLayout.setVisibility(View.INVISIBLE);
////
////
////
////
////            Cursor CR = dop.getStudentInfo(dop);
////            numStudents = CR.getCount();
////
////            if (numStudents == 0){
////                Toast.makeText(getApplicationContext(), "There are currently no students registered", Toast.LENGTH_SHORT).show();
////            } else {
////
////                ArrayList<String> studentNames = dop.getActiveStudentNames(dop, selectedTeacher);
////                if ((studentNames.size() == 0) && (currentUserData.getInstance().getUserType().equals("Teacher"))) {
////                    Toast.makeText(getApplicationContext(), "There are no students registered in your class. Please" +
////                            " contact the administrators for assistance.", Toast.LENGTH_SHORT).show();
////                } else {
////                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
////                            (this, android.R.layout.simple_spinner_item, studentNames);
////
////
////                    //        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////
////
////                    //     adapter = ArrayAdapter.createFromResource(this, R.array.students, android.R.layout.simple_spinner_item);
////                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
////                    studentSpinner.setAdapter(adapter);
////                    studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
////                        @Override
////                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
////                            if (studentSelected == true) {
////                                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) +
////                                        " selected", Toast.LENGTH_LONG).show();
////                            }
////                            studentSelected = true;
////                        }
////
////                        @Override
////                        public void onNothingSelected(AdapterView<?> parent) {
////
////                        }
////                    });
////                }
////            }
////
////
////
////        }
////        // ATTENTION: This was auto-generated to implement the App Indexing API.
////        // See https://g.co/AppIndexing/AndroidStudio for more information.
////        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//        }
//    }

    @Override
    public void onBackPressed() {
        startActivity(prevIntent);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "selectionController Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.nick.ProgressMonitoringTool/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "selectionController Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.example.nick.ProgressMonitoringTool/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
//    }
}