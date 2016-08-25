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

public class userSetupController extends AppCompatActivity {
    Button addNewUserButton;

    EditText newUserNameField;
    EditText newPasswordField;
    EditText confirmPasswordField;
    EditText userLastNameField;

    String newUserName;
    String newPassword;
    String confirmPassword;
    String userLastName;

    Spinner userTypeSpinner;
    String userType;

    Context ctx = this;

    ArrayAdapter<CharSequence> adapter;

    //These two booleans are used to check, first, if the user has actually selected a usertype for
    //adding a new user, and second, checking if the user is actually interacting with the ui
    //select a new user.
    boolean userTypeSelected;
    boolean userInteracting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersetup_screen);



        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);
        newUserNameField = (EditText) findViewById(R.id.newUserNameField);
        newPasswordField = (EditText) findViewById(R.id.newPasswordField);
        addNewUserButton = (Button) findViewById(R.id.addNewUserButton);
        userLastNameField = (EditText) findViewById(R.id.userLastNameField);
        userTypeSpinner = (Spinner) findViewById(R.id.userTypeSpinner);

        userInteracting = false;

        userTypeSelected = false;

        adapter= ArrayAdapter.createFromResource(this, R.array.userTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (userInteracting == true) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    userType = (String) parent.getItemAtPosition(position);
                    userTypeSelected = true;

                }
                userInteracting = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUserName = newUserNameField.getText().toString();
                newPassword = newPasswordField.getText().toString();
                confirmPassword = confirmPasswordField.getText().toString();
                userLastName = userLastNameField.getText().toString();
                DatabaseOperations DOP = new DatabaseOperations(ctx);



                if (newUserName.equals("") || newPassword.equals("") || confirmPassword.equals("")
                    || userLastName.equals("")){
                    Toast.makeText(getBaseContext(), "Please complete all fields to register a new user",
                            Toast.LENGTH_LONG).show();
                } else {

                    if (!(newPassword.equals(confirmPassword))) {
                        Toast.makeText(getBaseContext(),
                                "Passwords do not match, please re-enter your passwords", Toast.LENGTH_LONG).show();
                        newPasswordField.setText("");
                        confirmPasswordField.setText("");

                    } else if (isUnique(DOP, newUserName, 0) == false) {
                        Toast.makeText(getBaseContext(),
                                "The user name " + newUserName + " is already in use, please use a different user name.", Toast.LENGTH_LONG).show();
                        newUserNameField.setText("");
                    } else if (isUnique(DOP, userLastName, 3) == false) {
                        Toast.makeText(getBaseContext(),
                                "A user with the last name " + userLastName + " has already been registered." +
                                        "Please add your first and/or middle initials to register " +
                                        "under a unique name", Toast.LENGTH_LONG).show();
                        userLastNameField.setText("");

                    } else if (userTypeSelected == false) {
                        Toast.makeText(getBaseContext(),
                                "No user type is selected, please select a type to add a new user", Toast.LENGTH_LONG).show();

                    } else {
                        DatabaseOperations db = new DatabaseOperations(ctx);
                        db.addNewUser(db, newUserName, newPassword, userType, userLastName);
                        Toast.makeText(getBaseContext(),
                                newUserName + " has been successfully registered!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }




    public boolean isUnique(DatabaseOperations DOP, String name, int columnNum){
        Cursor CR = DOP.getUserInfo(DOP);
        int numUsers = CR.getCount();
        if (numUsers == 0){
            return true;
        }
        CR.moveToFirst();
        do {

            if (name.equalsIgnoreCase(CR.getString(columnNum))){
                return false;
            }
        }
        while (CR.moveToNext());
        return true;
    }


}
