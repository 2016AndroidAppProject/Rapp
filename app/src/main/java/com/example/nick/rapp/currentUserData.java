package com.example.nick.rapp;

import android.app.Activity;

/**
 * Created by Nick on 6/23/2016.
 */

//This class is part of the model component of the model-view-controller design.

    /*This class represents the user currently using the system. It is responsible for holding all information relating to that user,
     * with the exception of question data. This information governs what the user is able to access in the app, and
     * beacuse it contains identifying information, how they interact with all the data in the application and related
     * databases IE a user with the userType teacher will not be able to access tests that are marked as admin only */


public class currentUserData {
    private String userName;
    private String password;
    private String userType;
    private String userRealName;
//    private Activity prevAct;
//    private Activity currentAct;

    public currentUserData(String userName, String password, String userType, String userRealName) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
//        this.prevAct = prevAct;
//        this.currentAct = currentAct;
    }


    //SINGLETON PATTERN: The user is instantiated here so that the user is ready to hold data when initialized in
    //the other class. The userData is initialized in the loginController.java class.
    private static final currentUserData CURRENT_USER_DATA = new currentUserData("blank", "blank", "blank", "blank");
//
//    public Activity getPrevAct() {
//        return prevAct;
//    }
//
//    public void setPrevAct(Activity prevAct) {
//        this.prevAct = prevAct;
//    }
//
//    public Activity getCurrentAct() {
//        return currentAct;
//    }
//
//    public void setCurrentAct(Activity currentAct) {
//        this.currentAct = currentAct;
//    }


    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public static currentUserData getInstance() {return CURRENT_USER_DATA;}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    //    Unclear if we need these boolean values
    /*boolean isCorrect;
    boolean isValid;*/



}
