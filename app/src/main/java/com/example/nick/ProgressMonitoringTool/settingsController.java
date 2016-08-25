package com.example.nick.ProgressMonitoringTool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class settingsController extends AppCompatActivity {
    Button resultsSetupButton;
    Button testSetupButton;
    Button userSetupButton;
    Button classSetupButton;

    Intent goToResultsSetup;
    Intent goToTestsSetup;
    Intent goToUserSetup;
    Intent goToClassSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_screen);

        resultsSetupButton = (Button) findViewById(R.id.resultsSetup);
        testSetupButton = (Button) findViewById(R.id.testSetup);
        userSetupButton = (Button) findViewById(R.id.addUsersSetup);
        classSetupButton = (Button) findViewById(R.id.classroomSetup);

        goToResultsSetup = new Intent(this, resultsSetupController.class);
        goToTestsSetup = new Intent(this, testSetupController.class);
        goToUserSetup = new Intent(this, userSetupController.class);
        goToClassSetup = new Intent(this, classSetupController.class);

        resultsSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToResultsSetup);
            }
        });

        testSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToTestsSetup);
            }
        });

        userSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToUserSetup);
            }
        });

        classSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToClassSetup);
            }
        });
    }
}
