package com.example.nick.rapp;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.util.Random;


////This class is part of the controller component of the model-view-controller design.


//THIS CLASS MAY BE BROKEN UP INTO TWO CLASSES, A SCREEN CONTROLLER AND A QUESTION CONTROLLER

//This class is responsible for managing the userData and questionData while the user interacts
//with the questions. It is responsible for loading new questions into currentQuestionData, changing the audio and
//visual assets displayed on the testquestions_screen and figuring
// out if that question is of the type practice, filler, or test.
// It will only tell the user if they are incorrect if the question is of the type practice.
// and the class will properly store the results depending on the type of question answered.
// The class is also responsible for detecting when the user has concluded the test
// and will forward the user to the doneController at the end.

public class questionsController extends AppCompatActivity {
    Intent proceedIntent;
    Button answerButton;
    TextView testNotice;
    currentQuestionData question;
    RadioButton opt1;
    RadioButton opt2;
    RadioButton opt3;
    int currentQNum = question.getInstance().getQuestionNum();
    int n;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testquestions_screen);


        opt1 = (RadioButton) findViewById(R.id.opt1);
        opt2 = (RadioButton) findViewById(R.id.opt2);
        opt3 = (RadioButton) findViewById(R.id.opt3);




        //Create intent to forward user to the real test items
        // (currently doneController) when user finishes all practice questions.
        //In the final version, it will have to detect if the user has answered two
        //practice questions correctly in a row before sending them to the
        //test items.
        proceedIntent = new Intent(this, doneController.class);


        testNotice = (TextView) findViewById(R.id.testNotice);
        int testSize = question.getInstance().getTestSize();
        String notice = String.valueOf(testSize);
        testNotice.setText(notice);

        loadQuestion();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    //This method loads new assets into screen based on the current question number.
    //it users the random number n to make sure we don't place the correct answer in the same location twice
    //and it also sets the correct answer to the proper option.
    public void loadQuestion() {

        int pic1id = this.getResources().getIdentifier("p" + currentQNum + "a", "drawable", this.getPackageName());
        int pic2id = this.getResources().getIdentifier("p" + currentQNum + "b", "drawable", this.getPackageName());
        int pic3id = this.getResources().getIdentifier("p" + currentQNum + "c", "drawable", this.getPackageName());

        Random rand = new Random();

        int prevn = n;
        while (n == prevn){
            n = rand.nextInt(3) + 1;
        }
        Log.d("random", "current random number is " + n);

        switch (n) {
            case 1:
                opt1.setBackground(getResources().getDrawable(pic1id));
                opt2.setBackground(getResources().getDrawable(pic2id));
                opt3.setBackground(getResources().getDrawable(pic3id));
                question.getInstance().setCorrectAnswer(2);
                Log.d("answer", "current correct answer is " + question.getInstance().getCorrectAnswer());
            case 2:
                opt1.setBackground(getResources().getDrawable(pic3id));
                opt2.setBackground(getResources().getDrawable(pic1id));
                opt3.setBackground(getResources().getDrawable(pic2id));
                question.getInstance().setCorrectAnswer(3);
                Log.d("answer", "current correct answer is " + question.getInstance().getCorrectAnswer());
            case 3:
                opt1.setBackground(getResources().getDrawable(pic2id));
                opt2.setBackground(getResources().getDrawable(pic3id));
                opt3.setBackground(getResources().getDrawable(pic1id));
                question.getInstance().setCorrectAnswer(1);
                Log.d("answer", "current correct answer is " + question.getInstance().getCorrectAnswer());
        }




    }


    //THIS METHOD SHOULD BE BROKEN UP INTO SUB METHODS WHEN WE FINISH THE PROTOTYPE

    //The answer method responds to a user clicking any option
    //from the set of radio buttons available on the question screen.
    //The method will detect user input and figure out if the user answered correctly. If the user answered
    // incorrectly and the question is a practiceQuestion, the controller will tell the tesyQuestion screen to prompt
    //the user to try again via a toast message. The method will forward them to the next question (currently
    //the good job screen) if they answered correctly by loading a new question into the currentQuestionData,
    //and displaying new items on the screen. The answer is responsible for detecting when the user
    //has answered all the items in the test, and forward them to the doneController accordingly.

    //The answer method is also responsible for controllign the storing of results, depending on the kind
    //of question answered.
    public void answer(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.opt1:
                question.getInstance().nextQuestion();
                currentQNum = question.getInstance().getQuestionNum();
                Log.d("currentQuestion","The current question is " + currentQNum);


                loadQuestion();
                break;
            case R.id.opt2:
                Toast.makeText(getApplicationContext(), "Incorrect, please try again", Toast.LENGTH_SHORT).show();
                break;
            case R.id.opt3:
                Toast.makeText(getApplicationContext(), "Incorrect, please try again", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "questionsController Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nick.rapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "questionsController Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.nick.rapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
