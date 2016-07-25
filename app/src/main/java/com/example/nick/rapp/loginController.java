package com.example.nick.rapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.Button;




//This class is part of the controller component of the model-view-controller design.


//The loginController class is the first activity of the application. It is responsible for managing the
//login layout, allowing the user to attempt to login in, and validate their credentials.
// Beacuse it is the first activity, it is also responsible for initializing the userData and
// questionData classes so that they are ready to hold data. The loginController.java will
//be responsible for sending the users credentials to the databases, and assuming they are valid, it
//will be responsible for fetching that users information and storing their information in the userData class.

public class loginController extends AppCompatActivity {
    EditText userName;
    EditText password;
    Button loginButton;
    Intent teacherLoginIntent;
    Intent adminLoginIntent;

    currentUserData currentUserData;
    currentQuestionData currentQuestionData;

    Context CTX = this;

    int status;

    DatabaseOperations DOP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //The class links up with the fields in the login_screen.
        userName = (EditText) findViewById(R.id.newUserNameField);
        password = (EditText) findViewById(R.id.newPasswordField);
        loginButton = (Button) findViewById(R.id.loginButton);

        //Login class prepares an intent to change activity to the practice screens.
        teacherLoginIntent = new Intent(this, selectionController.class);
        adminLoginIntent = new Intent(this, adminViewController.class);


        //Following method class detects when user has clicked the login button and validates
        //their credentials.

        //The method does this by fetching a cursor from the database that points to the top
        //of the credential columns (userName and password) and iterating through them.
        //If no match occurs, the entered credentials are invalid and a toast message
        //is displayed saying so.


        //If valid, users information will be loaded into userData class and the user will be
        //forwarded to the appropriate activity (admin or teacher login screens)


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 1;
                String enteredName = userName.getText().toString();
                String enteredPassword = password.getText().toString();

                //Placeholder for loggin in as administrator until we have a user type
                //column for our account info table.
                if ((enteredName.equals("admin") && (enteredPassword.equals("admin")))) {
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    currentUserData.getInstance().setUserName(enteredName);
                    currentUserData.getInstance().setPassword(enteredPassword);
                    currentUserData.getInstance().setUserType("admin");
                    startActivity(adminLoginIntent);
                } else {

                    DOP = new DatabaseOperations(CTX);
                    //  DatabaseOperations DOP = new DatabaseOperations(CTX);
                    Cursor CR = DOP.getUserCredentials(DOP);
                    CR.moveToFirst();

                    //will change to true if credentials are valid
                    boolean loginStatus = false;
                    do {
                        String NAME = "";
                        if (enteredName.equals(CR.getString(0)) &&
                                enteredPassword.equals(CR.getString(1))) {
                            loginStatus = true;
                            NAME = CR.getString(0);

                        }


                    }
                    while (CR.moveToNext());         //will return false (and end loop) if there is no next row

                    if (loginStatus == true) {


                        Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                        currentUserData.getInstance().setUserName(enteredName);
                        currentUserData.getInstance().setPassword(enteredPassword);
                        currentUserData.getInstance().setUserType("teacher");
                        startActivity(teacherLoginIntent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Wrong Credentials, please try again " +
                                "or contact testing administrators for help logging in.", Toast.LENGTH_SHORT).show();
                    }
                    //
                    //
                    //                if(userName.getText().toString().equals("admin") &&
                    //
                    //                        password.getText().toString().equals("admin")) {
                    //
                    //
                    //                } else if(userName.getText().toString().equals("teacher") &&
                    //
                    //                        password.getText().toString().equals("teacher")) {


                }
            }
        });
    }




/*    public void login(View view){
        Toast.makeText(getBaseContext(), "You have entered " + userName.getText().toString(), Toast.LENGTH_LONG) .show();
    }*/




}
