package com.example.nick.rapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Arrays;
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

    private int correctAnswer;
    private int currentSelection;
    private int numPosAnswer;
    private String currentQtype;
    private int currentQIndex;
    private int currentQNum;
    private int pracItemID;



    //integer attempts describes how many times a user has incorrectly answer so we know when to play
    //the word audio again (on the 3rd attempt)and we know when a user has gotten a currentQuestionData
    //correct on the first attempt.
    private int attempts;


    private byte[][] audioList;
    private Bitmap[][] imageList;

    private int testID;

    private int numPracticeItems;


    //these two arrays hold the randomized integer arrays that determine the currentQuestionData order.
    private int[] practiceList;
    private int[] questionList;

    //The following arrays will hold the assets obtained from the SQLite database so we can
    //do all the database interaction at one point.
    private ArrayList<ArrayList> bmp_images = new ArrayList<ArrayList>();
    private ArrayList<byte[]> audio = new ArrayList<byte[]>();


    //THE FOLLOWING VALUE IS HARDCODED BELOW TO REFLECT THE NUMBER OF EXAMPLE QUESTIONS
    //IN FINAL APP IT WILL REFLECT THE NUMBER OF QUESTIONS IN A POSSIBLE TEST
    //AND WILL BE OBTAINED VIA QUERY FROM THE DATABASE
    private int testSize;

    //This value indicates how many practice items are in the test
    private int numPractice;

    //int to keep track of how many practice questions have been answered correctly
    //so we know when to move to next question.
    private int pracCorrect;




    private Cursor questionData;
    private Cursor practiceItemData;

    private boolean firstQuestion;

    private Bitmap pic1Bitmap;
    private Bitmap pic2Bitmap;
    private Bitmap pic3Bitmap;
    private Bitmap pic4Bitmap;
    private byte[] audioBytes;

    private DatabaseOperations dop;

    //The following two hashmaps keep track of what the questionID's and
    //words so that we can use those values
    //when we are inserting new results.
    private HashMap<Integer, String> wordIndex;
    private HashMap<Integer, Integer> questionIdIndex;
    private HashMap<Integer, Integer> pracIdIndex;


    //The following boolean indicates if the currentQuestion data is new or if it has
    //been used before; if it has been used before, the data will be wiped and
    //constructed anew.
    private boolean oldData;





    public currentQuestionData(HashMap<Integer, String> wordIndex,
                               HashMap<Integer, Integer> questionIdIndex,
                               HashMap<Integer, Integer> pracIdIndex,
                               int currentQNum, int correctAnswer, int currentSelection,
                               int numPosAnswer, String currentQtype, int currentQIndex,
                               int attempts, byte[][] audioList, Bitmap[][] imageList,
                               int testID, int numPracticeItems, DatabaseOperations dop,
                               int[] practiceList, int[] questionList, ArrayList<ArrayList> bmp_images,
                               ArrayList<byte[]> audio, int testSize, int numPractice, Cursor questionData,
                               Bitmap pic1Bitmap,
                               Bitmap pic2Bitmap,
                               Bitmap pic3Bitmap,
                               Bitmap pic4Bitmap,
                               byte[] audioBytes, boolean firstQuestion, int pracCorrect,
                               Cursor practiceItemData, int pracItemID, boolean oldData, DatabaseOperations databaseOperations) {

        this.wordIndex = wordIndex;
        this.questionIdIndex = questionIdIndex;
        this.pracIdIndex = pracIdIndex;
        this.setCurrentQNum(currentQNum);
        this.setCorrectAnswer(correctAnswer);
        this.setCurrentSelection(currentSelection);
        this.setNumPosAnswer(numPosAnswer);
        this.setCurrentQtype(currentQtype);
        this.setCurrentQIndex(currentQIndex);
        this.setAttempts(attempts);
        this.setAudioList(audioList);
        this.setImageList(imageList);
        this.setTestID(testID);
        this.setNumPracticeItems(numPracticeItems);
        this.setDop(dop);
        this.setPracticeList(practiceList);
        this.setQuestionList(questionList);
        this.setBmp_images(bmp_images);
        this.setAudio(audio);
        this.setTestSize(testSize);
        this.setNumPractice(numPractice);
        this.setQuestionData(questionData);
        this.setPic1Bitmap(pic1Bitmap);
        this.setPic2Bitmap(pic2Bitmap);
        this.setPic3Bitmap(pic3Bitmap);
        this.setPic4Bitmap(pic4Bitmap);
        this.setAudioBytes(audioBytes);
        this.setFirstQuestion(firstQuestion);
        this.setPracCorrect(pracCorrect);
        this.setDop(databaseOperations);
        this.setPracticeItemData(practiceItemData);
        this.setPracItemID(pracItemID);
        this.setOldData(oldData);

    }

    //SINGLETON pattern: the class is instantiated here so
    // that it is ready to hold data when we initialize it in another class.
    // It is first initialized in the login screen.java class.
    private static final currentQuestionData CURRENT_QUESTION_DATA =
            new currentQuestionData(null, null, null, 0, 0, 0, 0, null, 0, 0, null, null, 0, 0, null, null, null, null, null, 0, 0, null,
                                    null, null, null, null, null, true, 0, null, 0, false, null);

    public HashMap<Integer, Integer> getPracIdIndex() {
        return pracIdIndex;
    }

    public void setPracIdIndex(HashMap<Integer, Integer> pracIdIndex) {
        this.pracIdIndex = pracIdIndex;
    }

    public int getPracItemID() {
        return pracItemID;
    }

    public void setPracItemID(int pracItemID) {
        this.pracItemID = pracItemID;
    }

    public boolean isOldData() {
        return oldData;
    }

    public void setOldData(boolean oldData) {
        this.oldData = oldData;
    }

    //    public currentQuestionData(int correctAnswer, int currentSelection,
