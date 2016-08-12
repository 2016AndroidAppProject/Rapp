package com.example.nick.rapp;

/**
 * Created by Nick on 7/19/2016.
 */

import android.app.Activity;
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
import android.net.rtp.AudioStream;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


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

    public String RESULTSINDEX_CREATE = "CREATE TABLE " + tableData.RESULTINDEX.TABLE_NAME +
            "(" + tableData.RESULTINDEX.STUDENT_ID + " INTEGER," +
            tableData.RESULTINDEX.RESULT_ID + " INTEGER PRIMARY KEY," +
            tableData.RESULTINDEX.TIME + " DATETIME);";

    public String RESULTS_CREATE = "CREATE TABLE " + tableData.RESULTS.TABLE_NAME +
            "(" + tableData.RESULTS.FOIL_TYPE + " TEXT," +
            tableData.RESULTS.WORD + " TEXT," +
            tableData.RESULTS.CORRECT + " BOOLEAN," +
            tableData.RESULTS.TEST_ID + " INTEGER," +
            tableData.RESULTS.RESULT_ID + " INTEGER," +

            tableData.RESULTS.QUESTION_ID + " INTEGER," +
            "FOREIGN KEY(" + tableData.QUESTIONS.QUESTION_ID + ") REFERENCES " +
            tableData.QUESTIONS.TABLE_NAME + "(" + tableData.QUESTIONS.QUESTION_ID + ")," +


            "FOREIGN KEY(" + tableData.RESULTS.RESULT_ID + ") REFERENCES " +
            tableData.RESULTINDEX.TABLE_NAME + "(" + tableData.RESULTINDEX.RESULT_ID + ")," +


            "FOREIGN KEY(" + tableData.RESULTS.TEST_ID + ") REFERENCES " +
            tableData.TESTS.TABLE_NAME + "(" + tableData.TESTS.TEST_ID + "));";

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
            tableData.TESTS.TEST_TYPE + " TEXT);";

    public DatabaseOperations(Context context) {
            //, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, tableData.STUDENTINFO.DATABASE_NAME, null , database_version);
        Log.d("Database operations", "Database created");
        Log.d("Database operations", STUDENTINFO_CREATE);
        Log.d("Database operations", USERINFO_CREATE);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(USERINFO_CREATE);
        sdb.execSQL(STUDENTINFO_CREATE);
        sdb.execSQL(RESULTSINDEX_CREATE);
        sdb.execSQL(TESTS_CREATE);
        sdb.execSQL(QUESTIONS_CREATE);
        sdb.execSQL(RESULTS_CREATE);
        Log.d("Database operations", "USERINFO Table created");
        Log.d("Database operations", "STUDENTINFO Table created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user_info;");
        db.execSQL("DROP TABLE IF EXISTS student_info;");
        onCreate(db);
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

    public void addNewTest(DatabaseOperations dop, String testName, String testType, int testID){
        SQLiteDatabase SQLD = dop.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(tableData.TESTS.TEST_NAME, testName);
        cv.put(tableData.TESTS.TEST_TYPE, testType);
        cv.put(tableData.TESTS.TEST_ID, testID);

        long k = SQLD.insert(tableData.TESTS.TABLE_NAME, null, cv);
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


    public Cursor getStudentInfo(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.STUDENTINFO.STUDENT_FIRSTNAME,
                tableData.STUDENTINFO.STUDENT_ID, tableData.STUDENTINFO.TEACHER_NAME};
        Cursor CR = SQ.query(tableData.STUDENTINFO.TABLE_NAME, columns, null, null, null, null, null);

        return CR;


    }

    public Cursor getTests(DatabaseOperations dop){
        SQLiteDatabase SQ = dop.getReadableDatabase();
        String[] columns = {tableData.TESTS.TEST_ID, tableData.TESTS.TEST_TYPE, tableData.TESTS.TEST_NAME};
        Cursor CR = SQ.query(tableData.TESTS.TABLE_NAME, columns, null, null, null, null, null, null);
        return CR;
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
                tableData.QUESTIONS.PHON_FOIL, tableData.QUESTIONS.TYPE, tableData.QUESTIONS.NUM_ANSWERS};
        String whereClause = tableData.QUESTIONS.TEST_ID.toString() + "=?";
        Cursor CR = SQ.query(tableData.QUESTIONS.TABLE_NAME, columns,
                whereClause, new String[] {Integer.toString(testID)}, null, null, null);
        return CR;
    }

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
//        Uri filePath = Uri.parse("android.resource://com.example.nick.rapp/" + id);
//        File file = new File(filePath);
//        InputStream is = (InputStream) getResources().openRawResource(id);
//        byte[] buffer = new byte[(int)file.length()];
//        fis.read(buffer, 0, buffer.length);
//        fis.close();
//        return buffer;
    }




    public static byte[] pngToByteArray(Bitmap bitmap) {
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

    public static byte[] jpgToByteArray(Bitmap bitmap) {
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
