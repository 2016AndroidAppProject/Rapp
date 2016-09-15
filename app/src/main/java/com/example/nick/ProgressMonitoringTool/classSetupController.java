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
    Button makeStudentInactiveButton;

    Context ctx = this;

    Spinner teacherSpinner;
    Spinner studentSpinner;
    ArrayAdapter<String> adapter;

    Boolean teacherSelected;
    Boolean studentSelected;






    String newStudentFirstName;
    int newStudentid;

    DatabaseOperations dop;




    EditText newStudentFirstNameField;

    String selectedTeacher;
    String selectedStudent;
    String selectedStudentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classsetup_screen);
        addNewStudentButton = (Button) findViewById(R.id.addNewStudentButton);
        makeStudentInactiveButton = (Button) findViewById(R.id.makeStudentInactiveButton);

        newStudentFirstNameField = (EditText) findViewById(R.id.addStudentFNameField);
        newStudentFirstNameField.setVisibility(View.GONE);

        teacherSelected = false;
        studentSelected = false;

        selectedTeacher = "";
        selectedStudent = "";

        studentSpinner = (Spinner) findViewById(R.id.studentSpinner);




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
                ArrayList<String> studentNames;
                ArrayAdapter<String> studentAdapter;


                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (teacherSelected == true) {
                        Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " teacherSelected", Toast.LENGTH_LONG).show();
                        selectedTeacher = (String) parent.getItemAtPosition(position);
                        newStudentFirstNameField.setVisibility(View.VISIBLE);

                        studentNames = dop.getAllStudentNames(dop, selectedTeacher);

                        for (int i = 0; i < studentNames.size(); i++){
                            if (studentNames.get(i).equals("")){

                            } else {
                                int studentID = dop.lookupStudentID(dop, selectedTeacher, studentNames.get(i));
                                String status = dop.getStudentStatus(dop, studentID);
                                studentNames.set(i, studentNames.get(i) + " " + status);
                            }
                        }
                        studentAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, studentNames);
                        studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        studentSpinner.setAdapter(studentAdapter);
                        studentSelected = false;
                    } else {
                        teacherSelected = true;
                    }



                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            studentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {



                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (studentSelected == true) {


                        String[] selectedString = ((String) parent.getItemAtPosition(position)).split(" ");
                        selectedStudent = selectedString[0];
                        Toast.makeText(getBaseContext(), selectedStudent + " selected", Toast.LENGTH_LONG).show();
                        selectedStudentStatus = selectedString[1];
                    } else {
                        studentSelected = true;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }


            addNewStudentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> studentNames;
                    ArrayAdapter<String> studentAdapter;

                    newStudentFirstName = newStudentFirstNameField.getText().toString();
                    Cursor CR = dop.getActiveStudentInfo(dop);
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
                                    studentNames = dop.getAllStudentNames(dop, selectedTeacher);

                                    for (int i = 0; i < studentNames.size(); i++){
                                        if (studentNames.get(i).equals("")){

                                        } else {
                                            int studentID = dop.lookupStudentID(dop, selectedTeacher, studentNames.get(i));
                                            String status = dop.getStudentStatus(dop, studentID);
                                            studentNames.set(i, studentNames.get(i) + " " + status);
                                        }
                                    }
                                    studentAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, studentNames);
                                    studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    studentSpinner.setAdapter(studentAdapter);
                                    studentSelected = false;
                                }
                            }
                        }

                    }
                }
             });

        makeStudentInactiveButton.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> studentNames;
            ArrayAdapter<String> studentAdapter;

            @Override
            public void onClick(View v) {
                if (selectedStudent.equals("")) {
                    Toast.makeText(getBaseContext(), "Please select a student to change their status", Toast.LENGTH_LONG).show();
                } else {
                    int studentID = dop.lookupStudentID(dop, selectedTeacher, selectedStudent);
                    if (selectedStudentStatus.equals("Active")) {
                        dop.updateStudentStatus(dop, studentID, "Inactive");
                        Toast.makeText(getBaseContext(), selectedStudent + " status changed to INACTIVE", Toast.LENGTH_LONG).show();
                    } else if (selectedStudentStatus.equals("Inactive")) {
                        dop.updateStudentStatus(dop, studentID, "Active");
                        Toast.makeText(getBaseContext(), selectedStudent + " status changed to ACTIVE", Toast.LENGTH_LONG).show();
                    }
                }
                if (dop.getActiveStudentNames(dop, selectedTeacher).size() == 0) {
                    Toast.makeText(getBaseContext(), "There are no students currently registered for this teacher", Toast.LENGTH_LONG).show();
                } else {
                    studentNames = dop.getAllStudentNames(dop, selectedTeacher);

                    for (int i = 0; i < studentNames.size(); i++){
                        if (studentNames.get(i).equals("")){

                        } else {
                            int studentID = dop.lookupStudentID(dop, selectedTeacher, studentNames.get(i));
                            String status = dop.getStudentStatus(dop, studentID);
                            studentNames.set(i, studentNames.get(i) + " " + status);
                        }
                    }
                    studentAdapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_item, studentNames);
                    studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    studentSpinner.setAdapter(studentAdapter);
                    studentSelected = false;

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
        Cursor CR = dop.getActiveStudentInfo(dop);

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
