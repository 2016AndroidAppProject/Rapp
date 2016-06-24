package com.example.nick.rapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


//This class is part of the controller component of the model-view-controller design.

//This class is responsible for controlling the done screen that users see when they finish the test.


public class doneController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_screen);
    }
}
