package com.example.nick.ProgressMonitoringTool;

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

    //This method is used to ensure that the user cannot press the back button while in a test
    @Override
    public void onBackPressed() {
    }

}
