package com.example.nick.rapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;


////This class is part of the controller component of the model-view-controller design.


//THIS CLASS MAY BE BROKEN UP INTO TWO CLASSES, A SCREEN CONTROLLER AND A QUESTION CONTROLLER

//This class is responsible for managing the userData and questionData while the user interacts
//with the questions. It is responsible for loading new questions into currentQuestionData, changing the audio and
//visual assets displayed on the testquestions_screen and figuring
// out if that currentQuestionData is of the type practice, filler, or test.
// It will only tell the user if they are incorrect if the currentQuestionData is of the type practice.
// and the class will properly store the results depending on the type of currentQuestionData answered.
// The class is also responsible for detecting when the user has concluded the test
// and will forward the user to the doneController at the end.

public class questionsController extends AppCompatActivity {
    Intent proceedIntent;
    Button answerButton;
    currentQuestionData currentQuestionData;
    currentUserData currentUserData;
    DatabaseOperations dop;


    //We get the number of possible answers from the questionData so that we
    //know how many pics we need to put up.
    int numPosAnswers;

    //the values for the button visuals and the buttons themselves in the test layout.
    View opt1;
    View opt2;
    View opt3;
    View opt4;
    RadioButton opt1but;
    RadioButton opt2but;
    RadioButton opt3but;
    RadioButton opt4but;

    Context ctx;

    int n;

    int testId;
    int studentId;
    String testName;
    String studentName;

    int resultId;

    int audioTest;

    int numQuestions;
    int numQuestionsCorrect;
    int numQuestionsComplete;

    int recordID;












    //A media player to allow us to play audio.
    MediaPlayer mp;





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //get all questions associated with test
    //create new array to represent the questions that is the same length as the test
    //create corresponding arrays to store audio and images
    //we will store images and audio at index[x] in the appropiate arrays
    //where x represents the index of that currentQuestionData int the main currentQuestionData list
    //for the test, store audio first
    //then store images.

    //we will retrieve audio and images by getting the integer of the currentQuestionData
    //in the questionList and getting the audio/images stored at their index in the
    //respective arrays.



    //A helper method to produce the random list of practice questions and questions.
    //the length of the array is dictated by the test length feature of the currentQuestionData data singleton.
    //the array consists of every integer from 1 to test length, randomly organized.
    //must include special functionality for practice questions









    //On create, the activity needs to get the number of possible answers and either keep the default layout (3)
    //or add a 4th layout if needed if the numPosAnswers is 4.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testquestions_screen);
        mp = new MediaPlayer();
        ctx = this;

        currentQuestionData = currentQuestionData.getInstance();


        //Need to check if there is still some questionData, and if so, reset questionData to its
        //new state.
        if (currentQuestionData.isOldData() == true){
            currentQuestionData = new currentQuestionData(null, null, null, 0, 0, 0, 0, null, 0, 0, null, null, 0, 0, null, null, null, null, null, 0, 0, null,
                    null, null, null, null, null, true, 0, null, 0, false, null);
        }
        currentUserData = currentUserData.getInstance();


        dop = new DatabaseOperations(ctx);
        n = 1;
        resultId = 1;
        testName = currentUserData.getSelectedTest();
        studentName = currentUserData.getSelectedStudent();
        testId = dop.getTestIDByName(dop, currentUserData.getSelectedTest());
        studentId = dop.getStudentIDByName(dop, currentUserData.getSelectedStudent());


        

        audioTest = 0;






        //setting testNum to 1 for testing purposes, in final version testNum will be
        //set to the testID that was indicated in the previous screen.
        currentQuestionData.setTestID(testId);
        DatabaseOperations dop = new DatabaseOperations(ctx);
        currentQuestionData.setDop(dop);
        currentQuestionData.loadInitialData(dop);
        currentQuestionData.loadQuestionData(currentQuestionData.getDop(), currentQuestionData.getTestID());

        numQuestions = currentQuestionData.getQuestionList().length;
        numQuestionsCorrect = 0;
        numQuestionsComplete = 0;


        numPosAnswers = currentQuestionData.getNumPosAnswer();

        recordID = generateRandomID();

        dop.addNewTestCompletionRecord(dop, recordID, studentId, studentName, testId,
                testName, numQuestions, numQuestionsCorrect, numQuestionsComplete);




        opt1 = (View) findViewById(R.id.opt1pic);
        opt2 = (View) findViewById(R.id.opt2pic);
        opt3 = (View) findViewById(R.id.opt3pic);

