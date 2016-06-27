package com.example.nick.rapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class proceedController extends AppCompatActivity {
    Intent proceedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proceed_screen);

        proceedIntent = new Intent(this, instructionController.class);


    }

    public void proceed(View view) {
        startActivity(proceedIntent);
    }

}
