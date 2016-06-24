package com.example.nick.rapp;

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
    private int correctAnswer;
    int currentSelection;
    int numPosAnswer;
    String currentQtype;
    int totalQuestions;
    String currentQuestion;

    public currentQuestionData(int correctAnswer, int currentSelection,
                               int numPosAnswer, String currentQtype, int totalQuestions, String currentQuestion) {
        this.correctAnswer = correctAnswer;
        this.currentSelection = currentSelection;
        this.numPosAnswer = numPosAnswer;
        this.currentQtype = currentQtype;
        this.totalQuestions = totalQuestions;
        this.currentQuestion = currentQuestion;
    }


    //SINGLETON pattern: the class is instantiated here so
    // that it is ready to hold data when we initialize it in another class.
    // It is first initialized in the login screen.java class.
    private static final currentQuestionData CURRENT_QUESTION_DATA =
            new currentQuestionData(0, 0, 0, "practice", 0, "blank");

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

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    // boolean nextQEmpty?





}
