package com.example.nick.rapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class practiceItems extends AppCompatActivity {
    Intent proceedIntent;
    Button answerButton;
    TextView welcomeNotice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practiceitems_layout);
        proceedIntent = new Intent(this, doneScreen.class);
        welcomeNotice = (TextView) findViewById(R.id.welcomeNotice);
        welcomeNotice.setText(currentUserData.getInstance().getUserType());


    }


    public void answer(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.answer1:
                startActivity(proceedIntent);
                break;
            case R.id.answer2:
                Toast.makeText(getApplicationContext(), "Incorrect, please try again",Toast.LENGTH_SHORT).show();
                break;
            case R.id.answer3:
                Toast.makeText(getApplicationContext(), "Incorrect, please try again",Toast.LENGTH_SHORT).show();
                break;


        }
    }
}
