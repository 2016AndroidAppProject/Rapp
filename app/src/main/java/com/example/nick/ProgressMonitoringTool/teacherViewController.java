package com.example.nick.ProgressMonitoringTool;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class teacherViewController extends AppCompatActivity {

    Button goToSelectionButton;
    Button goToViewResultsButton;
    Button returnToLoginButton;

    Intent goToSelection;
    Intent goToViewResults;
    Intent returnToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_view_screen);

        goToSelectionButton = (Button) findViewById(R.id.selectionButton);
        goToViewResultsButton = (Button) findViewById(R.id.viewResultsButton);
        returnToLoginButton = (Button) findViewById(R.id.returnLogin);

        goToSelection = new Intent(this, selectionController.class);
        goToViewResults = new Intent(this, viewResultsController.class);
        returnToLogin = new Intent(this, loginController.class);

        DatabaseOperations dop = new DatabaseOperations(this);

        Cursor settings = dop.getResultMode(dop);
        settings.moveToFirst();
        String resultsMode = settings.getString(0);


        if (resultsMode.equalsIgnoreCase("noResults")) {
            goToViewResultsButton.setVisibility(View.INVISIBLE);
        }

        goToSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentUserData.getInstance().setPracticeMode(false);
                startActivity(goToSelection);
            }
        });

        goToViewResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToViewResults);
            }
        });

        returnToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(returnToLogin);
            }
        });
    }
}
