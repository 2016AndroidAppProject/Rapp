package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import android.os.Handler;


////This class is part of the controller component of the model-view-controller design.


//THIS CLASS MAY BE BROKEN UP INTO TWO CLASSES, A SCREEN CONTROLLER AND A QUESTION CONTROLLER

//This class is responsible for managing the userData and testItemData while the user interacts
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

    TextView practiceNotice;


    //We get the number of possible answers from the testItemData so that we
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


    String upperLeft;
    String upperRight;
    String lowerRight;
    String lowerLeft;

    Context ctx;

    int n;

    int testId;
    int studentId;
    String testName;
    String studentName;

    int resultId;

    int audioTest;

    boolean fillerItemComplete;
    HashMap<Integer, String> fillerWordIndex;

    int numQuestions;
    int numQuestionsCorrect;
    int numQuestionsComplete;

    int numTestItemsComplete;

    int trueNumTestItemsComplete;

    int recordID;

    Bitmap image1;
    Bitmap image2;
    Bitmap image3;
    Bitmap image4;

    byte[] currentAudio;

    boolean continuingTest;
    boolean practiceMode;
    boolean play;

    HashMap<String, Integer> alreadyAnsweredWords;

    String currentUserName;
    Handler handler;













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

        handler = new Handler();
        play = true;

        numTestItemsComplete = 0;
        trueNumTestItemsComplete = 0;
        if (currentUserData.getInstance().isContinueTest()){
            continuingTest = true;
        } else {
            continuingTest = false;
        }

        practiceNotice = (TextView) this.findViewById(R.id.practiceNotice);
        practiceNotice.setVisibility(View.INVISIBLE);
        practiceMode = currentUserData.getInstance().isPracticeMode();
        if (practiceMode == true){
            practiceNotice.setVisibility(View.VISIBLE);
        }

        currentQuestionData = currentQuestionData.getInstance();
        dop = new DatabaseOperations(ctx);

        fillerItemComplete = false;
        fillerWordIndex = new HashMap<Integer, String>();

        currentUserData = currentUserData.getInstance();
        //Getting teh relevant information from the currentUserData (whose the current student, test?)
        testId = dop.getTestIDByName(dop, currentUserData.getSelectedTest());
        n = 1;

        testName = currentUserData.getSelectedTest();
        studentName = currentUserData.getSelectedStudent();

        studentId = currentUserData.getSelectedStudentId();

        if (currentQuestionData.oldData == true){
            currentQuestionData =  new currentQuestionData(null, null, null, 0, null, null,false, 3, "Practice", 0, 0, null, null, null, null, 0, 0, 0, 0, 0, 0, null, null, true, null);
        }

        alreadyAnsweredWords = new HashMap<String, Integer>();
        currentUserName = currentUserData.getInstance().getUserName();
        loadInitialData(dop);

        //Need to check if there is still some testItemData, and if so, reset testItemData to its
        //new state.


