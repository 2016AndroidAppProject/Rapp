package com.example.nick.rapp;

import android.content.Context;

/**
 * Created by Nick on 6/23/2016.
 */


//This class is part of the model component of the model-view-controller design.

    /*This class is responsible for holding the current status of the questions in the app.
    * such as which question we are on. It is not responsible for holding the question assets, such
     * as audio and images; those files will be held by the local and remote databases.
     *
     * The class employs a high degree of encapsulation.*/


public class currentQuestionData {
    Context context;
    int correctAnswer;
    int currentSelection;
    int numPosAnswer;
    String currentQtype;
    int currentIndex;


    //these two arrays hold the randomized integer arrays that determine the question order.
    int[] practiceList;
    int[] questionList;



    //THE FOLLOWING VALUE IS HARDCODED BELOW TO REFLECT THE NUMBER OF EXAMPLE QUESTIONS
    //IN FINAL APP IT WILL REFLECT THE NUMBER OF QUESTIONS IN A POSSIBLE TEST
    //AND WILL BE OBTAINED VIA QUERY FROM THE DATABASE
    int testSize;

    //This value indicates how many practice items are in the test
    int numPractice;

    public int getNumPractice() {
        return numPractice;
    }

    public void setNumPractice(int numPractice) {
        this.numPractice = numPractice;
    }
    //WILL NEED TO BE CHANGED TO ALLOW FOR DIFFERENT TESTS TO BE PUT IN


    public currentQuestionData(int correctAnswer, int currentSelection,
                               int numPosAnswer, String currentQtype, int currentIndex
                                , int testSize, int numPractice, int[] practiceList, int[] questionList) {
        this.correctAnswer = correctAnswer;
        this.currentSelection = currentSelection;
        this.numPosAnswer = numPosAnswer;
        this.currentQtype = currentQtype;
        this.currentIndex = currentIndex;
        this.testSize = testSize;
        this.numPractice = numPractice;
        this.practiceList = practiceList;
        this.questionList = questionList;

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


    //SINGLETON pattern: the class is instantiated here so
    // that it is ready to hold data when we initialize it in another class.
    // It is first initialized in the login screen.java class.
    private static final currentQuestionData CURRENT_QUESTION_DATA =
            new currentQuestionData(0, 0, 4, "Practice", 0, 24, 2, null, null);

    public static currentQuestionData getInstance() {return CURRENT_QUESTION_DATA;}

    public int getTestSize() {
        return testSize;
    }

    public void setTestSize(int testSize) {
        this.testSize = testSize;
    }

    public int getQuestionNum(){
        return this.currentIndex;
    }

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




    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void nextQuestion() {

        this.currentIndex++;
    }

    // boolean nextQEmpty?





}
