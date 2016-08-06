package com.example.nick.rapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class testsController extends AppCompatActivity {

    Button editPracticeItemsButton;
    Button editTestsButton;
    Button createTestButton;

    Intent goToEditTests;
    Intent goToCreateTest;
    Intent goToEditPracticeItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tests_screen);

        editPracticeItemsButton = (Button) findViewById(R.id.editPrac);
        editTestsButton = (Button) findViewById(R.id.editTests);
        createTestButton = (Button) findViewById(R.id.createNewTest);

        goToEditTests = new Intent(this, editTestsController.class);
        goToEditPracticeItems = new Intent(this, editPracticeItemsController.class);
        goToCreateTest = new Intent(this, addTestController.class);

        editPracticeItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToEditPracticeItems);
            }
        });

        editTestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToEditTests);
            }
        });

        createTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToCreateTest);
            }
        });
    }
}
