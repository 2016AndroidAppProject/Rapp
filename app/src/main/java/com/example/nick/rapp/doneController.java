package com.example.nick.rapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


//This class is part of the controller component of the model-view-controller design.

//This class is responsible for controlling the done screen that users see when they finish the test.


public class doneController extends AppCompatActivity {
    TextView testNotice;
    currentQuestionData question;
    MediaPlayer mp;
    Button returnButton;
    Intent intent;

    //This number is used to set the number of clicks required of the invisible
    //good job button for a user to return to the selection screen.
    int numClicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.done_screen);
        returnButton = (Button) this.findViewById(R.id.returnButton);
        intent = new Intent(this, selectionController.class);

        mp = MediaPlayer.create(this, R.raw.receptiveending);
        mp.start();

        numClicks = 0;
        returnButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                numClicks++;
                if (numClicks >= 3) {
                    startActivity(intent);
                }

            }
        });




        testNotice = (TextView) findViewById(R.id.testNotice);
        testNotice.setText(question.getInstance().getCurrentQtype());
    }



    //This method is used to ensure that the user cannot press the back button while in a test
    @Override
    public void onBackPressed() {
    }
}