        opt1but = (RadioButton) findViewById(R.id.opt1);
        opt2but = (RadioButton) findViewById(R.id.opt2);
        opt3but = (RadioButton) findViewById(R.id.opt3);



        if (currentQuestionData.getNumPosAnswer() == 4){

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
        playMp3(currentQuestionData.getAudioBytes());
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
        setButVisible();
        opt1but.setEnabled(true);
        opt2but.setEnabled(true);
        opt3but.setEnabled(true);
        if (numPosAnswers == 4){
            opt4but.setEnabled(true);
        }
        playMp3(currentQuestionData.getAudioBytes());
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                audioTest++;
            }
        });
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



    //This method loads new assets into screen based on the current currentQuestionData number.
    //it users the random number n to make sure we don't place the correct answer in the same location twice
    //and it also sets the correct answer to the proper option.
    public void loadQuestion() {




        //determines rather we need to play third set of instructions here.
        String audio;
//        if (currentQuestionData.getCurrentQtype() == "Practice"){
//            audio = "play";
//        } else {
//            audio = "stop";
//        }


        //The following if statement sets the currentQuestionData to the next currentQuestionData IF and only IF
        //we are not on the first currentQuestionData. If we are loading the questions for the first time, we want
        //it to load the first currentQuestionData.
        if (currentQuestionData.getFirstQuestion() == false) {
            if ((currentQuestionData.getPracCorrect() == 2) || (currentQuestionData.getCurrentQIndex() == (currentQuestionData.getNumPractice() - 1))){
                currentQuestionData.setCurrentQIndex(currentQuestionData.getNumPractice() - 1);
                currentQuestionData.setCurrentQtype("Test");
            }
            currentQuestionData.setNextQuestion();

            //code to set the current currentQuestionData to the next currentQuestionData OR
            //forward them to the first practice item if they just successfully
            //completed the second practice currentQuestionData.

        } else {
            currentQuestionData.setFirstQuestion(false);
        }


        //currentQIndex describes the position in the questionList array that
        //the currentQuestionData is located at.

       // currentQuestionData.setCurrentQIndex(currentQuestionData.getCurr);
        currentQuestionData.findCurrentQNum();
        currentQuestionData.setAttempts(0);
        //THIS WILL BE REMOVED AND REPLACED WITH A QUERY TO SEE IF PROBLEM IS PRACTICE IN DATABASE
        if (currentQuestionData.getCurrentQIndex() < currentQuestionData.getNumPractice()){
            currentQuestionData.setCurrentQtype("Practice");
        } else  {
            currentQuestionData.setCurrentQtype("Test");
        }

        if (currentQuestionData.getCurrentQIndex() == 0){
            disableButtons();
            setButInvisible();
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
            setButInvisible();
            //mp = MediaPlayer.create(this, audID);
            //mp.start();
            playMp3(currentQuestionData.getAudioBytes());
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    enableButtons();
                }
            });
        }

        Bitmap[] pics = new Bitmap[4];
        pics[0] = currentQuestionData.getPic1Bitmap();
        pics[1] = currentQuestionData.getPic2Bitmap();
        pics[2] = currentQuestionData.getPic3Bitmap();
        pics[3] = currentQuestionData.getPic4Bitmap();




        if (numPosAnswers == 4){
//            int pic4id = this.getResources().getIdentifier("p" + currentQNum + "d", "drawable", this.getPackageName());
            Random rand = new Random();
            int prevn = n;
            while (n == prevn){
                n = rand.nextInt(3) + 1;
            }
            switch (n){
                case 1 :
//                    opt1.setBackground(getResources().getDrawable(pic1id));
//                    opt2.setBackground(getResources().getDrawable(pic2id));
//                    opt3.setBackground(getResources().getDrawable(pic3id));
//                    opt4.setBackground(getResources().getDrawable(pic4id));

                    opt1.setBackground(new BitmapDrawable(getResources(), pics[0]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    opt4.setBackground(new BitmapDrawable(getResources(), pics[3]));
                    currentQuestionData.setCorrectAnswer(1);
                    mp.start();
                    break;
                case 2:
//                    opt1.setBackground(getResources().getDrawable(pic4id));
//                    opt2.setBackground(getResources().getDrawable(pic3id));
//                    opt3.setBackground(getResources().getDrawable(pic1id));
//                    opt4.setBackground(getResources().getDrawable(pic2id));
                    opt1.setBackground(new BitmapDrawable(getResources(), pics[3]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[0]));
                    opt4.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    currentQuestionData.setCorrectAnswer(3);
                    mp.start();
                    break;
                case 3:
//                    opt1.setBackground(getResources().getDrawable(pic2id));
//                    opt2.setBackground(getResources().getDrawable(pic1id));
//                    opt3.setBackground(getResources().getDrawable(pic4id));
//                    opt4.setBackground(getResources().getDrawable(pic3id));
                    opt1.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[0]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    opt4.setBackground(new BitmapDrawable(getResources(), pics[3]));
                    currentQuestionData.setCorrectAnswer(2);
                    mp.start();
                    break;
                case 4:
//                    opt1.setBackground(getResources().getDrawable(pic3id));
//                    opt2.setBackground(getResources().getDrawable(pic4id));
//                    opt3.setBackground(getResources().getDrawable(pic2id));
//                    opt4.setBackground(getResources().getDrawable(pic1id));
                    opt1.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[3]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    opt4.setBackground(new BitmapDrawable(getResources(), pics[0]));
                    currentQuestionData.setCorrectAnswer(4);
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
                    opt1.setBackground(new BitmapDrawable(getResources(), pics[0]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    currentQuestionData.setCorrectAnswer(1);

                    mp.start();
                    break;
                case 2:
//                    opt1.setBackground(getResources().getDrawable(pic3id));
//                    opt2.setBackground(getResources().getDrawable(pic1id));
//                    opt3.setBackground(getResources().getDrawable(pic2id));
                    opt1.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[0]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    currentQuestionData.setCorrectAnswer(2);

                    mp.start();
                    break;
                case 3:
//                    opt1.setBackground(getResources().getDrawable(pic2id));
//                    opt2.setBackground(getResources().getDrawable(pic3id));
//                    opt3.setBackground(getResources().getDrawable(pic1id));
                    opt1.setBackground(new BitmapDrawable(getResources(), pics[1]));
                    opt2.setBackground(new BitmapDrawable(getResources(), pics[2]));
                    opt3.setBackground(new BitmapDrawable(getResources(), pics[0]));


                    currentQuestionData.setCorrectAnswer(3);
                    mp.start();
                    break;
            }
        }

    }

    //Message that appears when user answers incorrectly.
    public void tryAgainMessage() {
        mp = MediaPlayer.create(this, R.raw.tryagain);
        mp.start();
    }

    //Behavior that is triggered when a user selects the wrong answer in the practice questions.
    public void incorrectAnswer(){
        currentQuestionData.incrementAttempts();
        if (currentQuestionData.getAttempts() >= 2){
            playMp3(currentQuestionData.getAudioBytes());
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

    //Behavior that is triggered when the user successfully completes a second practice currentQuestionData.
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
    //the user will continue to the next currentQuestionData, they will have finished the test,
    //or they will have finished the practice questions.
    public void processAnswer(){
        dop.updateTestCompletionRecord(dop, recordID, numQuestionsCorrect, numQuestionsComplete);
        //Code to add +1 to pracCorrect if attempts = 0
        //so that when user gets two practice questions right on their first attempt
        //they are forwarded to the first real practice item regardless of rather
        //they finished all the practice questions.
        if ((currentQuestionData.getAttempts() == 0) && (currentQuestionData.getCurrentQtype() == "Practice")){
            currentQuestionData.incrementPracCorrect();
        } else {
            currentQuestionData.setPracCorrect(0);
        }

        if (currentQuestionData.getCurrentQIndex() == (currentQuestionData.getTestSize() - 1)) {
            //go to last activity
            //RECORD RESULTS HERE
//            dop.addNewTestCompletionRecord(dop, generateRandomID(), studentId, studentName, testId,
//                    testName, numQuestions, numQuestionsCorrect, numQuestionsComplete);

            currentQuestionData.setOldData(true);
            startActivity(proceedIntent);
            this.finish();
        } else if ((currentQuestionData.getCurrentQIndex() == (currentQuestionData.getNumPractice() - 1)
                || currentQuestionData.getPracCorrect() == 2)){
            transitionToTestItems();
        } else {
            loadQuestion();
        }
    }




    public boolean isUniqueId(int newId, Cursor CR){
        do {
            if (newId == CR.getInt(1)) {
                return false;
            }
        }
        while (CR.moveToNext());
        return true;
    }

    public int generateRandomID(){
        int id;
        //  DatabaseOperations DOP = new DatabaseOperations(CTX);
        Cursor CR = dop.getTestCompletionRecords(dop);

        int numRecords = CR.getCount();

        if (numRecords == 0){
            id = 1;
            return id;
        }
        CR.moveToFirst();

        Random rand = new Random();

        id = rand.nextInt((100000 - 0) + 1) + 0;
        while (isUniqueId(id, CR) == false) {
            id = rand.nextInt((100000 - 0) + 1) + 0;
        }
        return id;
    }










    //The answer method responds to a user clicking any option
    //from the set of radio buttons available on the currentQuestionData screen.
    //The method will detect user input and figure out if the user answered correctly. If the user answered
    // incorrectly and the currentQuestionData is a practiceQuestion, the controller will tell the tesyQuestion screen to prompt
    //the user to try again via a toast message. The method will forward them to the next currentQuestionData (currently
    //the good job screen) if they answered correctly by loading a new currentQuestionData into the currentQuestionData,
    //and displaying new items on the screen. The answer is responsible for detecting when the user
    //has answered all the items in the test, and forward them to the doneController accordingly.

    //The answer method is also responsible for controlling the storing of results, depending on the kind
    //of currentQuestionData answered.
    public void answer(View view) {
            boolean checked = ((RadioButton) view).isChecked();
            numQuestionsComplete++;
            switch (view.getId()) {
                case R.id.opt1:
                    if (currentQuestionData.getCorrectAnswer() == 1){
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                true, testId, testName, resultId, recordID, studentId, studentName);
                        numQuestionsCorrect++;
                        resultId++;
                    } else {
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                false, testId,testName, resultId, recordID, studentId, studentName);
                        resultId++;
                    }

                    if ((currentQuestionData.getCorrectAnswer() == 1) ||
                     (currentQuestionData.getCurrentQtype() != "Practice")) {
                        processAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt2:
                    if (currentQuestionData.getCorrectAnswer() == 2){
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                true, testId, testName, resultId, recordID, studentId, studentName);
                        numQuestionsCorrect++;
                        resultId++;
                    } else {
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                false, testId,testName, resultId, recordID, studentId, studentName);
                        resultId++;
                    }


                    if ((currentQuestionData.getCorrectAnswer() == 2) ||
                            (currentQuestionData.getCurrentQtype() != "Practice")) {
                        processAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt3:
                    if (currentQuestionData.getCorrectAnswer() == 3){
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                true, testId, testName, resultId, recordID, studentId, studentName);
                        numQuestionsCorrect++;
                        resultId++;
                    } else {
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                false, testId,testName, resultId, recordID, studentId, studentName);
                        resultId++;
                    }

                    if ((currentQuestionData.getCorrectAnswer() == 3) ||
                            (currentQuestionData.getCurrentQtype() != "Practice")) {
                        processAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt4:
                    if (currentQuestionData.getCorrectAnswer() == 4){
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                true, testId, testName, resultId, recordID, studentId, studentName);
                        resultId++;
                        numQuestionsCorrect++;
                    } else {
                        dop.addNewResult(dop, currentQuestionData.getCurrentQuestionID(),
                                currentQuestionData.getCurrentQuestionWord(),
                                false, testId,testName, resultId, recordID, studentId, studentName);
                        resultId++;
                    }

                    if ((currentQuestionData.getCorrectAnswer() == 4) ||
                            (currentQuestionData.getCurrentQtype() != "Practice")) {
                        processAnswer();
                    } else {
                      incorrectAnswer(); //user answers incorrectly
                    }
                    break;
            }
        }



    //This method is used as a placeholder on the button
    public void doNothing(View view){
    }




    //This method is used to ensure that the user cannot press the back button while in a test
    @Override
    public void onBackPressed() {
    }





    public void playMp3(byte[] mp3SoundByteArray) {
        try {

            audioTest++;
            // create temp file that will hold byte array
            File tempMp3 = File.createTempFile("tempFile", "mp3", getCacheDir());
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);
            fos.write(mp3SoundByteArray);
            fos.close();

            // Tried passing path directly, but kept getting
            // "Prepare failed.: status=0x1"
            // so using file descriptor instead
            FileInputStream fis = new FileInputStream(tempMp3);
            mp.reset();
            mp.setDataSource(fis.getFD());

            mp.prepare();
            mp.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
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
