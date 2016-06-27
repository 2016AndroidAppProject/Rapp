package com.example.nick.rapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


//This class is part of the controller component of the model-view-controller design.

//This class is responsible for controlling the done screen that users see when they finish the test.


public class doneController extends AppCompatActivity {
    TextView testNotice;
    currentQuestionData question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_screen);




        testNotice = (TextView) findViewById(R.id.testNotice);
        testNotice.setText(question.getInstance().getCurrentQtype());
    }
}
