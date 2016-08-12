package com.example.nick.rapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


//This class is part of the controller component of the model-view-controller design.


//The loginController class is the first activity of the application. It is responsible for managing the
//login layout, allowing the user to attempt to login in, and validate their credentials.
// Beacuse it is the first activity, it is also responsible for initializing the userData and
// questionData classes so that they are ready to hold data. The loginController.java will
//be responsible for sending the users credentials to the databases, and assuming they are valid, it
//will be responsible for fetching that users information and storing their information in the userData class.

public class loginController extends AppCompatActivity {
    EditText userName;
    EditText password;
    Button loginButton;
    String userType;
    String userRealName;
    Intent teacherLoginIntent;
    Intent adminLoginIntent;

    currentUserData currentUserData;
    currentQuestionData currentQuestionData;

    Context CTX = this;

    int status;

    DatabaseOperations DOP;
    byte[] audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        //The class links up with the fields in the login_screen.
        userName = (EditText) findViewById(R.id.newUserNameField);
        password = (EditText) findViewById(R.id.newPasswordField);

        loginButton = (Button) findViewById(R.id.loginButton);

        //Login class prepares an intent to change activity to the practice screens.
        teacherLoginIntent = new Intent(this, selectionController.class);
        adminLoginIntent = new Intent(this, adminViewController.class);

        DOP = new DatabaseOperations(CTX);

        //Following code is used to ensure that there is a blank item at the top of all the records
        Cursor studentCR = DOP.getStudentInfo(DOP);
        Cursor userCR = DOP.getUserInfo(DOP);
        Cursor testCR = DOP.getTests(DOP);

        if (testCR.getCount() == 0){
            DOP.addNewTest(DOP, "", "", 0);
        }

        if (studentCR.getCount() == 0){
            DOP.addNewStudent(DOP, "", 0, "");
        }

        if (userCR.getCount() == 0){
            DOP.addNewUser(DOP, "", "", "", "");
        }



        //Following code is used to relate the sample test to the test and questions tables.
//        DOP.addNewTest(DOP, "sampleTest", "Administrator", 1);

//        int pic1id = this.getResources().getIdentifier("p" + 1 + "a", "drawable", this.getPackageName());
//        int pic2id = this.getResources().getIdentifier("p" + 1 + "b", "drawable", this.getPackageName());
//        int pic3id = this.getResources().getIdentifier("p" + 1 + "c", "drawable", this.getPackageName());
//        int pic4id = this.getResources().getIdentifier("p" + 1 + "d", "drawable", this.getPackageName());

//        int audioId = this.getResources().getIdentifier("a" + 1, "raw", this.getPackageName());


//        try {
//            audio = DOP.audioToByteArray((Activity) this, audioId);
//        } catch(FileNotFoundException ie) {
//            ie.printStackTrace();
//        } catch(IOException ie){
//            ie.printStackTrace();
//        }

//        Bitmap pic1Bitmap = BitmapFactory.decodeResource(CTX.getResources(), pic1id);
//        Bitmap pic2Bitmap = BitmapFactory.decodeResource(CTX.getResources(), pic2id);
//        Bitmap pic3Bitmap = BitmapFactory.decodeResource(CTX.getResources(), pic3id);
//        Bitmap pic4Bitmap = BitmapFactory.decodeResource(CTX.getResources(), pic4id);
//
//
//        byte[] pic2 = DOP.jpgToByteArray(pic2Bitmap);
//        byte[] pic3 = DOP.jpgToByteArray(pic3Bitmap);
//        byte[] pic4 = DOP.jpgToByteArray(pic4Bitmap);
//        byte[] pic1 = DOP.jpgToByteArray(pic1Bitmap);









        //Following method class detects when user has clicked the login button and validates
        //their credentials.

        //The method does this by fetching a cursor from the database that points to the top
        //of the credential columns (userName and password) and iterating through them.
        //If no match occurs, the entered credentials are invalid and a toast message
        //is displayed saying so.


        //If valid, users information will be loaded into userData class and the user will be
        //forwarded to the appropriate activity (admin or teacher login screens)


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = 1;
                String enteredName = userName.getText().toString();
                String enteredPassword = password.getText().toString();

                if (enteredName.equals("") || enteredPassword.equals("")){
                    Toast.makeText(getApplicationContext(), "Please fill out all fields to log in",
                            Toast.LENGTH_LONG).show();
                }

                //Placeholder for loggin in as administrator until we have a user type
                //column for our account info table.
                if ((enteredName.equals("admin") && (enteredPassword.equals("admin")))) {
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    currentUserData.getInstance().setUserName(enteredName);
                    currentUserData.getInstance().setPassword(enteredPassword);
                    currentUserData.getInstance().setUserType("Administrator");
                    userName.setText("");
                    password.setText("");
                    startActivity(adminLoginIntent);
                } else {

                    DOP = new DatabaseOperations(CTX);
                    //  DatabaseOperations DOP = new DatabaseOperations(CTX);
                    Cursor CR = DOP.getUserInfo(DOP);
                    int numUsers = CR.getCount();

                    if (numUsers == 0){
                        Toast.makeText(getApplicationContext(), "Wrong Credentials, please try again " +
                                "or contact testing administrators for help logging in.", Toast.LENGTH_SHORT).show();
                    } else {
                        CR.moveToFirst();

                        //will change to true if credentials are valid
                        boolean loginStatus = false;
                        do {
                            String NAME = "";
                            if (enteredName.equalsIgnoreCase(CR.getString(0)) &&
                                    enteredPassword.equals(CR.getString(1))) {
                                loginStatus = true;
                                NAME = CR.getString(0);
                                userType = CR.getString(2);
                                userRealName = CR.getString(3);

                            }


                        }
                        while (CR.moveToNext());         //will return false (and end loop) if there is no next row

                        if (loginStatus == true) {



                            Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                            currentUserData.getInstance().setUserName(enteredName);
                            currentUserData.getInstance().setPassword(enteredPassword);
                            currentUserData.getInstance().setUserType(userType);
                            currentUserData.getInstance().setUserRealName(userRealName);
                            userName.setText("");
                            password.setText("");

                            if (userType.equals("Administrator")){
                                startActivity(adminLoginIntent);
                            } else {
                                startActivity(teacherLoginIntent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong Credentials, please try again " +
                                    "or contact testing administrators for help logging in.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //
                    //
                    //                if(userName.getText().toString().equals("admin") &&
                    //
                    //                        password.getText().toString().equals("admin")) {
                    //
                    //
                    //                } else if(userName.getText().toString().equals("teacher") &&
                    //
                    //                        password.getText().toString().equals("teacher")) {


                }
            }
        });
    }

    public Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap background = BitmapFactory.decodeByteArray(byteArray, 0,
                byteArray.length);
        Bitmap back = Bitmap.createBitmap(background.getWidth(),
                background.getHeight(), Bitmap.Config.ARGB_8888);
        return back;
    }




/*    public void login(View view){
        Toast.makeText(getBaseContext(), "You have entered " + userName.getText().toString(), Toast.LENGTH_LONG) .show();
    }*/




}
