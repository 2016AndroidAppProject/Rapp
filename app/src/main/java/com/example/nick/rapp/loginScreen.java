package com.example.nick.rapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class loginScreen extends AppCompatActivity {
    EditText userName;
    EditText password;
    TextView testText;
    Button loginButton;
    Intent loginIntent;

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
                    startActivity(loginIntent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

                    testText.setVisibility(View.VISIBLE);
                    testText.setBackgroundColor(Color.RED);
                    testText.setText("Incorrect Credentials");

                }
            }
        });
    }



/*    public void login(View view){
        Toast.makeText(getBaseContext(), "You have entered " + userName.getText().toString(), Toast.LENGTH_LONG) .show();
    }*/




}
