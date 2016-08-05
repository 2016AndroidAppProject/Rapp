package com.example.nick.rapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

public class selectionController extends AppCompatActivity {
    ExpandableListView testView;
    Spinner studentSpinner;
    ArrayAdapter<String> adapter;
    currentUserData userData;
    String selectedTeacher;
    LinearLayout studentSelectionLayout;
    LinearLayout teacherSelectionLayout;
    Spinner teacherSpinner;
    boolean userIsInteracting;
    boolean teacherSelected;
    boolean studentSelected;

    int numStudents;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    DatabaseOperations dop;
    Context ctx = this;
    Intent intent;

    public void beginTest(View view) {
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_screen);

        intent = new Intent(this, proceedController.class);
        teacherSelected = false;
        studentSelected = false;

        //fetching the student names
        dop = new DatabaseOperations(ctx);
        studentSelectionLayout = (LinearLayout) this.findViewById(R.id.studentSelection);
        teacherSelectionLayout = (LinearLayout) this.findViewById(R.id.teacherSelection);
        teacherSpinner = (Spinner) findViewById(R.id.teacherSpinner);
        studentSpinner = (Spinner) findViewById(R.id.studentSpinner);

        if (currentUserData.getInstance().getUserType().equals("Administrator")){

            ArrayList<String> teacherNames = dop.getTeacherNames(dop);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, teacherNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teacherSpinner.setAdapter(adapter);



            teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (teacherSelected == true) {
                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                        selectedTeacher = (String) parent.getItemAtPosition(position);

                        Cursor CR = dop.getStudentInfo(dop);
                        numStudents = CR.getCount();

                        if (numStudents == 0) {
                            Toast.makeText(getApplicationContext(), "There are currently no students registered", Toast.LENGTH_SHORT).show();
                        } else {

                            ArrayList<String> studentNames = dop.getStudentNames(dop, selectedTeacher);
                            if ((studentNames.size() == 0) && (currentUserData.getInstance().getUserType().equals("Teacher"))) {
                                Toast.makeText(getApplicationContext(), "There are no students registered in your class. Please" +
                                        " contact the administrators for assistance.", Toast.LENGTH_SHORT).show();
                            } else {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (ctx, android.R.layout.simple_spinner_item, studentNames);


                                //        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                                //     adapter = ArrayAdapter.createFromResource(this, R.array.students, android.R.layout.simple_spinner_item);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                studentSpinner.setAdapter(adapter);
                                studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        if (studentSelected == true) {
                                            Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) +
                                                    " selected", Toast.LENGTH_LONG).show();
                                        }
                                        studentSelected = true;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }


                        }
                    }
                        teacherSelected = true;
                }



                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        } else {
            selectedTeacher = userData.getInstance().getUserRealName();
            teacherSelectionLayout.setVisibility(View.INVISIBLE);
            Cursor CR = dop.getStudentInfo(dop);
            numStudents = CR.getCount();

            if (numStudents == 0){
                Toast.makeText(getApplicationContext(), "There are currently no students registered", Toast.LENGTH_SHORT).show();
            } else {

                ArrayList<String> studentNames = dop.getStudentNames(dop, selectedTeacher);
                if ((studentNames.size() == 0) && (currentUserData.getInstance().getUserType().equals("Teacher"))) {
                    Toast.makeText(getApplicationContext(), "There are no students registered in your class. Please" +
                            " contact the administrators for assistance.", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (this, android.R.layout.simple_spinner_item, studentNames);


                    //        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                    //     adapter = ArrayAdapter.createFromResource(this, R.array.students, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    studentSpinner.setAdapter(adapter);
                    studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (studentSelected == true) {
                                Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) +
                                        " selected", Toast.LENGTH_LONG).show();
                            }
                            studentSelected = true;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }



        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "selectionController Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nick.rapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "selectionController Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nick.rapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
