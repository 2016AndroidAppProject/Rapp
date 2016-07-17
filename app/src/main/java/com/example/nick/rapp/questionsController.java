package com.example.nick.rapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;
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
    currentQuestionData question;

    //the values for the button visuals and the buttons themselves in the test layout.
    View opt1;
    View opt2;
    View opt3;
    View opt4;
    RadioButton opt1but;
    RadioButton opt2but;
    RadioButton opt3but;
    RadioButton opt4but;


    //values that describe which question we are on, how many possible answers the questions have, and
    //an array of integers that determines that order of questions.
    int currentQIndex;

    int currentQNum;

    int numPracticeItems;

    int numPosAnswers;
    int[] practiceList;
    int[] questionList;

    //integer attempts describes how many times a user has incorrectly answer so we know when to play
    //the word audio again (on the 3rd attempt)
    int attempts;

    //integer n is used as a random number to shuffle the position of the buttons on the screen
    int n;

    //integer testSize is used to say how big the test should be. It is obtained from the testsize
    //quality of the question data in the oncreate method.
    int testSize;

    //integer audID is used as an identifier for our audio files
    int audID;

    MediaPlayer mp;


    //this simple boolean prevents the index of the question from incrementing when we first load the questions
    boolean firstQuestion;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //A helper method to produce the random list of practice questions and questions.
    //the length of the array is dictated by the test length feature of the question data singleton.
    //the array consists of every integer from 1 to test length, randomly organized.
    //must include special functionality for practice questions

    private void getQuestionList(){
        questionList = new int[testSize];
        int j = 1;
        for (int i = 0; i < testSize; i++){
            questionList[i] = j;
            j++;
        }
        numPracticeItems = this.question.getInstance().getNumPractice();
        getPracticeArray();
        question.getInstance().setPracticeList(practiceList);
        System.out.println("Practice list copied: " + Arrays.toString(question.getInstance().getPracticeList()));

        getNonPracticeItemsList();
        shuffleArray(questionList);
        System.out.println("Test list after shuffle: " + Arrays.toString(questionList));

        int[] finalQuestionList = new int[testSize];


        attempts = 0;
        System.arraycopy(practiceList, 0, finalQuestionList, 0, numPracticeItems);
        System.arraycopy(questionList, 0, finalQuestionList, numPracticeItems, questionList.length);


        questionList = finalQuestionList;

        question.getInstance().setQuestionList(questionList);
        System.out.println("Final: " + Arrays.toString(question.getInstance().getQuestionList()));
    }

    //This array takes the number of practice items and creates
    //a seperate array with that number of spaces
    //then copies the proper number of elements from the questionList
    //to the practiceList.
    private void getPracticeArray(){
        //copies the proper number of question numbers from the question List
        //to the practice list depending on the amount of practice items.

        practiceList = new int[numPracticeItems];
        System.out.println("The number of practice items is " + numPracticeItems);
        System.arraycopy(questionList, 0, practiceList,
                0, numPracticeItems);
        System.out.println("Practice list: " + Arrays.toString(question.getInstance().getPracticeList()));
    }


    private void getNonPracticeItemsList(){
        int[] newQuestionList = new int[questionList.length - numPracticeItems];
        System.arraycopy(questionList, numPracticeItems, // from array[removeEnd]
                newQuestionList, 0,            // to array[removeStart]
                questionList.length - numPracticeItems);
        questionList = newQuestionList;
        System.out.println("Test list before shuffle: " + Arrays.toString(questionList));
    }

    static void shuffleArray(int[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void findCurrentQNum(){
        currentQNum = this.questionList[currentQIndex];
    }

    //On create, the activity needs to get the number of possible answers and either keep the default layout (3)
    //or add a 4th layout if needed if the numPosAnswers is 4.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testquestions_screen);


        testSize = question.getInstance().getTestSize();
        getQuestionList();
        question.getInstance().setCurrentIndex(0);

        firstQuestion = true;
        attempts = 0;

        numPracticeItems = this.question.getInstance().getNumPractice();

        opt1 = (View) findViewById(R.id.opt1pic);
        opt2 = (View) findViewById(R.id.opt2pic);
        opt3 = (View) findViewById(R.id.opt3pic);

        opt1but = (RadioButton) findViewById(R.id.opt1);
        opt2but = (RadioButton) findViewById(R.id.opt2);
        opt3but = (RadioButton) findViewById(R.id.opt3);

        numPosAnswers = question.getInstance().getNumPosAnswer();

        if (numPosAnswers == 4){

            LinearLayout top = (LinearLayout) findViewById(R.id.top);
            RelativeLayout opt3Layout = (RelativeLayout) findViewById(R.id.opt3layout);
            RelativeLayout opt4Layout = (RelativeLayout) findViewById(R.id.opt4layout);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            top.removeView(opt3Layout);
            top.removeView(opt4Layout);
            top.addView(opt3Layout, params);
            top.addView(opt4Layout, params);




            opt4 = (View) findViewById(R.id.opt4pic);
            opt4but = (RadioButton) findViewById(R.id.opt4);
            opt4.setSoundEffectsEnabled(false);


        }


        opt1.setSoundEffectsEnabled(false);
        opt2.setSoundEffectsEnabled(false);
        opt3.setSoundEffectsEnabled(false);




        //Create intent to forward user to the real test items
        // (currently doneController) when user finishes all practice questions.
        //In the final version, it will have to detect if the user has answered two
        //practice questions correctly in a row before sending them to the
        //test items.
        proceedIntent = new Intent(this, doneController.class);






        loadQuestion();



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    public void performOnEnd(){
        audID = this.getResources().getIdentifier("a" + currentQNum, "raw", this.getPackageName());
        mp = MediaPlayer.create(this, audID);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                enableButtons();
            }
        });

    }

    public void proceedOnEnd(){
        setButVisible();
        enableButtons();
        loadQuestion();
    }

    public void disableButtons(){
        opt1but.setEnabled(false);
        opt2but.setEnabled(false);
        opt3but.setEnabled(false);

        if (numPosAnswers == 4){
            opt4but.setEnabled(false);
        }
    }

    public void enableButtons(){
        opt1but.setEnabled(true);
        opt2but.setEnabled(true);
        opt3but.setEnabled(true);

        if (numPosAnswers == 4){
            opt4but.setEnabled(true);
        }
    }

    public void setButVisible(){
        opt1.setVisibility(View.VISIBLE);
        opt2.setVisibility(View.VISIBLE);
        opt3.setVisibility(View.VISIBLE);
        if (numPosAnswers == 4){
            opt4.setVisibility(View.VISIBLE);
        }

    }

    public void setButInvisible(){
        opt1.setVisibility(View.GONE);
        opt2.setVisibility(View.GONE);
        opt3.setVisibility(View.GONE);
        if (numPosAnswers == 4){
            opt4.setVisibility(View.GONE);
        }

    }



    //This method loads new assets into screen based on the current question number.
    //it users the random number n to make sure we don't place the correct answer in the same location twice
    //and it also sets the correct answer to the proper option.
    public void loadQuestion() {
        //determines rather we need to play third set of instructions here.
        String audio;
//        if (question.getInstance().getCurrentQtype() == "Practice"){
//            audio = "play";
//        } else {
//            audio = "stop";
//        }
        if (firstQuestion == false) {
            question.getInstance().nextQuestion();
        } else {
            firstQuestion = false;
        }
        currentQIndex = question.getInstance().getQuestionNum();
        findCurrentQNum();
        Log.d("currentIndex", "The current question is " + currentQNum + " located at " + currentQIndex);
        attempts = 0;
        //THIS WILL BE REMOVED AND REPLACED WITH A QUERY TO SEE IF PROBLEM IS PRACTICE IN DATABASE
        if ((currentQIndex == 0) || (currentQIndex == 1)){
            question.getInstance().setCurrentQtype("Practice");

        } else  {
            question.getInstance().setCurrentQtype("Test");
        }
        audID = this.getResources().getIdentifier("a" + currentQNum, "raw", this.getPackageName());
        if (currentQIndex == 0){
            disableButtons();
            mp = MediaPlayer.create(this, R.raw.practiceiteminstructions);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    performOnEnd();
                }
            });
        }
        else {
            disableButtons();
            mp = MediaPlayer.create(this, audID);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    enableButtons();
                }
            });
        }
        int pic1id = this.getResources().getIdentifier("p" + currentQNum + "a", "drawable", this.getPackageName());
        int pic2id = this.getResources().getIdentifier("p" + currentQNum + "b", "drawable", this.getPackageName());
        int pic3id = this.getResources().getIdentifier("p" + currentQNum + "c", "drawable", this.getPackageName());
        if (numPosAnswers == 4){
            int pic4id = this.getResources().getIdentifier("p" + currentQNum + "d", "drawable", this.getPackageName());
            Random rand = new Random();
            int prevn = n;
            while (n == prevn){
                n = rand.nextInt(3) + 1;
            }
            switch (n){
                case 1 :
                    opt1.setBackground(getResources().getDrawable(pic1id));
                    opt2.setBackground(getResources().getDrawable(pic2id));
                    opt3.setBackground(getResources().getDrawable(pic3id));
                    opt4.setBackground(getResources().getDrawable(pic4id));
                    question.getInstance().setCorrectAnswer(2);
                    mp.start();
                    break;
                case 2:
                    opt1.setBackground(getResources().getDrawable(pic4id));
                    opt2.setBackground(getResources().getDrawable(pic3id));
                    opt3.setBackground(getResources().getDrawable(pic1id));
                    opt4.setBackground(getResources().getDrawable(pic2id));
                    question.getInstance().setCorrectAnswer(4);
                    mp.start();
                    break;
                case 3:
                    opt1.setBackground(getResources().getDrawable(pic2id));
                    opt2.setBackground(getResources().getDrawable(pic1id));
                    opt3.setBackground(getResources().getDrawable(pic4id));
                    opt4.setBackground(getResources().getDrawable(pic3id));
                    question.getInstance().setCorrectAnswer(1);
                    mp.start();
                    break;
                case 4:
                    opt1.setBackground(getResources().getDrawable(pic3id));
                    opt2.setBackground(getResources().getDrawable(pic4id));
                    opt3.setBackground(getResources().getDrawable(pic2id));
                    opt4.setBackground(getResources().getDrawable(pic1id));
                    question.getInstance().setCorrectAnswer(3);
                    mp.start();
                    break;
            }
        } else if (numPosAnswers == 3) {
            Random rand = new Random();
            int prevn = n;
            while (n == prevn) {
                n = rand.nextInt(3) + 1;
            }
            switch (n) {
                case 1:
                    opt1.setBackground(getResources().getDrawable(pic1id));
                    opt2.setBackground(getResources().getDrawable(pic2id));
                    opt3.setBackground(getResources().getDrawable(pic3id));
                    question.getInstance().setCorrectAnswer(2);
                    Log.d("answer", "current correct answer is " + question.getInstance().getCorrectAnswer());
                    mp.start();
                    break;
                case 2:
                    opt1.setBackground(getResources().getDrawable(pic3id));
                    opt2.setBackground(getResources().getDrawable(pic1id));
                    opt3.setBackground(getResources().getDrawable(pic2id));
                    question.getInstance().setCorrectAnswer(3);
                    Log.d("answer", "current correct answer is " + question.getInstance().getCorrectAnswer());
                    mp.start();
                    break;
                case 3:
                    opt1.setBackground(getResources().getDrawable(pic2id));
                    opt2.setBackground(getResources().getDrawable(pic3id));
                    opt3.setBackground(getResources().getDrawable(pic1id));
                    question.getInstance().setCorrectAnswer(1);
                    Log.d("answer", "current correct answer is " + question.getInstance().getCorrectAnswer());
                    mp.start();
                    break;
            }
        }
    }

    //Message that appears when user answers incorrectly.
    public void tryAgainMessage() {
        Toast.makeText(getApplicationContext(), "Incorrect, please try again", Toast.LENGTH_SHORT).show();
        mp = MediaPlayer.create(this, R.raw.tryagain);
        mp.start();
    }

    //Behavior that is triggered when a user selects the wrong answer in the practice questions.
    public void incorrectAnswer(){
        attempts++;
        if (attempts >= 2){
            mp = MediaPlayer.create(this, audID);
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    tryAgainMessage();
                }
            });
        } else {
            tryAgainMessage();
        }
    }

    //Behavior that is triggered when the user successfully completes a second practice question.
    public void transitionToTestItems(){
        disableButtons();
        mp = MediaPlayer.create(this, R.raw.transitiontotestitems);
        mp.start();
        opt1.setVisibility(View.GONE);
        opt2.setVisibility(View.GONE);
        opt3.setVisibility(View.GONE);
        if (numPosAnswers == 4){
            opt4.setVisibility(View.GONE);
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                proceedOnEnd();
            }
        });
    }

    //Behavior that is triggered when the user chooses the correct button. Either
    //the user will continue to the next question, they will have finished the test,
    //or they will have finished the practice questions.
    public void processCorrectAnswer(){
        if (currentQIndex == (testSize - 1)) {
            //go to last activity
            //RECORD RESULTS HERE
            startActivity(proceedIntent);
        } else if (currentQIndex == 1){
            transitionToTestItems();
        } else {
            loadQuestion();
        }
    }









    //The answer method responds to a user clicking any option
    //from the set of radio buttons available on the question screen.
    //The method will detect user input and figure out if the user answered correctly. If the user answered
    // incorrectly and the question is a practiceQuestion, the controller will tell the tesyQuestion screen to prompt
    //the user to try again via a toast message. The method will forward them to the next question (currently
    //the good job screen) if they answered correctly by loading a new question into the currentQuestionData,
    //and displaying new items on the screen. The answer is responsible for detecting when the user
    //has answered all the items in the test, and forward them to the doneController accordingly.

    //The answer method is also responsible for controlling the storing of results, depending on the kind
    //of question answered.
    public void answer(View view) {
            boolean checked = ((RadioButton) view).isChecked();
            switch (view.getId()) {
                case R.id.opt1:
                    if ((question.getInstance().getCorrectAnswer() == 1) ||
                     (question.getInstance().getCurrentQtype() != "Practice")) {
                        processCorrectAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt2:
                    if ((question.getInstance().getCorrectAnswer() == 2) ||
                            (question.getInstance().getCurrentQtype() != "Practice")) {
                        processCorrectAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt3:
                    if ((question.getInstance().getCorrectAnswer() == 3) ||
                            (question.getInstance().getCurrentQtype() != "Practice")) {
                        processCorrectAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt4:
                    if ((question.getInstance().getCorrectAnswer() == 4) ||
                            (question.getInstance().getCurrentQtype() != "Practice")) {
                        processCorrectAnswer();
                    } else {
                      incorrectAnswer(); //user answers incorrectly
                    }
                    break;
            }
        }



    //This method is used as a placeholder on the button
    public void doNothing(View view){
    }




    //Following two methods were generated when the  to help with getting rid of compile errors.
    //The following two methods were generated when the following classes were imported
    //import com.google.android.gms.appindexing.Action;
    //import com.google.android.gms.appindexing.AppIndex;
    //import com.google.android.gms.common.api.GoogleApiClient;


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
