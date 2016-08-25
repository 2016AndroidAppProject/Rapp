package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class classSetupController extends AppCompatActivity {
    Button addNewStudentButton;

    Context ctx = this;

    Spinner teacherSpinner;
    ArrayAdapter<String> adapter;






    String newStudentFirstName;
    int newStudentid;

    DatabaseOperations dop;




    EditText newStudentFirstNameField;

    String selectedTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classsetup_screen);
        addNewStudentButton = (Button) findViewById(R.id.addNewStudentButton);

        newStudentFirstNameField = (EditText) findViewById(R.id.addStudentFNameField);
        newStudentFirstNameField.setVisibility(View.GONE);


        dop = new DatabaseOperations(ctx);

        Cursor CR = dop.getUserInfo(dop);
        int numTeachers = CR.getCount();

        if (numTeachers == 0){
            Toast.makeText(getApplicationContext(), "There are currently no teachers registered", Toast.LENGTH_SHORT).show();
        } else {

            teacherSpinner = (Spinner) findViewById(R.id.teacherSpinner);
            ArrayList<String> teacherNames = dop.getTeacherNames(dop);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (this, android.R.layout.simple_spinner_item, teacherNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            teacherSpinner.setAdapter(adapter);


            teacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " teacherSelected", Toast.LENGTH_LONG).show();
                    selectedTeacher = (String) parent.getItemAtPosition(position);
                    newStudentFirstNameField.setVisibility(View.VISIBLE);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


            addNewStudentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    newStudentFirstName = newStudentFirstNameField.getText().toString();
                    Cursor CR = dop.getStudentInfo(dop);
                    if (selectedTeacher.equals("")) {
                        Toast.makeText(getBaseContext(),
                                "Pleas select a teacher to add a student", Toast.LENGTH_LONG).show();

                    } else {
                        if (CR.getCount() == 0) {
                            dop.addNewStudent(dop, newStudentFirstName, generateRandomID(), selectedTeacher);
                            Toast.makeText(getBaseContext(),
                                    newStudentFirstName + " has been successfully added!", Toast.LENGTH_LONG).show();
                            newStudentFirstNameField.setText("");

                        } else {
                            boolean isUnique = isUniqueName(newStudentFirstName, CR);
                            if (isUnique == false) {
                                Toast.makeText(getBaseContext(), "There is already a student with that name in that teachers class." +
                                        " Please add initials to the name to make it unique.", Toast.LENGTH_LONG).show();
                                newStudentFirstNameField.setText("");
                            } else {

                                if (newStudentFirstName.equals("")) {
                                    Toast.makeText(getBaseContext(), "Please fill in the student name field", Toast.LENGTH_LONG).show();
                                } else {
                                    dop.addNewStudent(dop, newStudentFirstName, generateRandomID(), selectedTeacher);
                                    Toast.makeText(getBaseContext(),
                                            newStudentFirstName + " has been successfully added!", Toast.LENGTH_LONG).show();
                                    newStudentFirstNameField.setText("");
                                }
                            }
                        }

                    }
                }
             });

    }

    public boolean isUniqueName(String name, Cursor CR){
        boolean isUnique = true;
        CR.moveToFirst();
        do {
            String test = CR.getString(0);
            String teacher = CR.getString(2);
            String currentTeacher = selectedTeacher;
            if (name.equals(CR.getString(0)) && selectedTeacher.equals(CR.getString(2))){

                isUnique = false;
            }

        } while (CR.moveToNext());
        return isUnique;
    }

    public boolean isUniqueId(int newId, Cursor CR){
        do {
            if (newId == CR.getInt(1)) {
                return false;
            }
        }
        while (CR.moveToNext());
        return true;
    }

    public int generateRandomID(){
        int id;
        //  DatabaseOperations DOP = new DatabaseOperations(CTX);
        Cursor CR = dop.getStudentInfo(dop);

        int numStuds = CR.getCount();

        if (numStuds == 0){
            id = 1;
            return id;
        }
        CR.moveToFirst();

        Random rand = new Random();

        id = rand.nextInt((100000 - 0) + 1) + 0;
        while (isUniqueId(id, CR) == false) {
            id = rand.nextInt((100000 - 0) + 1) + 0;
        }
        return id;
    }
}
