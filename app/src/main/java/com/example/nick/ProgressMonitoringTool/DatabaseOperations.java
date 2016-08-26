package com.example.nick.ProgressMonitoringTool;

/**
 * Created by Nick on 7/19/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class DatabaseOperations extends SQLiteOpenHelper {
    public static final int database_version = 1;



    public String USERINFO_CREATE = "CREATE TABLE "+
            tableData.USERINFO.TABLE_NAME+"(" + tableData.USERINFO.USER_NAME+" TEXT PRIMARY KEY,"
            + tableData.USERINFO.USER_PASSWORD+" TEXT," + tableData.USERINFO.USER_TYPE+" TEXT," +
            tableData.USERINFO.USER_LASTNAME+" TEXT);";

    public String STUDENTINFO_CREATE = "CREATE TABLE "+
            tableData.STUDENTINFO.TABLE_NAME+"(" + tableData.STUDENTINFO.STUDENT_FIRSTNAME+" TEXT,"
            + tableData.STUDENTINFO.STUDENT_ID+" INTEGER PRIMARY KEY," +
            tableData.STUDENTINFO.TEACHER_NAME+" TEXT);";

    public String PRACITEMS_CREATE = "CREATE TABLE " +
            tableData.PRACTICEITEMSETS.TABLE_NAME+"(" + tableData.PRACTICEITEMSETS.PRAC_NAME + " TEXT,"
            + tableData.PRACTICEITEMSETS.PRAC_ID + " INTEGER PRIMARY KEY);";

    public Cursor getPracItemSets(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.PRACTICEITEMSETS.PRAC_ID,
                            tableData.PRACTICEITEMSETS.PRAC_NAME};
        Cursor CR = SQ.query(tableData.PRACTICEITEMSETS.TABLE_NAME, columns, null, null, null, null, null);
        return CR;
    }



    public void addNewPracItemSet(DatabaseOperations dop, int pracId, String pracName){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(tableData.PRACTICEITEMSETS.PRAC_ID, pracId);
        cv.put(tableData.PRACTICEITEMSETS.PRAC_NAME, pracName);
        long k = SQLD.insert(tableData.PRACTICEITEMSETS.TABLE_NAME, null, cv);
    }

    public String TESTCOMPLETIONRECORDS_CREATE = "CREATE TABLE " + tableData.TESTCOMPLETIONRECORDS.TABLE_NAME +
            "(" + tableData.TESTCOMPLETIONRECORDS.RECORD_ID + " INTEGER PRIMARY KEY,"
            + tableData.TESTCOMPLETIONRECORDS.STUDENT_ID + " INTEGER," +
            tableData.TESTCOMPLETIONRECORDS.STUDENT_NAME + " TEXT," +
            tableData.TESTCOMPLETIONRECORDS.TEST_ID + " INTEGER," +
            tableData.TESTCOMPLETIONRECORDS.TEST_NAME + " TEXT," +
            tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONS + " INTEGER," +
            tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCORRECT + " INTEGER," +
            tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCOMPLETE + " INTEGER," +
            tableData.TESTCOMPLETIONRECORDS.DATE + " DATETIME);";

    public Cursor getTestCompletionRecords(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.TESTCOMPLETIONRECORDS.RECORD_ID,
                tableData.TESTCOMPLETIONRECORDS.RECORD_ID,
                tableData.TESTCOMPLETIONRECORDS.STUDENT_ID,
                tableData.TESTCOMPLETIONRECORDS.STUDENT_NAME,
                tableData.TESTCOMPLETIONRECORDS.TEST_ID,
                tableData.TESTCOMPLETIONRECORDS.TEST_NAME,
                tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONS,
                tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCORRECT,
                tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCOMPLETE,
                tableData.TESTCOMPLETIONRECORDS.DATE};
        Cursor CR = SQ.query(tableData.TESTCOMPLETIONRECORDS.TABLE_NAME, columns, null, null, null, null, null);
        return CR;
    }



    public void addNewTestCompletionRecord(DatabaseOperations dop, int recordId, int studentId, String studentName, int testId,
                                           String testName, int numQuestions, int numQuestionsCorrect, int numQuestionsComplete){
                SQLiteDatabase SQLD = dop.getWritableDatabase();
                ContentValues cv = new ContentValues();

                cv.put(tableData.TESTCOMPLETIONRECORDS.RECORD_ID, recordId);
                cv.put(tableData.TESTCOMPLETIONRECORDS.STUDENT_ID, studentId);
                cv.put(tableData.TESTCOMPLETIONRECORDS.STUDENT_NAME, studentName);
                cv.put(tableData.TESTCOMPLETIONRECORDS.TEST_ID, testId);
                cv.put(tableData.TESTCOMPLETIONRECORDS.TEST_NAME, testName);
                cv.put(tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONS, numQuestions);
                cv.put(tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCORRECT, numQuestionsCorrect);
                cv.put(tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCOMPLETE, numQuestionsComplete);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(new Date());

                ContentValues values = new ContentValues();
                values.put("ColumnName", strDate);
                cv.put(tableData.TESTCOMPLETIONRECORDS.DATE, strDate);
                long k = SQLD.insert(tableData.TESTCOMPLETIONRECORDS.TABLE_NAME, null, cv);
    }

    public void updateTestCompletionRecord(DatabaseOperations dop, int recordId,
                                           int numQuestionsCorrect, int numQuestionsComplete){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        String strFilter = tableData.TESTCOMPLETIONRECORDS.RECORD_ID + "=" + recordId;
        ContentValues cv = new ContentValues();

        cv.put(tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCORRECT,numQuestionsCorrect);
        cv.put(tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCOMPLETE,numQuestionsComplete);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());

//        ContentValues values = new ContentValues();
//        values.put("ColumnName", strDate);
        cv.put(tableData.TESTCOMPLETIONRECORDS.DATE, strDate);

        SQ.update(tableData.TESTCOMPLETIONRECORDS.TABLE_NAME, cv, strFilter, null);
    }

    public String RESULTS_CREATE = "CREATE TABLE " + tableData.RESULTS.TABLE_NAME +
            "(" + tableData.RESULTS.QUESTION_ID + " INTEGER," +
            tableData.RESULTS.WORD + " TEXT," +
            tableData.RESULTS.CORRECT + " BOOLEAN," +
            tableData.RESULTS.TEST_ID + " INTEGER," +
            tableData.RESULTS.TEST_NAME + " TEXT," +
            tableData.RESULTS.RESULT_ID + " INTEGER, " +
            tableData.RESULTS.RECORD_ID + " INTEGER," +
            tableData.RESULTS.STUDENT_ID + " INTEGER, " +
            tableData.RESULTS.STUDENT_NAME + " TEXT," +

            "FOREIGN KEY (" + tableData.RESULTS.RECORD_ID + ") REFERENCES " +
            tableData.TESTCOMPLETIONRECORDS.TABLE_NAME + "(" + tableData.TESTCOMPLETIONRECORDS.RECORD_ID + ")," +

            "FOREIGN KEY (" + tableData.RESULTS.STUDENT_ID + ") REFERENCES " +
            tableData.STUDENTINFO.TABLE_NAME + "(" + tableData.STUDENTINFO.STUDENT_ID + ")," +

            "FOREIGN KEY(" + tableData.QUESTIONS.QUESTION_ID + ") REFERENCES " +
            tableData.QUESTIONS.TABLE_NAME + "(" + tableData.QUESTIONS.QUESTION_ID + ")," +

            "FOREIGN KEY(" + tableData.RESULTS.TEST_ID + ") REFERENCES " +
            tableData.TESTS.TABLE_NAME + "(" + tableData.TESTS.TEST_ID + ")," +
            " PRIMARY KEY(" + tableData.RESULTS.RECORD_ID + ", " + tableData.RESULTS.RESULT_ID + "));";







    public String QUESTIONS_CREATE = "CREATE TABLE " + tableData.QUESTIONS.TABLE_NAME +
            "(" + tableData.QUESTIONS.DIFFICULTY + " TEXT," +
            tableData.QUESTIONS.TYPE + " TEXT," +
            tableData.QUESTIONS.THEM_FOIL + " BLOB," +
            tableData.QUESTIONS.TARGET + " BLOB," +
            tableData.QUESTIONS.CON_FOIL + " BLOB," +
            tableData.QUESTIONS.PHON_FOIL + " BLOB," +
            tableData.QUESTIONS.WORD + " TEXT,"
            + tableData.TESTS.TEST_ID + " INTEGER," +
            tableData.QUESTIONS.AUDIO + " BLOB," +
            tableData.QUESTIONS.NUM_ANSWERS + " INTEGER," +
            tableData.QUESTIONS.QUESTION_ID + " INTEGER," +
            "FOREIGN KEY(" + tableData.TESTS.TEST_ID + ") REFERENCES " +
            tableData.TESTS.TABLE_NAME + "(" + tableData.TESTS.TEST_ID + ")," +
            " PRIMARY KEY (" + tableData.QUESTIONS.TEST_ID + ", " + tableData.QUESTIONS.QUESTION_ID + "));";

    public String TESTS_CREATE = "CREATE TABLE " + tableData.TESTS.TABLE_NAME +
            "(" + tableData.TESTS.TEST_ID + " INTEGER PRIMARY KEY," +
            tableData.TESTS.TEST_NAME + " TEXT," +
            tableData.TESTS.TEST_TYPE + " TEXT," +
            tableData.TESTS.PRAC_ID + " INTEGER);";

    public String SETTINGS_CREATE = "CREATE TABLE " + tableData.SETTINGS.TABLE_NAME +
            "(" + tableData.SETTINGS.RESULT_MODE + " STRING," +
                tableData.SETTINGS.MAINTAIN_PROPORTIONS + " BOOLEAN," +
                tableData.SETTINGS.NUM_ITEMSTOTEST + " STRING);";

    public DatabaseOperations(Context context) {
            //, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, tableData.STUDENTINFO.DATABASE_NAME, null , database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(USERINFO_CREATE);
        sdb.execSQL(STUDENTINFO_CREATE);
       // sdb.execSQL(TESTCOMPLETIONRECORDS_CREATE);
        sdb.execSQL(TESTS_CREATE);
        sdb.execSQL(QUESTIONS_CREATE);
        sdb.execSQL(RESULTS_CREATE);
        sdb.execSQL(TESTCOMPLETIONRECORDS_CREATE);
        sdb.execSQL(PRACITEMS_CREATE);
        sdb.execSQL(SETTINGS_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Database operations", "Tables dropped on upgrade");
    }

    //This method is used to add new users to the app.
    //It is meant to test the functionality of inserting data into the SQLlite database.
    //currently it is linked to a button on the adminview_screen but in the final product
    //it will be on its own screen farther into settings.
    public void addNewUser(DatabaseOperations dop, String userName, String password,
                           String userType,String userLastName){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.USERINFO.USER_NAME, userName);
        cv.put(tableData.USERINFO.USER_PASSWORD, password);
        cv.put(tableData.USERINFO.USER_TYPE, userType);
        cv.put(tableData.USERINFO.USER_LASTNAME, userLastName);
        long k = SQLD.insert(tableData.USERINFO.TABLE_NAME, null, cv);

        Log.d("Database operations", "New user inserted");
    }

    public void addNewSettings(DatabaseOperations dop, String resultMode, boolean maintainProportions, String numItemsToTest){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.SETTINGS.RESULT_MODE, resultMode);
        cv.put(tableData.SETTINGS.MAINTAIN_PROPORTIONS, maintainProportions);
        cv.put(tableData.SETTINGS.NUM_ITEMSTOTEST, numItemsToTest);
        long k = SQLD.insert(tableData.SETTINGS.TABLE_NAME, null, cv);

    }




    public void addNewResult(DatabaseOperations dop, int questionID, String word,
                             boolean correct, int testID, String testName, int resultID, int recordID, int studentID, String studentName){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.RESULTS.QUESTION_ID, questionID);
        cv.put(tableData.RESULTS.WORD, word);
        cv.put(tableData.RESULTS.CORRECT, correct);
        cv.put(tableData.RESULTS.TEST_ID, testID);
        cv.put(tableData.RESULTS.TEST_NAME, testName);
        cv.put(tableData.RESULTS.RESULT_ID, resultID);
        cv.put(tableData.RESULTS.RECORD_ID, recordID);
        cv.put(tableData.RESULTS.STUDENT_ID, studentID);
        cv.put(tableData.RESULTS.STUDENT_NAME, studentName);
        long k = SQLD.insert(tableData.RESULTS.TABLE_NAME, null, cv);

        Log.d("Database operations", "New result inserted");
    }


    public void addNewTest(DatabaseOperations dop, String testName, String testType, int testID, int pracID){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.TESTS.TEST_NAME, testName);
        cv.put(tableData.TESTS.TEST_TYPE, testType);
        cv.put(tableData.TESTS.TEST_ID, testID);
        cv.put(tableData.TESTS.PRAC_ID, pracID);

        long k = SQLD.insert(tableData.TESTS.TABLE_NAME, null, cv);
        Log.d("Database operations", "New test inserted");
    }

//    public void addNewQuestion(DatabaseOperations dop, int testID, int questionID, String word){
//        SQLiteDatabase SQLD = dop.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//
//        cv.put(tableData.QUESTIONS.TEST_ID, testID);
//        cv.put(tableData.QUESTIONS.QUESTION_ID, questionID);
//        cv.put(tableData.QUESTIONS.WORD, word);
//
//        long k = SQLD.insert(tableData.TESTS.TABLE_NAME, null, cv);
//    }

    public void addNewStudent(DatabaseOperations dop, String studentFName,int studentID, String teacherName){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.STUDENTINFO.STUDENT_FIRSTNAME, studentFName);

        cv.put(tableData.STUDENTINFO.STUDENT_ID, studentID);
        cv.put(tableData.STUDENTINFO.TEACHER_NAME, teacherName);
        long k = SQLD.insert(tableData.STUDENTINFO.TABLE_NAME, null, cv);

        Log.d("Database operations", "New student inserted");
    }

    public ArrayList<String> getPracNames(){
        ArrayList<String> names = new ArrayList<String>();
        Cursor CR = this.getPracItemSets(this);
        if (CR.getCount() == 0){
            return null;
        }
        CR.moveToFirst();
        do {
            names.add(CR.getString(1));

        } while (CR.moveToNext());
        return names;
    }

    public int getPracIDByName(String name){
        int id = 0;
        Cursor CR = this.getPracItemSets(this);
        if (CR.getCount() == 0){
            return 0;
        }
        CR.moveToFirst();
        do {
            if (CR.getString(1).equals(name)){
                id = CR.getInt(0);
            }

        } while (CR.moveToNext());
        return id;
    }


    public Cursor getStudentInfo(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.STUDENTINFO.STUDENT_FIRSTNAME,
                tableData.STUDENTINFO.STUDENT_ID, tableData.STUDENTINFO.TEACHER_NAME};
        Cursor CR = SQ.query(tableData.STUDENTINFO.TABLE_NAME, columns, null, null, null, null, null);

        return CR;


    }

    public Cursor getResultMode(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.SETTINGS.RESULT_MODE};
        Cursor CR = SQ.query(tableData.SETTINGS.TABLE_NAME, columns, null, null, null, null, null);

        return CR;
    }

    public Cursor getMaintainProp(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.SETTINGS.MAINTAIN_PROPORTIONS};
        Cursor CR = SQ.query(tableData.SETTINGS.TABLE_NAME, columns, null, null, null, null, null);

        return CR;
    }

    public Cursor getNumItemsToTest(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.SETTINGS.NUM_ITEMSTOTEST};
        Cursor CR = SQ.query(tableData.SETTINGS.TABLE_NAME, columns, null, null, null, null, null);

        return CR;
    }

    public void updateNumItemsToTest(DatabaseOperations dop, String newItemString){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        //String strFilter = tableData.SETTINGS.RESULT_MODE + "=?";
        ContentValues cv = new ContentValues();
        cv.put(tableData.SETTINGS.NUM_ITEMSTOTEST, newItemString);

        SQ.update(tableData.SETTINGS.TABLE_NAME, cv, null, null);
    }

    public void updateMaintainProp(DatabaseOperations dop, boolean maintainProp){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        //String strFilter = tableData.SETTINGS.RESULT_MODE + "=?";
        ContentValues cv = new ContentValues();
        cv.put(tableData.SETTINGS.MAINTAIN_PROPORTIONS, maintainProp);

        SQ.update(tableData.SETTINGS.TABLE_NAME, cv, null, null);
    }

    public void updateResultMode(DatabaseOperations dop, String newMode){
        SQLiteDatabase SQ = dop.getWritableDatabase();
        //String strFilter = tableData.SETTINGS.RESULT_MODE + "=?";
        ContentValues cv = new ContentValues();
        cv.put(tableData.SETTINGS.RESULT_MODE, newMode);

        SQ.update(tableData.SETTINGS.TABLE_NAME, cv, null, null);
    }
    public Cursor getTests(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.TESTS.TEST_ID, tableData.TESTS.TEST_TYPE, tableData.TESTS.TEST_NAME,
                            tableData.TESTS.PRAC_ID};
        Cursor CR = SQ.query(tableData.TESTS.TABLE_NAME, columns, null, null, null, null, null, null);
        return CR;
    }

    public Cursor getPracItemSetsByTestID(DatabaseOperations dop, int testID){
        Cursor tests = getTests(this);
        int pracID = 0;
        if (tests.getCount() == 0){
            return null;
        }
        tests.moveToFirst();
        do {
            if (tests.getInt(0) == testID){
                pracID = tests.getInt(3);
            }
        } while (tests.moveToNext());

        Cursor practiceQuestions = getQuestionsForTest(this, pracID);
        return practiceQuestions;
    }


    public int getTestIDByName(DatabaseOperations dop, String name){
        Cursor CR = getTests(dop);
        CR.moveToFirst();
        if (CR.getCount() == 0){
            return 0;
        }
        do {
            if (CR.getString(2).equals(name)){
                return CR.getInt(0);
            }
        }
        while (CR.moveToNext());
        return 0;
    }

    public int getStudentIDByName(DatabaseOperations dop, String name){
        Cursor CR = getStudentInfo(dop);
        CR.moveToFirst();
        if (CR.getCount() == 0){
            return 0;
        }
        do {
            if (CR.getString(2).equals(name)){
                return CR.getInt(0);
            }
        }
        while (CR.moveToNext());
        return 0;
    }

    public ArrayList<String> getTestNamesForAdministrators(Cursor CR){
        ArrayList<String> adminTests = new ArrayList<String>();
        CR.moveToFirst();
        if (CR.getCount() == 0){
            return null;
        }
        do {
                adminTests.add(CR.getString(2));
        }
        while (CR.moveToNext());
        return adminTests;
    }


    public ArrayList<String> getTestNamesForTeachers(Cursor CR){
        ArrayList<String> teacherTests = new ArrayList<String>();
        CR.moveToFirst();
        if (CR.getCount() == 0){
            return null;
        }

        do {
            if (CR.getString(1).equals("Teacher") || (CR.getString(1).equals(""))){
                teacherTests.add(CR.getString(2));
            }

        }
        while (CR.moveToNext());
        return teacherTests;
    }

    //Method for retrieving an array of names, either last names or first.
    //if the int is 0 it will get the firstNames from column 0,
    //if the int is 1 it will get the lastNames from column 1.
    public ArrayList<String> getStudentNames(DatabaseOperations dop, String teacher){
        ArrayList<String> studentNames = new ArrayList<String>();
        Cursor CR = getStudentInfo(dop);
        CR.moveToFirst();
        do {
            if (CR.getString(2).equals(teacher) || CR.getString(2).equals("")) {
                studentNames.add(CR.getString(0));
            }
        }
        while (CR.moveToNext());
        return studentNames;
    }

    //Method for retrieving a name of all the teachers to display in the app
    //automatically goes to index 2 of the user info table beacuse that is the column
    //under which user types are indicated
    public ArrayList<String> getTeacherNames(DatabaseOperations dop){
        ArrayList<String> teacherNames = new ArrayList<String>();
        Cursor CR = getUserInfo(dop);
        CR.moveToFirst();
        do {
            if (CR.getString(2).equals("Teacher") || CR.getString(2).equals("")){
                teacherNames.add(CR.getString(3));
            }


        }
        while (CR.moveToNext());
        return teacherNames;

    }


    public Cursor getUserInfo(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.USERINFO.USER_NAME, tableData.USERINFO.USER_PASSWORD, tableData.USERINFO.USER_TYPE,
                            tableData.USERINFO.USER_LASTNAME};
        Cursor CR = SQ.query(tableData.USERINFO.TABLE_NAME, columns, null, null, null, null, null);

        return CR;


    }

    public Cursor getQuestionsForTest(DatabaseOperations dop, int testID){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.QUESTIONS.TEST_ID, tableData.QUESTIONS.QUESTION_ID , tableData.QUESTIONS.AUDIO ,
                tableData.QUESTIONS.THEM_FOIL , tableData.QUESTIONS.TARGET, tableData.QUESTIONS.CON_FOIL,
                tableData.QUESTIONS.PHON_FOIL, tableData.QUESTIONS.TYPE, tableData.QUESTIONS.NUM_ANSWERS,
                tableData.QUESTIONS.WORD};
        String whereClause = tableData.QUESTIONS.TEST_ID.toString() + "=?";
        Cursor CR = SQ.query(tableData.QUESTIONS.TABLE_NAME, columns,
                whereClause, new String[] {Integer.toString(testID)}, null, null, null);
        return CR;
    }

    public Cursor getResults(DatabaseOperations dop, String testName){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.RESULTS.RESULT_ID, tableData.RESULTS.RECORD_ID, tableData.RESULTS.WORD, tableData.RESULTS.STUDENT_NAME, tableData.RESULTS.CORRECT};
        String whereClause = tableData.RESULTS.TEST_NAME + "=?";
        Cursor CR = SQ.query(tableData.RESULTS.TABLE_NAME, columns,
                whereClause, new String[] {testName}, null, null, null);
        return CR;
    }

    public Cursor getAllResults(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.RESULTS.RESULT_ID, tableData.RESULTS.RECORD_ID, tableData.RESULTS.WORD, tableData.RESULTS.STUDENT_NAME, tableData.RESULTS.CORRECT};
        Cursor CR = SQ.query(tableData.RESULTS.TABLE_NAME, columns,
                null, null, null, null, null);
        return CR;
    }

    public Cursor getResultWords(DatabaseOperations dop, String testName){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.RESULTS.RESULT_ID, tableData.RESULTS.WORD};
        String whereClause = tableData.RESULTS.TEST_NAME + "=?";
        Cursor CR = SQ.query(tableData.RESULTS.TABLE_NAME, columns,
                whereClause, new String[] {testName}, tableData.RESULTS.WORD, null, null);
        return CR;
    }

    public Cursor getCompletionRecordsByStudent(DatabaseOperations dop, String testName){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.TESTCOMPLETIONRECORDS.RECORD_ID, tableData.TESTCOMPLETIONRECORDS.STUDENT_NAME,
                tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCORRECT, tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONS,
                tableData.TESTCOMPLETIONRECORDS.DATE};
        String whereClause = tableData.TESTCOMPLETIONRECORDS.TEST_NAME + "=?";
        Cursor CR = SQ.query(tableData.TESTCOMPLETIONRECORDS.TABLE_NAME, columns,
                whereClause, new String[] {testName}, null, null, null);

        ArrayList<String> students = new ArrayList<String>();
        return CR;
    }

    public double percentageCorrectResults(DatabaseOperations dop, String testName){
        Double percentCorrect = 0.0;
        int totalResults = 0;
        int correctResults = 0;
        Cursor CR = dop.getResults(dop, testName);
        CR.moveToFirst();
        do {
            totalResults++;
            if (CR.getInt(4) == 1){
                correctResults++;
            }
        } while (CR.moveToNext());

        percentCorrect = ((double) correctResults / totalResults) * 100;
        double finalPercCorrect = (double) Math.round(percentCorrect * 100) / 100;
        return finalPercCorrect;
    }



    public HashMap<String, Double> getWordsCorrrect(DatabaseOperations dop, Cursor resultsForTest){
        HashMap<String, int[]> wordsCorrect = new HashMap<String, int[]>();
        HashMap<String, Double> wordsCorrectPercentages = new HashMap<String, Double>();

        resultsForTest.moveToFirst();

        //For each word, we create an array of two int values, the first value stores the total number of times
        //that word pops up in results.
        //the second value stores the number of times that word was correctly answered in the result.
        do {
            if (wordsCorrect.get(resultsForTest.getString(2)) == null){
                int[] numCorrect = new int[2];
                numCorrect[0] = 1;
                if (resultsForTest.getInt(4) == 1){
                    numCorrect[1] = 1;
                } else if (resultsForTest.getInt(4) == 0){
                    numCorrect[1] = 0;
                }
                wordsCorrect.put(resultsForTest.getString(2), numCorrect);
            } else {
                int[] numCorrect = new int[2];
                numCorrect[0] = wordsCorrect.get(resultsForTest.getString(2))[0] + 1;
                if (resultsForTest.getInt(4) == 1){
                    numCorrect[1] = wordsCorrect.get(resultsForTest.getString(2))[1] + 1;
                } else if (resultsForTest.getInt(4) == 0){
                    numCorrect[1] = wordsCorrect.get(resultsForTest.getString(2))[1];;
                }
                wordsCorrect.put(resultsForTest.getString(2), numCorrect);
            }

        } while (resultsForTest.moveToNext());

        for (String word : wordsCorrect.keySet()) {
            wordsCorrectPercentages.put(word, ((double) wordsCorrect.get(word)[1] / wordsCorrect.get(word)[0]));
        }

        return wordsCorrectPercentages;

    }

    public Cursor getCompletionRecordByID(DatabaseOperations dop, int recordID){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCOMPLETE, tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONS,
                            tableData.TESTCOMPLETIONRECORDS.NUM_QUESTIONSCORRECT, tableData.TESTCOMPLETIONRECORDS.DATE};
        String whereClause = tableData.TESTCOMPLETIONRECORDS.RECORD_ID + "=?";
        Cursor CR = SQ.query(tableData.TESTCOMPLETIONRECORDS.TABLE_NAME, columns, whereClause,
                new String[] {Integer.toString(recordID)}, null, null, null);
        return CR;
    }

//    public String RESULTS_CREATE = "CREATE TABLE " + tableData.RESULTS.TABLE_NAME +
//            "(" + tableData.RESULTS.QUESTION_ID + " INTEGER," +
//            tableData.RESULTS.WORD + " TEXT," +
//            tableData.RESULTS.CORRECT + " BOOLEAN," +
//            tableData.RESULTS.TEST_ID + " INTEGER," +
//            tableData.RESULTS.TEST_NAME + " TEXT," +
//            tableData.RESULTS.RESULT_ID + " INTEGER, " +
//            tableData.RESULTS.STUDENT_ID + " INTEGER, " +
//            tableData.RESULTS.STUDENT_NAME + " TEXT," +
//
//            "FOREIGN KEY (" + tableData.RESULTS.STUDENT_ID + ") REFERENCES " +
//            tableData.STUDENTINFO.TABLE_NAME + "(" + tableData.STUDENTINFO.STUDENT_ID + ")," +
//
//            "FOREIGN KEY(" + tableData.QUESTIONS.QUESTION_ID + ") REFERENCES " +
//            tableData.QUESTIONS.TABLE_NAME + "(" + tableData.QUESTIONS.QUESTION_ID + ")," +
//
//            "FOREIGN KEY(" + tableData.RESULTS.TEST_ID + ") REFERENCES " +
//            tableData.TESTS.TABLE_NAME + "(" + tableData.TESTS.TEST_ID + ")," +
//            " PRIMARY KEY(" + tableData.RESULTS.TEST_ID + ", " + tableData.RESULTS.RESULT_ID + "));";

    public Cursor getQuestionsWithID(DatabaseOperations dop, int questionID){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.QUESTIONS.TEST_ID, tableData.QUESTIONS.QUESTION_ID , tableData.QUESTIONS.AUDIO ,
                tableData.QUESTIONS.THEM_FOIL , tableData.QUESTIONS.TARGET, tableData.QUESTIONS.CON_FOIL,
                tableData.QUESTIONS.PHON_FOIL, tableData.QUESTIONS.TYPE, tableData.QUESTIONS.NUM_ANSWERS};
        String whereClause = tableData.QUESTIONS.QUESTION_ID.toString() + "=?";
        Cursor CR = SQ.query(tableData.QUESTIONS.TABLE_NAME, columns,
                whereClause, new String[] {Integer.toString(questionID)}, null, null, null);
        return CR;
    }



    public void addQuestion(DatabaseOperations dop, String word, String type, String difficulty,
                            byte[] them_foil, byte[] target, byte[] con_foil , byte[] phon_foil, byte[] audio,
                            int testID, int questionID, int numPosAnswers) throws SQLiteException {
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new  ContentValues();
        cv.put(tableData.QUESTIONS.TYPE, type);
        cv.put(tableData.QUESTIONS.DIFFICULTY, difficulty);
        cv.put(tableData.QUESTIONS.THEM_FOIL,    them_foil);
        cv.put(tableData.QUESTIONS.TARGET,   target);
        cv.put(tableData.QUESTIONS.CON_FOIL,   con_foil);
        cv.put(tableData.QUESTIONS.PHON_FOIL,   phon_foil);
        cv.put(tableData.QUESTIONS.WORD,   word);
        cv.put(tableData.QUESTIONS.TEST_ID,   testID);
        cv.put(tableData.QUESTIONS.QUESTION_ID,   questionID);
        cv.put(tableData.QUESTIONS.AUDIO, audio);
        cv.put(tableData.QUESTIONS.NUM_ANSWERS, numPosAnswers);
        SQLD.insert(tableData.QUESTIONS.TABLE_NAME, null, cv);
    }


//    public File[] getImages(){
//
//    }
//
//    public File[] getAudio(){
//
//    }
//





    //The following code is used to construct a set of questions that are related to a single test
    //(meaning, these questions are part of that test). It uses the testName field to infer
    //which test to link all the new questions to, and it takes in arrays
    //of images and audios to create the questions.
    public void createQuestions(DatabaseOperations dop, String testName, File[] images, File[] audio){

    }





//    public byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte[] buffer = new byte[8192];
//        int bytesRead;
//        while ((bytesRead = inStream.read(buffer)) > 0) {
//            baos.write(buffer, 0, bytesRead);
//        }
//        return baos.toByteArray();
//    }

    public byte[] audioToByteArray(File file) throws IOException{
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[10240];
            int i = Integer.MAX_VALUE;
            while ((i = fis.read(buff, 0, buff.length)) > 0) {
                baos.write(buff, 0, i);
            }

            return baos.toByteArray();
        } catch (IOException io){
            return null;
        }
//        IOUtils.
//        byte[] payload = IOUtils.toByteArray();
//        return payload;
//        Uri filePath = Uri.parse("android.resource://com.example.nick.ProgressMonitoringTool/" + id);
//        File file = new File(filePath);
//        InputStream is = (InputStream) getResources().openRawResource(id);
//        byte[] buffer = new byte[(int)file.length()];
//        fis.read(buffer, 0, buffer.length);
//        fis.close();
//        return buffer;
    }




    public static byte[] pngToByteArray(Bitmap bitmap) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
        byte[] result = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            // handle exception here
        }

        return result;
    }

    public static byte[] jpgToByteArray(Bitmap bitmap) throws IOException {
//        byte[] byteArray;
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//
//        int size = bitmap.getRowBytes() * bitmap.getHeight();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//        bitmap.copyPixelsToBuffer(byteBuffer);
//        byteArray = byteBuffer.array();
//        return byteArray;



        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
        byte[] result = outputStream.toByteArray();
        try {
            outputStream.close();
        } catch (IOException e) {
            // handle exception here
        }

        return result;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }


}
