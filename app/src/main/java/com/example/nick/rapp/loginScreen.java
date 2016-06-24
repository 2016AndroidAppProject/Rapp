package com.example.nick.rapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.Button;

public class loginScreen extends AppCompatActivity {
    EditText userName;
    EditText password;
    TextView testText;
    Button loginButton;
    Intent loginIntent;
    currentUserData currentUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_format);
        userName = (EditText) findViewById(R.id.userNameField);
        password = (EditText)findViewById(R.id.passwordField);
        testText = (TextView) findViewById(R.id.testText);
        loginButton=(Button)findViewById(R.id.loginButton);
        testText.setVisibility(View.GONE);
        loginIntent = new Intent(this, practiceItems.class);



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
