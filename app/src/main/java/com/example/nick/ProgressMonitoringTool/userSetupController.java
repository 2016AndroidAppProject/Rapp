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

public class userSetupController extends AppCompatActivity {
    Button addNewUserButton;
    Button editUserButton;
    Button deleteUserbutton;
    Button confirmDeleteUserButton;

    DatabaseOperations dop;

    EditText newUserNameField;
    EditText newPasswordField;
    EditText confirmPasswordField;
    EditText userLastNameField;

    String newUserName;
    String newPassword;
    String confirmPassword;
    String userLastName;

    EditText editUserNameField;
    EditText editPasswordField;
    EditText editConfirmPasswordField;
    EditText editUserLastNameField;

    String editUserName;
    String editPassword;
    String editConfirmPassword;
    String editUserLastName;
    int editID;

    String prevName;
    String prevPassword;
    String prevLastName;

    String editUserType;

    String selectedUser;

    String userToDelete;



    Spinner addUserTypeSpinner;
    Spinner editUserTypeSpinner;
    Spinner selectUserSpinner;
    Spinner deleteUserSpinner;

    String userType;

    Context ctx = this;

    ArrayAdapter<CharSequence> adapter;

    //These two booleans are used to check, first, if the user has actually selected a usertype for
    //adding a new user, and second, checking if the user is actually interacting with the ui
    //select a new user.
    boolean userTypeSelected;
    boolean userInteracting;