//        if (currentUserData.getInstance().getUserType().equals("Administrator")){
//            currentUserName = "Admin";
//        } else {

       // }
        audioTest = 0;



        numQuestions = currentQuestionData.numTestItems;
        numQuestionsCorrect = 0;
        numQuestionsComplete = 0;


        numPosAnswers = currentQuestionData.numPosAnswer;



        if ((currentUserData.isPracticeMode() == false) && (continuingTest == false)) {
            dop.addNewTestCompletionRecord(dop, recordID, studentId, studentName, testId,
                    testName, numQuestions, numQuestionsCorrect, numQuestionsComplete, currentUserName);
        }



        opt1 = (View) findViewById(R.id.opt1pic);
        opt2 = (View) findViewById(R.id.opt2pic);
        opt3 = (View) findViewById(R.id.opt3pic);

        opt1but = (RadioButton) findViewById(R.id.opt1);
        opt2but = (RadioButton) findViewById(R.id.opt2);
        opt3but = (RadioButton) findViewById(R.id.opt3);



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
        playMp3(currentAudio);
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
        playMp3(currentAudio);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                audioTest++;
            }
        });
        audioLoop();
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
//        if (currentQuestionData.currentQtype == "Practice"){
//            audio = "play";
//        } else {
//            audio = "stop";
//        }


        //The following if statement sets the currentQuestionData to the next currentQuestionData IF and only IF
        //we are not on the first currentQuestionData. If we are loading the questions for the first time, we want
        //it to load the first currentQuestionData.
        if (currentQuestionData.firstQuestion == false) {
        } else {
            currentQuestionData.firstQuestion = false;
        }

        if ((currentQuestionData.pracCorrect == 2) || (numQuestionsComplete == currentQuestionData.numPracticeItems)){
            currentQuestionData.currentQtype = "Test";
        }



        loadQuestionAssets();


        //currentQIndex describes the position in the questionList array that
        //the currentQuestionData is located at.


        currentQuestionData.attempts = 0;


        if (numQuestionsComplete == 0){
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
            playMp3(currentAudio);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    enableButtons();
                }
            });
        }


        if (numPosAnswers == 4){
//            int pic4id = this.getResources().getIdentifier("p" + currentQNum + "d", "drawable", this.getPackageName());
            Random rand = new Random();
            int prevn = n;
            while (n == prevn){
                n = rand.nextInt(4) + 1;
            }
            switch (n){
                case 1 :
//                    opt1.setBackground(getResources().getDrawable(pic1id));
//                    opt2.setBackground(getResources().getDrawable(pic2id));
//                    opt3.setBackground(getResources().getDrawable(pic3id));
//                    opt4.setBackground(getResources().getDrawable(pic4id));

                    opt1.setBackground(new BitmapDrawable(getResources(), image1));
                    opt2.setBackground(new BitmapDrawable(getResources(), image2));
                    opt3.setBackground(new BitmapDrawable(getResources(), image3));
                    opt4.setBackground(new BitmapDrawable(getResources(), image4));
                    lowerLeft = "A";
                    lowerRight = "B";
                    upperLeft = "C";
                    upperRight = "D";
                    currentQuestionData.correctAnswer = 1;
                    mp.start();
                    break;
                case 2:
//                    opt1.setBackground(getResources().getDrawable(pic4id));
//                    opt2.setBackground(getResources().getDrawable(pic3id));
//                    opt3.setBackground(getResources().getDrawable(pic1id));
//                    opt4.setBackground(getResources().getDrawable(pic2id));
                    opt1.setBackground(new BitmapDrawable(getResources(), image4));
                    opt2.setBackground(new BitmapDrawable(getResources(), image3));
                    opt3.setBackground(new BitmapDrawable(getResources(), image1));
                    opt4.setBackground(new BitmapDrawable(getResources(), image2));
                    lowerLeft = "D";
                    lowerRight = "C";
                    upperLeft = "A";
                    upperRight = "B";
                    currentQuestionData.correctAnswer = 3;
                    mp.start();
                    break;
                case 3:
//                    opt1.setBackground(getResources().getDrawable(pic2id));
//                    opt2.setBackground(getResources().getDrawable(pic1id));
//                    opt3.setBackground(getResources().getDrawable(pic4id));
//                    opt4.setBackground(getResources().getDrawable(pic3id));
                    opt1.setBackground(new BitmapDrawable(getResources(), image2));
                    opt2.setBackground(new BitmapDrawable(getResources(), image1));
                    opt3.setBackground(new BitmapDrawable(getResources(), image3));
                    opt4.setBackground(new BitmapDrawable(getResources(), image4));
                    lowerLeft = "B";
                    lowerRight = "A";
                    upperLeft = "C";
                    upperRight = "D";
                    currentQuestionData.correctAnswer = 2;
                    mp.start();
                    break;
                case 4:
//                    opt1.setBackground(getResources().getDrawable(pic3id));
//                    opt2.setBackground(getResources().getDrawable(pic4id));
//                    opt3.setBackground(getResources().getDrawable(pic2id));
//                    opt4.setBackground(getResources().getDrawable(pic1id));
                    opt1.setBackground(new BitmapDrawable(getResources(), image3));
                    opt2.setBackground(new BitmapDrawable(getResources(), image4));
                    opt3.setBackground(new BitmapDrawable(getResources(), image2));
                    opt4.setBackground(new BitmapDrawable(getResources(), image1));
                    lowerLeft = "C";
                    lowerRight = "D";
                    upperLeft = "B";
                    upperRight = "A";
                    currentQuestionData.correctAnswer = 4;
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
                    opt1.setBackground(new BitmapDrawable(getResources(), image1));
                    opt2.setBackground(new BitmapDrawable(getResources(), image2));
                    opt3.setBackground(new BitmapDrawable(getResources(), image3));
                    lowerLeft = "A";
                    lowerRight = "B";
                    upperLeft = "C";
                    upperRight = "NONE";
                    currentQuestionData.correctAnswer = 1;

                    mp.start();
                    break;
                case 2:
//                    opt1.setBackground(getResources().getDrawable(pic3id));
//                    opt2.setBackground(getResources().getDrawable(pic1id));
//                    opt3.setBackground(getResources().getDrawable(pic2id));
                    opt1.setBackground(new BitmapDrawable(getResources(),image3));
                    opt2.setBackground(new BitmapDrawable(getResources(), image1));
                    opt3.setBackground(new BitmapDrawable(getResources(), image2));
                    lowerLeft = "C";
                    lowerRight = "A";
                    upperLeft = "B";
                    upperRight = "NONE";
                    currentQuestionData.correctAnswer = 2;

                    mp.start();
                    break;
                case 3:
//                    opt1.setBackground(getResources().getDrawable(pic2id));
//                    opt2.setBackground(getResources().getDrawable(pic3id));
//                    opt3.setBackground(getResources().getDrawable(pic1id));
                    opt1.setBackground(new BitmapDrawable(getResources(), image2));
                    opt2.setBackground(new BitmapDrawable(getResources(), image3));
                    opt3.setBackground(new BitmapDrawable(getResources(), image1));
                    lowerLeft = "B";
                    lowerRight = "C";
                    upperLeft = "A";
                    upperRight = "NONE";


                    currentQuestionData.correctAnswer = 3;
                    mp.start();
                    break;
            }
        }


    }



    //This method computes the total number of filler items based on test size, as per the requirements given.
    //It then finds how many items we have to complete to get those filler items, and stores those values
    //in the fillerOrders.
    public void findFillerNumAndIndex(){
        if (currentQuestionData.numPracticeItems > 0) {
            //We set the filler item index to a new hashmap

            currentQuestionData.fillerOrders = new HashMap<Integer, Integer>();

            currentQuestionData.numFillerItems = 2;
            double factor = .33;
            currentQuestionData.fillerOrders.put(1, (int) Math.round(currentQuestionData.numTestItems * factor));
            currentQuestionData.fillerOrders.put(2, (int) Math.round(currentQuestionData.numTestItems * (factor * 2)));

            currentQuestionData.fillerIndex = new int[currentQuestionData.numFillerItems];
        } else {
            currentQuestionData.numFillerItems = 0;
        }
    }



    //Queries the test items for our test.
    public void getTestItemsFromDatabase(){
        currentQuestionData.testItemData = dop.getQuestionsForTest(dop, testId);
        if (continuingTest == false){
            recordID = generateRandomID();
            resultId = 1;
        } else if (continuingTest == true) {
            Cursor mostRecentRecord = dop.getMostRecentCompletionRecordbyStudentByTeacher(dop, studentId, testName, currentUserName);
            alreadyAnsweredWords = dop.getWordsAlreadyAnswered(dop, mostRecentRecord);
            recordID = mostRecentRecord.getInt(4);
            trueNumTestItemsComplete = mostRecentRecord.getInt(0);
            numQuestionsCorrect = mostRecentRecord.getInt(2);
            resultId = mostRecentRecord.getInt(0) + 1;
        }
        currentQuestionData.practiceItemData = dop.getPracItemSetsByTestID(dop, testId);

        //setting those cursors to the first item so we don't get errors.
        currentQuestionData.testItemData.moveToFirst();
        currentQuestionData.practiceItemData.moveToFirst();
    }






    //Determines the size of the cursors and uses those to set the numPracItems, numTestItems, and totalTestSize
    public void determineAndSetSizes(){
        //setting the sizes based on how many elements are in those cursors.
        currentQuestionData.numPracticeItems = currentQuestionData.practiceItemData.getCount();
        currentQuestionData.numTestItems = currentQuestionData.testItemData.getCount();
        currentQuestionData.totalTestSize = currentQuestionData.numPracticeItems + currentQuestionData.numTestItems;
    }






    //Load initial Data: Modify the currentQuestionData to load the assets needed for the test.
    public void loadInitialData(DatabaseOperations dop){


        getTestItemsFromDatabase();
        determineAndSetSizes();
        findFillerNumAndIndex();

        if (currentQuestionData.numPracticeItems > 2) {
            currentQuestionData.numPracticeItems = currentQuestionData.numPracticeItems - currentQuestionData.numFillerItems;
        }

        //Set the number of possible answers as the number of answers in the first item.
        currentQuestionData.numPosAnswer = currentQuestionData.testItemData.getInt(8);

        //Set the asset lists
        currentQuestionData.testItemAssets = new HashMap<Integer, byte[][]>();
        currentQuestionData.practiceAssets= new HashMap<Integer, byte[][]>();
        currentQuestionData.fillerAssets = new HashMap<Integer, byte[][]>();

        currentQuestionData.testWordIndex= new HashMap<Integer, String>();
        currentQuestionData.pracWordIndex = new HashMap<Integer, String>();

        if (alreadyAnsweredWords != null) {
            currentQuestionData.testItemIndex = new int[currentQuestionData.numTestItems - alreadyAnsweredWords.size()];
        } else {
            currentQuestionData.testItemIndex = new int[currentQuestionData.numTestItems];
        }
        currentQuestionData.practiceItemIndex = new int[currentQuestionData.numPracticeItems];



        //We need a random integer to store an index of our questionNum values for our test item hashmap,
        //so we can randomly retrieve that questionNumber and its assets later.
        int i = 0;
        //Here we load the test item assets from the cursors into our test item hashmap so it is ready for use.
        do {
            String whatWord = currentQuestionData.testItemData.getString(9);
            if ((alreadyAnsweredWords == null) || (alreadyAnsweredWords.get(currentQuestionData.testItemData.getString(9)) == null)) {

                //Get the number of the item that will be inserted into the hashmap.
                int questionNum = currentQuestionData.testItemData.getInt(1);

                //generate an array of byte arrays, of size 5, so we can hold up to 4 image byte arrays, plus an audio byte []
                byte[][] assetsToLoad = new byte[5][];
                assetsToLoad[0] = currentQuestionData.testItemData.getBlob(3);
                assetsToLoad[1] = currentQuestionData.testItemData.getBlob(4);
                assetsToLoad[2] = currentQuestionData.testItemData.getBlob(5);

//                image1 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[0]);
//                image2 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[1]);
//                image3 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[2]);
//                if (currentQuestionData.numPosAnswer == 4) {
//                    image4 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[3]);
//                }

                //check to see if there is a 4th item (based on the number of possible items) and load that into test.
                if (currentQuestionData.numPosAnswer == 4) {
                    assetsToLoad[3] = currentQuestionData.testItemData.getBlob(6);
                }

                //no matter what, audio goes in the last byte array.
                assetsToLoad[4] = currentQuestionData.testItemData.getBlob(2);


                //Finally, load all those assets into the testItemAssets hashmap for later use.
                currentQuestionData.testItemAssets.put(questionNum, assetsToLoad);
                currentQuestionData.testItemIndex[i] = questionNum;
                currentQuestionData.testWordIndex.put(questionNum, currentQuestionData.testItemData.getString(9));
                i++;
            }

        } while (currentQuestionData.testItemData.moveToNext());

        currentQuestionData.shuffleArray(currentQuestionData.testItemIndex);

        //Here we do the same thing, but for the practice items, checking to make sure that
        //there actually are some practice items first.
        //We also store the practiceItemNum in the appropriate indexes.
        int j = 0;
        int y = 0;
        if (currentQuestionData.practiceItemData.getCount() != 0) {

            //We need to take some of these practice items and put them in the filler item assets instead, so we
            //will first put assets into the filler assets until we have reached the appropriate number.
            int numItemsFilled = 0;
            do {
                //Get the number of the item that will be inserted into the hashmap.
                int questionNum = currentQuestionData.practiceItemData.getInt(1);

                //generate an array of byte arrays, of size 5, so we can hold up to 4 image byte arrays, plus an audio byte []
                byte[][] assetsToLoad = new byte[5][];
                assetsToLoad[0] = currentQuestionData.practiceItemData.getBlob(3);
                assetsToLoad[1] = currentQuestionData.practiceItemData.getBlob(4);
                assetsToLoad[2] = currentQuestionData.practiceItemData.getBlob(5);

                //check to see if there is a 4th item (based on the number of possible items) and load that into test.
                if (currentQuestionData.numPosAnswer == 4) {
                    assetsToLoad[3] = currentQuestionData.practiceItemData.getBlob(6);
                }

                //no matter what, audio goes in the last byte array.
                assetsToLoad[4] = currentQuestionData.practiceItemData.getBlob(2);


                //Finally, load all those assets into the practiceItemAssets hashmap for later use.
                //Depending on numItemsFilled, these go into either fillerItemsAssets or practiceItemAssets.
                if (numItemsFilled < currentQuestionData.numFillerItems){
                    currentQuestionData.fillerIndex[j] = questionNum;
                    numItemsFilled++;
                    j++;
                    currentQuestionData.fillerAssets.put(j, assetsToLoad);
                    fillerWordIndex.put(j, currentQuestionData.practiceItemData.getString(9));
                } else {
                    currentQuestionData.practiceAssets.put(questionNum, assetsToLoad);
                    currentQuestionData.practiceItemIndex[y] = questionNum;
                    y++;
                }
                currentQuestionData.pracWordIndex.put(questionNum, currentQuestionData.practiceItemData.getString(9));
            } while (currentQuestionData.practiceItemData.moveToNext());
        }

        //if the number of practice items is not equal to zero, we set the current question type
        //to practice to reflect that.
        if ((currentQuestionData.numPracticeItems > 0) && (continuingTest != true)){
            currentQuestionData.currentQtype = "Practice";
        } else {
            currentQuestionData.currentQtype = "Test";
        }


        //Lastly, we load the initial data from the lists into the current images and current audio.

//        if (currentQuestionData.currentQtype.equals("Practice")){
//            int currentNumber = currentQuestionData.practiceItemIndex[0];
//            image1 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[0]);
//            image2 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[1]);
//            image3 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[2]);
//            if (currentQuestionData.numPosAnswer == 4){
//                image4 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[3]);
//            }
//            currentAudio = currentQuestionData.practiceAssets.get(currentNumber)[4];
//        }
    }


    public void loadQuestionAssets(){
        if ((currentQuestionData.numPracticeItems != 0) && (currentQuestionData.currentQtype.equals("Practice"))) {
            if (numQuestionsComplete < currentQuestionData.numPracticeItems) {
                int currentNumber = currentQuestionData.practiceItemIndex[numQuestionsComplete];
                image1 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[0]);
                image2 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[1]);
                image3 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[2]);
                if (currentQuestionData.numPosAnswer == 4) {
                    image4 = getImage(currentQuestionData.practiceAssets.get(currentNumber)[3]);
                }
                currentAudio = currentQuestionData.practiceAssets.get(currentNumber)[4];
            }
        }


        //Need to check if the numItemsComplete is enough that we can fill in a filler item.
        //To do that, we must first check that there are some filler items,
        //if yes, then we must check to see how many,
        //if yes, then we must check to see if we have completed enough items to get a filler item.
         else if (currentQuestionData.numFillerItems != 0){
            if (currentQuestionData.numFillerItems == 1){

                if ((numTestItemsComplete == currentQuestionData.fillerOrders.get(1)) && (fillerItemComplete == false)){
                    currentQuestionData.currentQtype = "Filler";
                    image1 = getImage(currentQuestionData.fillerAssets.get(1)[0]);
                    image2 = getImage(currentQuestionData.fillerAssets.get(1)[1]);
                    image3 = getImage(currentQuestionData.fillerAssets.get(1)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.fillerAssets.get(1)[3]);
                    }
                    currentAudio = currentQuestionData.fillerAssets.get(1)[4];
                } else {
                    currentQuestionData.currentQtype = "Test";
                    int currentNumber = currentQuestionData.testItemIndex[numTestItemsComplete];
                    image1 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[0]);
                    image2 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[1]);
                    image3 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[3]);
                    }
                    currentAudio = currentQuestionData.testItemAssets.get(currentNumber)[4];
                }
            }

            if (currentQuestionData.numFillerItems == 2){

                if ((trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(1)) && (fillerItemComplete == false)) {
                    currentQuestionData.currentQtype = "Filler";
                    image1 = getImage(currentQuestionData.fillerAssets.get(1)[0]);
                    image2 = getImage(currentQuestionData.fillerAssets.get(1)[1]);
                    image3 = getImage(currentQuestionData.fillerAssets.get(1)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.fillerAssets.get(1)[3]);
                    }
                    currentAudio = currentQuestionData.fillerAssets.get(1)[4];
                } else if ((trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(2)) && (fillerItemComplete == false)){
                    currentQuestionData.currentQtype = "Filler";
                    image1 = getImage(currentQuestionData.fillerAssets.get(2)[0]);
                    image2 = getImage(currentQuestionData.fillerAssets.get(2)[1]);
                    image3 = getImage(currentQuestionData.fillerAssets.get(2)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.fillerAssets.get(2)[3]);
                    }
                    currentAudio = currentQuestionData.fillerAssets.get(2)[4];
                } else {
                    currentQuestionData.currentQtype = "Test";
                    int currentNumber = currentQuestionData.testItemIndex[numTestItemsComplete];
                    image1 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[0]);
                    image2 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[1]);
                    image3 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[3]);
                    }
                    currentAudio = currentQuestionData.testItemAssets.get(currentNumber)[4];
                }
            }

            if (currentQuestionData.numFillerItems == 3){
                if ((trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(1)) && (fillerItemComplete == false)) {
                    currentQuestionData.currentQtype = "Filler";
                    image1 = getImage(currentQuestionData.fillerAssets.get(1)[0]);
                    image2 = getImage(currentQuestionData.fillerAssets.get(1)[1]);
                    image3 = getImage(currentQuestionData.fillerAssets.get(1)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.fillerAssets.get(1)[3]);
                    }
                    currentAudio = currentQuestionData.fillerAssets.get(1)[4];
                } else if ((trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(2)) && (fillerItemComplete == false)){
                    currentQuestionData.currentQtype = "Filler";
                    image1 = getImage(currentQuestionData.fillerAssets.get(2)[0]);
                    image2 = getImage(currentQuestionData.fillerAssets.get(2)[1]);
                    image3 = getImage(currentQuestionData.fillerAssets.get(2)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.fillerAssets.get(2)[3]);
                    }
                    currentAudio = currentQuestionData.fillerAssets.get(2)[4];
                } else if ((trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(3)) && (fillerItemComplete == false)){
                    currentQuestionData.currentQtype = "Filler";
                    image1 = getImage(currentQuestionData.fillerAssets.get(3)[0]);
                    image2 = getImage(currentQuestionData.fillerAssets.get(3)[1]);
                    image3 = getImage(currentQuestionData.fillerAssets.get(3)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.fillerAssets.get(3)[3]);
                    }
                    currentAudio = currentQuestionData.fillerAssets.get(3)[4];
                } else {
                    currentQuestionData.currentQtype = "Test";
                    int currentNumber = currentQuestionData.testItemIndex[numTestItemsComplete];
                    image1 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[0]);
                    image2 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[1]);
                    image3 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[2]);
                    if (currentQuestionData.numPosAnswer == 4) {
                        image4 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[3]);
                    }
                    currentAudio = currentQuestionData.testItemAssets.get(currentNumber)[4];
                }
            }
        }
        else {
            currentQuestionData.currentQtype = "Test";
            int currentNumber = currentQuestionData.testItemIndex[numTestItemsComplete];
            image1 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[0]);
            image2 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[1]);
            image3 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[2]);
            if (currentQuestionData.numPosAnswer == 4) {
                image4 = getImage(currentQuestionData.testItemAssets.get(currentNumber)[3]);
            }
            currentAudio = currentQuestionData.testItemAssets.get(currentNumber)[4];

        }
    }




