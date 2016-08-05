package com.example.nick.rapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminViewController extends AppCompatActivity {
    Button userSetup;
    Button administerReceptiveTest;
    Button classSetup;


    Intent goToUserSetup;
    Intent goToStudentLive;
    Intent goToClassSetup;

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminview_screen);

        userSetup = (Button) findViewById(R.id.exportResultsButton);
        administerReceptiveTest = (Button) findViewById(R.id.administerReceptiveTest);
        classSetup = (Button) findViewById(R.id.settingsButton);

        goToUserSetup = new Intent(this, userSetupController.class);
        goToStudentLive = new Intent(this, selectionController.class);
        goToClassSetup = new Intent(this, classSetupController.class);

        userSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToUserSetup);
            }
        });

        administerReceptiveTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToStudentLive);
            }
        });

        classSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToClassSetup);
            }
        });


    }
}


