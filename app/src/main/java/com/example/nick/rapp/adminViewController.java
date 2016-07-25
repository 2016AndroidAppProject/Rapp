package com.example.nick.rapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class adminViewController extends AppCompatActivity {
    Button addNewUserButton;

    EditText newUserNameField;
    EditText newPasswordField;
    EditText confirmPasswordField;

    String newUserName;
    String newPassword;
    String confirmPassword;

    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminview_screen);
        newUserNameField = (EditText) findViewById(R.id.newUserNameField);
        newPasswordField = (EditText) findViewById(R.id.newPasswordField);
        confirmPasswordField = (EditText) findViewById(R.id.confirmPasswordField);

        addNewUserButton = (Button)findViewById(R.id.addNewUserButton);

        addNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUserName = newUserNameField.getText().toString();
                newPassword = newPasswordField.getText().toString();
                confirmPassword = confirmPasswordField.getText().toString();

                if (!(newPassword.equals(confirmPassword))){
                    Toast.makeText(getBaseContext(),
                            "Passwords do not match, please re-enter your passwords", Toast.LENGTH_LONG).show();
                    newPasswordField.setText("");
                    confirmPasswordField.setText("");

                } else {
                    DatabaseOperations db = new DatabaseOperations(ctx);
                    db.addNewUser(db, newUserName, newPassword, "teacher");
                    Toast.makeText(getBaseContext(),
                            newUserName + " has been successfully registered!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}