//    public Bitmap byteArrayToBitmap(byte[] byteArray){
//        Bitmap background = BitmapFactory.decodeByteArray(byteArray, 0,
//                byteArray.length);
//        Bitmap back = Bitmap.createBitmap(background.getWidth(),
//                background.getHeight(), Bitmap.Config.ARGB_8888);
//        return back;
//    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }



























    //Message that appears when user answers incorrectly.
    public void tryAgainMessage() {
        mp = MediaPlayer.create(this, R.raw.tryagain);
        mp.start();
    }

    //Behavior that is triggered when a user selects the wrong answer in the practice questions.
    public void incorrectAnswer(){
        currentQuestionData.attempts++;
        if (currentQuestionData.attempts >= 2){
            playMp3(currentAudio);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    tryAgainMessage();
                }
            });
        } else {
            tryAgainMessage();
        }
        audioLoop();

    }

    public void audioLoop(){
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (play == true) {
                    playMp3(currentAudio);
                }
            }
        }, 10000);

    }

    //Behavior that is triggered when the user successfully completes a second practice currentQuestionData.
    public void transitionToTestItems(){
        disableButtons();
        mp.release();
        play = false;
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
                play = true;
            }
        });
    }

    //Behavior that is triggered when the user chooses the correct button. Either
    //the user will continue to the next currentQuestionData, they will have finished the test,
    //or they will have finished the practice questions.
    public void processAnswer(){
        if (practiceMode == false) {
            dop.updateTestCompletionRecord(dop, recordID, numQuestionsCorrect, trueNumTestItemsComplete);
        }
        //Code to add +1 to pracCorrect if attempts = 0
        //so that when user gets two practice questions right on their first attempt
        //they are forwarded to the first real practice item regardless of rather
        //they finished all the practice questions.
        numQuestionsComplete++;
        if  ((currentQuestionData.currentQtype == "Practice") && (currentQuestionData.attempts == 0)){
            currentQuestionData.pracCorrect++;
        }
        currentQuestionData.attempts = 0;

        if (trueNumTestItemsComplete == currentQuestionData.numTestItems) {
            //go to last activity
            //RECORD RESULTS HERE
//            dop.addNewTestCompletionRecord(dop, generateRandomID(), studentId, studentName, testId,
//                    testName, numQuestions, numQuestionsCorrect, numQuestionsComplete);

            currentQuestionData.oldData = true;
            handler.removeCallbacksAndMessages(null);
            startActivity(proceedIntent);
            this.finish();
        } else if (((numQuestionsComplete == currentQuestionData.numPracticeItems) && (currentQuestionData.currentQtype.equals("Practice")))
                || ((currentQuestionData.pracCorrect == 2) && (currentQuestionData.currentQtype.equals("Practice")))){
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





//    switch (n){
//        case 1 :
////                    opt1.setBackground(getResources().getDrawable(pic1id));
////                    opt2.setBackground(getResources().getDrawable(pic2id));
////                    opt3.setBackground(getResources().getDrawable(pic3id));
////                    opt4.setBackground(getResources().getDrawable(pic4id));
//
//            opt1.setBackground(new BitmapDrawable(getResources(), image1));
//            opt2.setBackground(new BitmapDrawable(getResources(), image2));
//            opt3.setBackground(new BitmapDrawable(getResources(), image3));
//            opt4.setBackground(new BitmapDrawable(getResources(), image4));
//            currentQuestionData.correctAnswer = 1;
//            mp.start();
//            break;
//        case 2:
////                    opt1.setBackground(getResources().getDrawable(pic4id));
////                    opt2.setBackground(getResources().getDrawable(pic3id));
////                    opt3.setBackground(getResources().getDrawable(pic1id));
////                    opt4.setBackground(getResources().getDrawable(pic2id));
//            opt1.setBackground(new BitmapDrawable(getResources(), image4));
//            opt2.setBackground(new BitmapDrawable(getResources(), image3));
//            opt3.setBackground(new BitmapDrawable(getResources(), image1));
//            opt4.setBackground(new BitmapDrawable(getResources(), image2));
//            currentQuestionData.correctAnswer = 3;
//            mp.start();
//            break;
//        case 3:
////                    opt1.setBackground(getResources().getDrawable(pic2id));
////                    opt2.setBackground(getResources().getDrawable(pic1id));
////                    opt3.setBackground(getResources().getDrawable(pic4id));
////                    opt4.setBackground(getResources().getDrawable(pic3id));
//            opt1.setBackground(new BitmapDrawable(getResources(), image2));
//            opt2.setBackground(new BitmapDrawable(getResources(), image1));
//            opt3.setBackground(new BitmapDrawable(getResources(), image3));
//            opt4.setBackground(new BitmapDrawable(getResources(), image4));
//            currentQuestionData.correctAnswer = 2;
//            mp.start();
//            break;
//        case 4:
////                    opt1.setBackground(getResources().getDrawable(pic3id));
////                    opt2.setBackground(getResources().getDrawable(pic4id));
////                    opt3.setBackground(getResources().getDrawable(pic2id));
////                    opt4.setBackground(getResources().getDrawable(pic1id));
//            opt1.setBackground(new BitmapDrawable(getResources(), image3));
//            opt2.setBackground(new BitmapDrawable(getResources(), image4));
//            opt3.setBackground(new BitmapDrawable(getResources(), image2));
//            opt4.setBackground(new BitmapDrawable(getResources(), image1));





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

            int questionNum = 0;
            String questionWord = null;
            if (currentQuestionData.currentQtype.equalsIgnoreCase("Test")){
                questionNum = currentQuestionData.testItemIndex[numTestItemsComplete];
                questionWord = currentQuestionData.testWordIndex.get(questionNum);
                trueNumTestItemsComplete++;
                if (currentQuestionData.numFillerItems == 2){
                    if ((trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(1)) ||
                            (trueNumTestItemsComplete == currentQuestionData.fillerOrders.get(2))){
                        fillerItemComplete = false;
                    }
                }

            } else if (currentQuestionData.currentQtype.equals("Practice")){
                questionNum = currentQuestionData.practiceItemIndex[numQuestionsComplete];
                questionWord = currentQuestionData.pracWordIndex.get(questionNum);
            } else if (currentQuestionData.currentQtype.equals("Filler")){
                fillerItemComplete = true;
                if (numTestItemsComplete == currentQuestionData.fillerOrders.get(1)){
                    questionNum = currentQuestionData.fillerIndex[0];
                    questionWord = fillerWordIndex.get(questionNum);
                } else if ((currentQuestionData.fillerOrders.get(2) != null) && (numTestItemsComplete == currentQuestionData.fillerOrders.get(2))){
                    questionNum = currentQuestionData.fillerIndex[1];
                    questionWord = fillerWordIndex.get(questionNum);
                } else if ((currentQuestionData.fillerOrders.get(3) != null) && (numTestItemsComplete == currentQuestionData.fillerOrders.get(3))) {
                    questionNum = currentQuestionData.fillerIndex[2];
                    questionWord = fillerWordIndex.get(questionNum);
                }
            }



            String selectedAnswer = "";
            switch (view.getId()) {

                case R.id.opt1:
                    if (numPosAnswers == 4) {
                        switch (n) {
                            case 1:
                                selectedAnswer = "A";
                                break;
                            case 2:
                                selectedAnswer = "D";
                                break;

                            case 3:
                                selectedAnswer = "B";
                                break;

                            case 4:
                                selectedAnswer = "C";
                                break;

                        }
                    } else if (numPosAnswers == 3) {
                        switch (n) {
                            case 1:
                                selectedAnswer = "A";
                                break;
                            case 2:
                                selectedAnswer = "C";
                                break;

                            case 3:
                                selectedAnswer = "B";
                                break;


                        }
                    }
                    if (currentQuestionData.correctAnswer == 1){
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    true, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, "A", currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numQuestionsCorrect++;
                        }
                        resultId++;
                    } else {
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        if (currentQuestionData.currentQtype.equalsIgnoreCase("Filler")){
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);

                        }
                        resultId++;
                    }

                    if ((currentQuestionData.currentQtype.equals("Filler") || (currentQuestionData.correctAnswer == 1) || (currentQuestionData.currentQtype.equals("Test")))) {
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numTestItemsComplete++;
                        }
                        processAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt2:
                    if (numPosAnswers == 4) {
                        switch (n) {
                            case 1:
                                selectedAnswer = "B";
                                break;
                            case 2:
                                selectedAnswer = "C";
                                break;

                            case 3:
                                selectedAnswer = "A";
                                break;

                            case 4:
                                selectedAnswer = "D";
                                break;

                        }
                    } else if (numPosAnswers == 3){
                        switch (n) {
                            case 1:
                                selectedAnswer = "B";
                                break;
                            case 2:
                                selectedAnswer = "A";
                                break;

                            case 3:
                                selectedAnswer = "C";
                                break;
                        }

                    }
                    if (currentQuestionData.correctAnswer == 2){
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    true, testId, testName, resultId, recordID, studentId, studentName,
                                    currentQuestionData.currentQtype, "A", currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight );
                        }
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numQuestionsCorrect++;
                        }
                        resultId++;
                    } else {
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        if (currentQuestionData.currentQtype.equalsIgnoreCase("Filler")){
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);

                        }
                        resultId++;
                    }


                    if ((currentQuestionData.currentQtype.equals("Filler") || (currentQuestionData.correctAnswer == 2) || (currentQuestionData.currentQtype.equals("Test")))) {
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numTestItemsComplete++;
                        }
                        processAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt3:
                    if (numPosAnswers == 4) {
                        switch (n) {
                            case 1:
                                selectedAnswer = "C";
                                break;
                            case 2:
                                selectedAnswer = "A";
                                break;

                            case 3:
                                selectedAnswer = "C";
                                break;

                            case 4:
                                selectedAnswer = "B";
                                break;

                        }
                    } else if (numPosAnswers == 3) {
                        switch (n) {
                            case 1:
                                selectedAnswer = "C";
                                break;
                            case 2:
                                selectedAnswer = "B";
                                break;

                            case 3:
                                selectedAnswer = "A";
                                break;

                        }
                    }
                    if (currentQuestionData.correctAnswer == 3){
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    true, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, "A", currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numQuestionsCorrect++;
                        }
                        resultId++;
                    } else {
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        if (currentQuestionData.currentQtype.equalsIgnoreCase("Filler")){
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);

                        }
                        resultId++;
                    }

                    if ((currentQuestionData.currentQtype.equals("Filler") || (currentQuestionData.correctAnswer == 3) || (currentQuestionData.currentQtype.equals("Test")))) {
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numTestItemsComplete++;
                        }
                        processAnswer();
                    } else {
                        incorrectAnswer();
                    }
                    break;
                case R.id.opt4:
                    switch (n){
                        case 1:
                            selectedAnswer = "D";
                            break;
                        case 2:
                            selectedAnswer = "B";
                            break;

                        case 3:
                            selectedAnswer = "D";
                            break;

                        case 4:
                            selectedAnswer = "A";
                            break;

                    }
                    if (currentQuestionData.correctAnswer == 4){
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    true, testId, testName, resultId, recordID, studentId, studentName,currentQuestionData.currentQtype, "A", currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        resultId++;
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numQuestionsCorrect++;
                        }
                    } else {
                        if (currentUserData.isPracticeMode() == false) {
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);
                        }
                        if (currentQuestionData.currentQtype.equalsIgnoreCase("Filler")){
                            dop.addNewResult(dop, questionNum,
                                    questionWord,
                                    false, testId, testName, resultId, recordID, studentId, studentName, currentQuestionData.currentQtype, selectedAnswer, currentUserName, trueNumTestItemsComplete, lowerLeft, lowerRight, upperLeft, upperRight);

                        }
                        resultId++;
                    }

                    if ((currentQuestionData.currentQtype.equals("Filler") || (currentQuestionData.correctAnswer == 4) || (currentQuestionData.currentQtype.equals("Test")))) {
                        if (currentQuestionData.currentQtype.equals("Test")) {
                            numTestItemsComplete++;
                        }
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
                Uri.parse("android-app://com.example.nick.ProgressMonitoringTool/http/host/path")
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
                Uri.parse("android-app://com.example.nick.ProgressMonitoringTool/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
