package com.example.nick.rapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class instructionController extends AppCompatActivity {
    Intent proceedIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_screen);
        MediaPlayer mp = MediaPlayer.create(this, R.raw.initialinstructions);
        proceedIntent = new Intent(this, questionsController.class);

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                performOnEnd();
            }

        });
        mp.start();
    }


    public void performOnEnd(){
        startActivity(proceedIntent);
    }
}