//                               int numPosAnswer, String currentQtype, int currentIndex
//                                , int testSize, int numPractice, int[] practiceList, int[] questionList,
//                               ArrayList<ArrayList> images) {
//        this.correctAnswer = correctAnswer;
//        this.currentSelection = currentSelection;
//        this.numPosAnswer = numPosAnswer;
//        this.currentQtype = currentQtype;
//        this.currentQIndex = currentIndex;
//        this.testSize = testSize;
//        this.numPractice = numPractice;
//        this.practiceList = practiceList;
//        this.questionList = questionList;
//        this.bmp_images = images;
//
//    }


    public Cursor getPracticeItemData() {
        return practiceItemData;
    }

    public void setPracticeItemData(Cursor practiceItemData) {
        this.practiceItemData = practiceItemData;
    }

    public HashMap<Integer, String> getWordIndex() {
        return wordIndex;
    }

    public void setWordIndex(HashMap<Integer, String> wordIndex) {
        this.wordIndex = wordIndex;
    }


    public HashMap<Integer, Integer> getQuestionIdIndex() {
        return questionIdIndex;
    }

    public void setQuestionIdIndex(HashMap<Integer, Integer> questionIdIndex) {
        this.questionIdIndex = questionIdIndex;
    }

    public int getPracCorrect() {
        return pracCorrect;
    }

    public void setPracCorrect(int pracCorrect) {
        this.pracCorrect = pracCorrect;
    }

    public boolean getFirstQuestion(){
        return this.isFirstQuestion();
    }

    public void setFirstQuestion(Boolean value ){
        this.firstQuestion = value;
    }

    public int getCurrentQNum() {
        return currentQNum;
    }

    public void setCurrentQNum(int currentQNum) {
        this.currentQNum = currentQNum;
    }

    public ArrayList<ArrayList> getBmp_images() {
        return bmp_images;
    }

    public void setBmp_images(ArrayList<Bitmap> images, int questionIndex) {
        this.getBmp_images().set(questionIndex, images);
    }

    public ArrayList<byte[]> getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio, int questionNum) {
        this.getAudio().set(questionNum, audio);
    }

    public boolean isFirstQuestion() {
        return firstQuestion;
    }

    public void setFirstQuestion(boolean firstQuestion) {
        this.firstQuestion = firstQuestion;
    }

    public Bitmap getPic1Bitmap() {
        return pic1Bitmap;
    }

    public void setPic1Bitmap(Bitmap pic1Bitmap) {
        this.pic1Bitmap = pic1Bitmap;
    }

    public Bitmap getPic2Bitmap() {
        return pic2Bitmap;
    }

    public void setPic2Bitmap(Bitmap pic2Bitmap) {
        this.pic2Bitmap = pic2Bitmap;
    }

    public Bitmap getPic3Bitmap() {
        return pic3Bitmap;
    }

    public void setPic3Bitmap(Bitmap pic3Bitmap) {
        this.pic3Bitmap = pic3Bitmap;
    }

    public Bitmap getPic4Bitmap() {
        return pic4Bitmap;
    }

    public void setPic4Bitmap(Bitmap pic4Bitmap) {
        this.pic4Bitmap = pic4Bitmap;
    }

    public byte[] getAudioBytes() {
        return audioBytes;
    }

    public void setAudioBytes(byte[] audioBytes) {
        this.audioBytes = audioBytes;
    }

    public int getNumPractice() {
        return numPractice;
    }

    public void setNumPractice(int numPractice) {
        this.numPractice = numPractice;
    }

    public int[] getPracticeList() {
        return practiceList;
    }

    public void setPracticeList(int[] practiceList) {
        this.practiceList = practiceList;
    }

    public int[] getQuestionList() {
        return questionList;
    }

    public void setQuestionList(int[] questionList) {
        this.questionList = questionList;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public byte[][] getAudioList() {
        return audioList;
    }

    public void setAudioList(byte[][] audioList) {
        this.audioList = audioList;
    }

    public Bitmap[][] getImageList() {
        return imageList;
    }

    public void setImageList(Bitmap[][] imageList) {
        this.imageList = imageList;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

//    public int getNumPracticeItems() {
//        return numPracticeItems;
//    }

    public void setNumPracticeItems(int numPracticeItems) {
        this.numPracticeItems = numPracticeItems;
    }

    public DatabaseOperations getDop() {
        return dop;
    }

    public void setDop(DatabaseOperations dop) {
        this.dop = dop;
    }

    public void setBmp_images(ArrayList<ArrayList> bmp_images) {
        this.bmp_images = bmp_images;
    }

    public void setAudio(ArrayList<byte[]> audio) {
        this.audio = audio;
    }

    public Cursor getQuestionData() {
        return questionData;
    }

    public void setQuestionData(Cursor questionData) {
        this.questionData = questionData;
    }

    public static currentQuestionData getCurrentQuestionData() {
        return CURRENT_QUESTION_DATA;
    }

    public static currentQuestionData getInstance() {return getCurrentQuestionData();}

    public int getTestSize() {
        return testSize;
    }

    public void setTestSize(int testSize) {
        this.testSize = testSize;
    }

    public int getQuestionNum(){
        return this.getCurrentQIndex();
    }

    public void setQuestionNum(int index) {
        this.setCurrentQIndex(index); }



    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getCurrentSelection() {
        return currentSelection;
    }

    public void setCurrentSelection(int currentSelection) {
        this.currentSelection = currentSelection;
    }

    public int getNumPosAnswer() {
        return numPosAnswer;
    }

    public void setNumPosAnswer(int numPosAnswer) {
        this.numPosAnswer = numPosAnswer;
    }

    public String getCurrentQtype() {
        return currentQtype;
    }

    public void setCurrentQtype(String currentQtype) {
        this.currentQtype = currentQtype;
    }




    public int getCurrentQIndex() {
        return currentQIndex;
    }

    public void setCurrentQIndex(int currentQIndex) {
        this.currentQIndex = currentQIndex;
    }


    // boolean nextQEmpty?

    public void loadInitialData(DatabaseOperations dop){
        //Fetching the actual data from the database

        setQuestionData(dop.getQuestionsForTest(dop, testID));
        getQuestionData().moveToFirst();

        setPracticeItemData(dop.getPracItemSetsByTestID(dop, testID));

        setNumPractice(getPracticeItemData().getCount());

        setNumPosAnswer(getQuestionData().getInt(8));
        setTestSize(getQuestionData().getCount());

        wordIndex = new HashMap<Integer, String>();
        questionIdIndex = new HashMap<Integer, Integer>();
        pracIdIndex = new HashMap<Integer, Integer>();


        //Initializing lists to store questionIndexs and questionAssets
        //audioList needs to be larger than the test size
        setQuestionList(new int[getTestSize()]);
        setPracticeList(new int[getNumPractice()]);
        setAudioList(new byte[getTestSize() + getNumPractice() + 1][]);
        setImageList(new Bitmap[getTestSize() + getNumPractice() + 1][4]);

        //Creating indexes for each currentQuestionData, starting with 1, up to testSize
        int j = 1;
        for (int i = 0; i < getTestSize(); i++){
            getQuestionList()[i] = j;
            j++;
        }

        int y = 1;
        for (int i = 0; i < getNumPractice(); i++){
            getPracticeList()[i] = y;
            y++;
        }

        //inserting the assets into lists so they are ready to be used.
        getPracticeItemData().moveToFirst();
        getQuestionData().moveToFirst();
        int i = 1;
        do {
            questionIdIndex.put(getQuestionData().getInt(1), i);
            wordIndex.put(i, getQuestionData().getString(9));
            audioList[i] = getQuestionData().getBlob(2);
            byte[] audioBytes = getQuestionData().getBlob(2);
            imageList[i][0] = getImage((getQuestionData().getBlob(3)));
            imageList[i][1] = getImage(getQuestionData().getBlob(4));
            imageList[i][2] = getImage(getQuestionData().getBlob(5));

            if (numPosAnswer == 4) {
                getImageList()[i][3] = getImage(getQuestionData().getBlob(6));
            }
            i++;
        } while (getQuestionData().moveToNext());

        if (getPracticeItemData().getCount() != 0) {
            int practiceIndex = 1;
            do {
                pracIdIndex.put(practiceIndex, i);
                wordIndex.put(i, getPracticeItemData().getString(9));
                audioList[i] = getPracticeItemData().getBlob(2);
                byte[] audioBytes = getPracticeItemData().getBlob(2);
                imageList[i][0] = getImage(getPracticeItemData().getBlob(3));
                imageList[i][1] = getImage(getPracticeItemData().getBlob(4));
                imageList[i][2] = getImage(getPracticeItemData().getBlob(5));

                if (numPosAnswer == 4) {
                    getImageList()[i][3] = getImage(getPracticeItemData().getBlob(6));
                }
                practiceIndex++;
                i++;
            } while (getPracticeItemData().moveToNext());
        }



        getQuestionData().moveToFirst();

        setNumPosAnswer(getQuestionData().getInt(8));



//        getPracticeArray();
//        setPracticeList(getPracticeList());
        System.out.println("Practice list copied: " + Arrays.toString(getPracticeList()));


        shuffleArray(getQuestionList());
        System.out.println("Test list after shuffle: " + Arrays.toString(getQuestionList()));

        int[] finalQuestionList = new int[getTestSize() + getNumPractice()];


        setAttempts(0);
        System.arraycopy(getPracticeList(), 0, finalQuestionList, 0, getNumPractice());
        System.arraycopy(getQuestionList(), 0, finalQuestionList, getNumPractice(), getQuestionList().length);


        setQuestionList(finalQuestionList);

        setQuestionList(getQuestionList());
        System.out.println("Final: " + Arrays.toString(getQuestionList()));

        setFirstQuestion(true);
        setAttempts(0);

        currentQNum = questionList[0];
        if (getCurrentQIndex() < getNumPractice()){
            setCurrentQtype("Practice");
        } else  {
            setCurrentQtype("Test");
        }
    }


    public void loadQuestionData(DatabaseOperations dop, int testID){
        if (currentQtype.equals("Practice")){
            setPic1Bitmap(imageList[pracIdIndex.get(currentQNum)][0]);
            setPic2Bitmap(imageList[pracIdIndex.get(currentQNum)][1]);
            setPic3Bitmap(imageList[pracIdIndex.get(currentQNum)][2]);
            if (numPosAnswer == 4) {
                setPic4Bitmap(imageList[pracIdIndex.get(currentQNum)][3]);
            }
            setAudioBytes(audioList[pracIdIndex.get(currentQNum)]);

        } else if (currentQtype.equals("Test")) {

            setPic1Bitmap(imageList[questionIdIndex.get(currentQNum)][0]);
            setPic2Bitmap(imageList[questionIdIndex.get(currentQNum)][1]);
            setPic3Bitmap(imageList[questionIdIndex.get(currentQNum)][2]);
            if (numPosAnswer == 4) {
                setPic4Bitmap(imageList[questionIdIndex.get(currentQNum)][3]);
            }
            setAudioBytes(audioList[questionIdIndex.get(currentQNum)]);
        }

    }

    public int getCurrentQuestionID(){
        return getQuestionIdIndex().get(currentQNum);
    }

    public String getCurrentQuestionWord(){
        return getWordIndex().get(currentQNum);
    }




    public Bitmap byteArrayToBitmap(byte[] byteArray){
        Bitmap background = BitmapFactory.decodeByteArray(byteArray, 0,
                byteArray.length);
        Bitmap back = Bitmap.createBitmap(background.getWidth(),
                background.getHeight(), Bitmap.Config.ARGB_8888);
        return back;
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

    public void findCurrentQNum(){
        setCurrentQNum(this.getQuestionList()[getCurrentQIndex()]);
    }



    public void setNextQuestion(){
        currentQIndex++;
//        if ((currentQIndex == 15)){
//            int catchhere = 2;
//            catchhere += 2;
//            setCurrentQNum(getQuestionList()[getCurrentQIndex()]);
//        }
        setCurrentQNum(getQuestionList()[getCurrentQIndex()]);
        if (currentQtype.equals("Practice")){
            setPic1Bitmap(imageList[pracIdIndex.get(currentQNum)][0]);
            setPic2Bitmap(imageList[pracIdIndex.get(currentQNum)][1]);
            setPic3Bitmap(imageList[pracIdIndex.get(currentQNum)][2]);
            if (numPosAnswer == 4) {
                setPic4Bitmap(imageList[pracIdIndex.get(currentQNum)][3]);
            }
            setAudioBytes(audioList[pracIdIndex.get(currentQNum)]);

        } else if (currentQtype.equals("Test")) {

            setPic1Bitmap(imageList[questionIdIndex.get(currentQNum)][0]);
            setPic2Bitmap(imageList[questionIdIndex.get(currentQNum)][1]);
            setPic3Bitmap(imageList[questionIdIndex.get(currentQNum)][2]);
            if (numPosAnswer == 4) {
                setPic4Bitmap(imageList[questionIdIndex.get(currentQNum)][3]);
            }
            setAudioBytes(audioList[questionIdIndex.get(currentQNum)]);
        }
    }

    public void incrementAttempts(){
        setAttempts(getAttempts() + 1);
    }

    public void incrementPracCorrect(){
        setPracCorrect(getPracCorrect() + 1);
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }











}
