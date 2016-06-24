package com.example.nick.rapp;

/**
 * Created by Nick on 6/23/2016.
 */
public class currentUserData {
    private String userName;
    String password;
    String userType;
    private int selection;

    public currentUserData(String userName, String password, String userType, int selection) {
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        this.selection = selection;
    }

    private static final currentUserData CURRENT_USER_DATA = new currentUserData("blank", "blank", "Teacher", 0);


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

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
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
