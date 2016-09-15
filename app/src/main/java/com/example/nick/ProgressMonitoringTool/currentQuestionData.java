package com.example.nick.ProgressMonitoringTool;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Nick on 6/23/2016.
 */


//This class is part of the model component of the model-view-controller design.

    /*This class is responsible for holding the current status of the questions in the app.
    * such as which currentQuestionData we are on. In addition, once we have actually loaded a test into the app,
    * it holds the assets for that test, such as the audio and images.
     *
     * The class employs a high degree of encapsulation.*/


public class currentQuestionData {

    //Int that says how many items are going to be displayed to the user.
    public int numPosAnswer;

    //The current type of question, either practice or test.
    public String currentQtype;

    //How many questions the user has completed, thus determining what kind of question
    //to display to them next, or rather they have ended the test or not.
    public int currentIndex;

    //integer attempts describes how many times a user has incorrectly answer so we know when to play
    //the word audio again (on the 3rd attempt)and we know when a user has gotten a currentQuestionData
    //correct on the first attempt.
    public int attempts;

    //Each question stores its assets in an array of byte arrays, so in order to store that data,
    //we must have an array of questions, so, an array of byte arrays.
    public HashMap<Integer, byte[][]> testItemAssets;


    //Beacuse we need to randomly select from those testItems, we need a corresponding arrayList of ints
    //which will store the key of the testItemAssets byte[][], so we can randomly retrieve them.

    //We also create indexs for the filler and practice items, though we will access those arrays in a fixed
    //order, rather than randomly. The filler item index is a hashmap, beacuse we know we will be
    //accessing the 1st, and then the 2nd and 3rd filler items if the test is large enough.
    //The practice index is an arrayList beacuse we do not know how many practice items we will be accessing.


    public int[] testItemIndex;

    public int[] practiceItemIndex;

    public int[] fillerIndex;

    public HashMap<Integer, String> testWordIndex;
    public HashMap<Integer, String> pracWordIndex;

    //Each practice question stores its assets the same way.
    public HashMap<Integer, byte[][]> practiceAssets;

    //Filler items are stored the same way, but they are transferred over from the practice assets.
    public HashMap<Integer, byte[][]> fillerAssets;

    //This stores numbers representing when the filler items will be displayed to the user
    public HashMap<Integer, Integer> fillerOrders;

    //This int is obtained from the userData to figure out what questions we need to load.
    public int testID;

    //The number of practice items which will be given in the test, obtained by getting the size of the cursor of practice items.
    public int numPracticeItems;

    //The number of filler items which will be given in the test. Is determined by the size of the test.
    public int numFillerItems;


    //The total number of regular items; obtained by getting the size of the cursor of test items.
    public int numTestItems;


    //The sum of numPracticeItems and numTestItems
    public int totalTestSize;

    //int to keep track of how many practice questions have been answered correctly
    //so we know when to move to next question.
    public int pracCorrect;



    //Cursors of the data for the regular test items and the practice items that we have obtained from the database.
    public Cursor testItemData;
    public Cursor practiceItemData;

    //A boolean to say if we just loaded the first question so we know not to do certain behaviors.
    public boolean firstQuestion;

    public DatabaseOperations dop;



    //The following boolean indicates if the currentQuestion data is new or if it has
    //been used before; if it has been used before, the data will be wiped and
    //constructed anew.
    public boolean oldData;

    public int correctAnswer;





    public static currentQuestionData getInstance() {return CURRENT_QUESTION_DATA;}

    //SINGLETON pattern: the class is instantiated here so
    // that it is ready to hold data when we initialize it in another class.
    // It is first initialized in the login screen.java class.
    private static final currentQuestionData CURRENT_QUESTION_DATA =
            new currentQuestionData(null, null, null, 0, null, null,false, 3, "Practice", 0, 0, null, null, null, null, 0, 0, 0, 0, 0, 0, null, null, true, null);



    public currentQuestionData(HashMap<Integer, String> testWordIndex, HashMap<Integer, String> pracWordIndex, int[] fillerIndex, int correctAnswer, int[] practiceItemIndex, int[] testItemIndex, boolean oldData, int numPosAnswer, String currentQtype, int currentIndex, int attempts, HashMap<Integer, byte[][]> questionAssets, HashMap<Integer, byte[][]> practiceAssets, HashMap<Integer, byte[][]> fillerAssets, HashMap<Integer, Integer> fillerIndexes, int testID, int numPracticeItems, int numFillerItems, int testSize, int numPractice, int pracCorrect, Cursor questionData, Cursor practiceItemData, boolean firstQuestion, DatabaseOperations dop) {
        this.testWordIndex = testWordIndex;
        this.pracWordIndex = pracWordIndex;
        this.fillerIndex = fillerIndex;
        this.correctAnswer = correctAnswer;
        this.practiceItemIndex = practiceItemIndex;
        this.testItemIndex = testItemIndex;
        this.oldData = oldData;
        this.numPosAnswer = numPosAnswer;
        this.currentQtype = currentQtype;
        this.currentIndex = currentIndex;
        this.attempts = attempts;
        this.testItemAssets = questionAssets;
        this.practiceAssets = practiceAssets;
        this.fillerAssets = fillerAssets;
        this.fillerOrders = fillerIndexes;
        this.testID = testID;
        this.numPracticeItems = numPracticeItems;
        this.numFillerItems = numFillerItems;
        this.totalTestSize = testSize;
        this.numPracticeItems = numPractice;
        this.pracCorrect = pracCorrect;
        this.testItemData = questionData;
        this.practiceItemData = practiceItemData;
        this.firstQuestion = firstQuestion;
        this.dop = dop;
    }







//    public Bitmap[] fetchBitmaps(Cursor CR, int questionNum){
//        Bitmap[] bitmaps = new Bitmap[4];
//        CR.moveToFirst();
//        byte[] pic1Bytes = CR.getBlob(3);
//        byte[] pic2Bytes = CR.getBlob(4);
//        byte[] pic3Bytes = CR.getBlob(5);
//        byte[] pic4Bytes = CR.getBlob(6);
//        bitmaps[0] = getDop().getImage(pic1Bytes);
//        bitmaps[1] = getDop().getImage(pic2Bytes);
//        bitmaps[2] = getDop().getImage(pic3Bytes);
//        bitmaps[3] = getDop().getImage(pic4Bytes);
//        return bitmaps;
//
//    }

//
//    //This array takes the number of practice items and creates
//    //a seperate array with that number of spaces
//    //then copies the proper number of elements from the questionList
//    //to the practiceList.
//    public void getPracticeArray(){
//        //copies the proper number of currentQuestionData numbers from the currentQuestionData List
//        //to the practice list depending on the amount of practice items.
//
//        setPracticeList(new int[getNumPracticeItems()]);
//        System.out.println("The number of practice items is " + getNumPracticeItems());
//        System.arraycopy(getQuestionList(), 0, getPracticeList(),
//                0, getNumPracticeItems());
//        System.out.println("Practice list: " + Arrays.toString(getPracticeList()));
//    }


//    private void getNonPracticeItemsList(){
//        int[] newQuestionList = new int[getQuestionList().length - getNumPracticeItems()];
//        System.arraycopy(getQuestionList(), getNumPracticeItems(), // from array[removeEnd]
//                newQuestionList, 0,            // to array[removeStart]
//                getQuestionList().length - getNumPracticeItems());
//        setQuestionList(newQuestionList);
//        System.out.println("Test list before shuffle: " + Arrays.toString(getQuestionList()));
//    }


    public static void shuffleArray(int[] ar)
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





    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }











}
