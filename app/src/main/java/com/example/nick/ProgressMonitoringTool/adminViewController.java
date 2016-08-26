package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminViewController extends AppCompatActivity {
    Button testsButton;
    Button administerReceptiveTestButton;
    Button settingsButton;
    Button studentLiveButton;
    Button exportResultsButton;
    Button openDatabaseManager;
    Button returnLoginButton;

    currentUserData currentUserData;



    Intent goToTests;
    Intent goToStudentLive;
    Intent goToAdministerReceptiveTest;
    Intent goToExportResults;
    Intent goToSettings;
    Intent databaseManager;
    Intent returnToLogin;

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminview_screen);

        testsButton = (Button) findViewById(R.id.tests);
        administerReceptiveTestButton = (Button) findViewById(R.id.administerReceptiveTest);
        settingsButton = (Button) findViewById(R.id.settingsButton);
        studentLiveButton = (Button) findViewById(R.id.studentLiveView);
        exportResultsButton = (Button) findViewById(R.id.exportResultsButton);
        openDatabaseManager = (Button) findViewById(R.id.checkDatabase);
        returnLoginButton = (Button) findViewById(R.id.returnLogin);

        goToTests = new Intent(this, testsController.class);
        goToStudentLive = new Intent(this, selectionController.class);
        goToSettings = new Intent(this, settingsController.class);
        goToAdministerReceptiveTest = new Intent(this, selectionController.class);
        goToExportResults = new Intent(this, exportResultsController.class);
        databaseManager = new Intent(this, AndroidDatabaseManager.class);
        returnToLogin = new Intent(this, loginController.class);

        testsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToTests);
            }
        });

        administerReceptiveTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserData.getInstance().setPracticeMode(false);
                startActivity(goToAdministerReceptiveTest);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToSettings);
            }
        });

        studentLiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserData.getInstance().setPracticeMode(true);
                startActivity(goToStudentLive);
            }
        });

        exportResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToExportResults);
            }
        });

        openDatabaseManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(databaseManager);
            }
        });

        returnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(returnToLogin);
            }
        });


    }
}