    boolean editUserTypeSelected;
    boolean editUserNameSelected;
    boolean userToDeleteSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.usersetup_screen);

        dop = new DatabaseOperations(ctx);


        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);
        newUserNameField = (EditText) findViewById(R.id.newUserNameField);
        newPasswordField = (EditText) findViewById(R.id.newPasswordField);
        addNewUserButton = (Button) findViewById(R.id.addNewUserButton);
        editUserButton = (Button) findViewById(R.id.editUserButton);
        deleteUserbutton = (Button) findViewById(R.id.deleteButton);
        confirmDeleteUserButton = (Button) findViewById(R.id.confirmDeleteButton);

        confirmDeleteUserButton.setVisibility(View.INVISIBLE);


        userLastNameField = (EditText) findViewById(R.id.userLastNameField);
        addUserTypeSpinner = (Spinner) findViewById(R.id.userTypeSpinner);
        selectUserSpinner = (Spinner) findViewById(R.id.editUserSpinner);
        editUserTypeSpinner = (Spinner) findViewById(R.id.editUserType);
        deleteUserSpinner = (Spinner) findViewById(R.id.deleteUserSpinner);


        editUserNameField = (EditText) findViewById(R.id.editUserNameField);
        editPasswordField = (EditText) findViewById(R.id.editNewPasswordField);
        editConfirmPasswordField = (EditText) findViewById(R.id.editConfirmPasswordField);
        editUserLastNameField = (EditText) findViewById(R.id.editNewNameField);


        userInteracting = false;

        editUserNameSelected = false;
        editUserTypeSelected = false;
        userTypeSelected = false;
        userToDeleteSelected = false;

        selectedUser = "";
        userToDelete = "";



        adapter= ArrayAdapter.createFromResource(this, R.array.userTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addUserTypeSpinner.setAdapter(adapter);
        addUserTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

//        ArrayList<String> teacherNames = dop.getTeacherNames(dop);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this, android.R.layout.simple_spinner_item, teacherNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        teacherSpinner.setAdapter(adapter);

        ArrayList<String> currentUsers = dop.getAllUserNames(dop);
        ArrayAdapter<String> userNamesAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, currentUsers);
        userNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectUserSpinner.setAdapter(userNamesAdapter);

        selectUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((editUserNameSelected == true) && (!parent.getItemAtPosition(position).equals(""))) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    selectedUser = (String) parent.getItemAtPosition(position);
                    editUserName = selectedUser;
                    Cursor selectedUserInfo = dop.getSpecificUserInfo(dop, editUserName);
                    selectedUserInfo.moveToFirst();
                    editUserLastNameField.setText(selectedUserInfo.getString(0));
                    editUserNameField.setText(editUserName);
                    prevLastName = selectedUserInfo.getString(0);
                    prevName = selectedUser;
                    prevPassword = selectedUserInfo.getString(2);
                    editUserType = selectedUserInfo.getString(3);
                    editID = selectedUserInfo.getInt(4);
                }
                editUserNameSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> currentUsers2 = dop.getAllUserNames(dop);
        ArrayAdapter<String> userNamesAdapter2 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, currentUsers);
        userNamesAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deleteUserSpinner.setAdapter(userNamesAdapter2);

        deleteUserSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((userToDeleteSelected == true) && (!parent.getItemAtPosition(position).equals(""))) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    userToDelete = parent.getItemAtPosition(position).toString();

                }
                userToDeleteSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> types = new ArrayList<String>();
        types.add("Administrator");
        types.add("Teacher");
        ArrayAdapter<String> userTypesAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, types);
        userTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editUserTypeSpinner.setAdapter(userTypesAdapter);

        editUserTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (editUserTypeSelected == true) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    editUserType = (String) parent.getItemAtPosition(position);
                    editUserTypeSelected = true;
                    confirmDeleteUserButton.setVisibility(View.INVISIBLE);


                }
                editUserTypeSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        deleteUserbutton.setOnClickListener(new View.OnClickListener() {



                                                @Override
                                                public void onClick(View v) {
                                                    if (userToDelete.equals("")){
                                                        Toast.makeText(getBaseContext(), "Please select a user you wish to delete", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(getBaseContext(), "Press the confirm button if you are sure you want to delete " + userToDelete, Toast.LENGTH_LONG).show();
                                                        confirmDeleteUserButton.setVisibility(View.VISIBLE);
                                                    }
                                                }
                                            });

        confirmDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> currentUsers;
            ArrayAdapter<String> userNamesAdapter;

            @Override
            public void onClick(View v) {
                Cursor userInfo = dop.getSpecificUserInfo(dop, userToDelete);
                userInfo.moveToFirst();
                int id = userInfo.getInt(4);
                dop.deleteUser(dop, id);
                Toast.makeText(getBaseContext(), userToDelete + " user deleted", Toast.LENGTH_LONG).show();
                confirmDeleteUserButton.setVisibility(View.INVISIBLE);
                userToDelete = "";
                currentUsers = dop.getAllUserNames(dop);
                userNamesAdapter = new ArrayAdapter<String> (ctx, android.R.layout.simple_spinner_item, currentUsers);
                userNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                selectUserSpinner.setAdapter(userNamesAdapter);

                ArrayList<String> currentUsers2 = dop.getAllUserNames(dop);
                ArrayAdapter<String> userNamesAdapter2 = new ArrayAdapter<String> (ctx, android.R.layout.simple_spinner_item, currentUsers);
                userNamesAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                deleteUserSpinner.setAdapter(userNamesAdapter2);
            }
        });

        editUserButton.setOnClickListener(new View.OnClickListener() {
//            EditText editUserNameField;
//            EditText editPasswordField;
//            EditText editConfirmPasswordField;
//            EditText editUserLastNameField;
//
//            String editUserName;
//            String editPassword;
//            String editConfirmPassword;
//            String editUserLastName;



            @Override
            public void onClick(View v) {
                editUserName = editUserNameField.getText().toString();
                editPassword = editPasswordField.getText().toString();
                editConfirmPassword = editConfirmPasswordField.getText().toString();
                editUserLastName = editUserLastNameField.getText().toString();
                if (selectedUser.equals("")) {
                    Toast.makeText(getBaseContext(), "Please select a user you wish to edit", Toast.LENGTH_LONG).show();
                } else {
                    if (editUserName.equals("")
                            || editUserLastName.equals("")) {
                        Toast.makeText(getBaseContext(), "Please complete all required fields",
                                Toast.LENGTH_LONG).show();
                    } else {

                        if (editPassword.equals("") && editConfirmPassword.equals("")){
                            editPassword = prevPassword;
                            editConfirmPassword = prevPassword;
                        }

                        if (!(editPassword.equals(editConfirmPassword))){
                            Toast.makeText(getBaseContext(),
                                    "New Passwords do not match, please re-enter your passwords", Toast.LENGTH_LONG).show();
                            editPasswordField.setText("");
                            editConfirmPasswordField.setText("");

                        } else if (!editUserName.equals(prevName) && (isUnique(dop, editUserName, 0) == false)) {
                            Toast.makeText(getBaseContext(),
                                    "The user name " + editUserName + " is already in use, please use a different user name.", Toast.LENGTH_LONG).show();
                            newUserNameField.setText("");
                        } else if (!editUserLastName.equals(prevLastName) && (isUnique(dop, editUserLastName, 3) == false)){
                            Toast.makeText(getBaseContext(),
                                    "A user with the last name " + editUserLastName + " has already been registered." +
                                            "Please add your first and/or middle initials to edit the users info " +
                                            "under a unique name", Toast.LENGTH_LONG).show();
                            userLastNameField.setText("");

                        } else if (editUserTypeSelected == false) {
                            Toast.makeText(getBaseContext(),
                                    "No user type is selected, please select a type to add a new user", Toast.LENGTH_LONG).show();

                        } else {
                            DatabaseOperations db = new DatabaseOperations(ctx);
                            db.updateUser(db, selectedUser, editUserName, editPassword, editUserType, editUserLastName, editID);
                            Toast.makeText(getBaseContext(),
                                    editUserName + " has been successfully updated!", Toast.LENGTH_LONG).show();
                            Cursor selectedUserInfo = dop.getSpecificUserInfo(dop, editUserName);
                            selectedUserInfo.moveToFirst();
                            editUserLastNameField.setText(selectedUserInfo.getString(0));
                            editUserNameField.setText(editUserName);
                        }
                    }


                }
            }



            });






        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> currentUsers;
            ArrayAdapter<String> userNamesAdapter;

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
                        int newID = generateRandomID();
                        DatabaseOperations db = new DatabaseOperations(ctx);
                        db.addNewUser(db, newUserName, newPassword, userType, userLastName, newID);
                        Toast.makeText(getBaseContext(),
                                newUserName + " has been successfully registered!", Toast.LENGTH_LONG).show();
                        currentUsers = dop.getAllUserNames(dop);
                        userNamesAdapter = new ArrayAdapter<String> (ctx, android.R.layout.simple_spinner_item, currentUsers);
                        userNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        selectUserSpinner.setAdapter(userNamesAdapter);

                        ArrayList<String> currentUsers2 = dop.getAllUserNames(dop);
                        ArrayAdapter<String> userNamesAdapter2 = new ArrayAdapter<String> (ctx, android.R.layout.simple_spinner_item, currentUsers);
                        userNamesAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        deleteUserSpinner.setAdapter(userNamesAdapter2);

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

    public boolean isUniqueId(int newId, Cursor CR){
        do {
            if (newId == CR.getInt(4)) {
                return false;
            }
        }
        while (CR.moveToNext());
        return true;
    }

    public int generateRandomID(){
        int id;
        //  DatabaseOperations DOP = new DatabaseOperations(CTX);
        Cursor CR = dop.getUserInfo(dop);

        int numTeachers = CR.getCount();

        if (numTeachers == 0){
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
