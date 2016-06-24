package com.example.nick.rapp;

import android.content.Intent;
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
    Intent loginIntent;
    currentUserData currentUserData;
    currentQuestionData currentQuestionData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //The class links up with the fields in the login_screen.
        userName = (EditText) findViewById(R.id.userNameField);
        password = (EditText)findViewById(R.id.passwordField);
        loginButton=(Button)findViewById(R.id.loginButton);

        //Login class prepares an intent to change activity to the practice screens.
        loginIntent = new Intent(this, testQuestionsController.class);


        //Following method class detects when user has clicked the login button and validates
        //their credentials. This placeholder will be replaced by a query of the databases.
        //If valid, users information will be loaded into userData class. Method also
        //provides feedback to user via toast messages.
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.getText().toString().equals("admin") &&

                        password.getText().toString().equals("admin")) {

                    Toast.makeText(getApplicationContext(), "Redirecting...",Toast.LENGTH_SHORT).show();
                    currentUserData.getInstance().setUserName(userName.getText().toString());
                    currentUserData.getInstance().setPassword(password.getText().toString());
                    currentUserData.getInstance().setUserType("Platypus!!");
                    startActivity(loginIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



/*    public void login(View view){
        Toast.makeText(getBaseContext(), "You have entered " + userName.getText().toString(), Toast.LENGTH_LONG) .show();
    }*/




}
