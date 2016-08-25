package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class editPracticeItemsController extends AppCompatActivity {

    String picturesDirPath;
    File picturesDir;
    File newPIDir;
    String pathToNewPIDir;
    EditText newPIDirField;
    Button addPIButton;
    String newPIDirName;
    File[] imagesFiles;
    Bitmap[] images;

    String newPISetName;
    EditText newPISetField;

    DatabaseOperations dop;


    Context ctx;

    int newPISetID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpitems_screen);
        newPIDirField = (EditText) findViewById(R.id.pracItemDirField);
        addPIButton = (Button) findViewById(R.id.addNewPracSetButton);
        ctx = this;

        newPISetField = (EditText) findViewById(R.id.addPracItemSet);

        dop = new DatabaseOperations(ctx);


        addPIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    //check if name field is filled, if not display message
                    if (newPISetField.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),
                                "Please enter a set name for the new set",
                                Toast.LENGTH_LONG).show();

                    } else {
                        newPISetName = newPISetField.getText().toString();
                        //check if entered name is unique (and thus valid), if not display message.
                        if (isUnique(dop, newPISetName) == false){
                            Toast.makeText(getApplicationContext(),
                                    "There is a practice set with the name " + newPISetName + " already, please enter a different" +
                                            "name",
                                    Toast.LENGTH_LONG).show();
                            newPISetName = null;
                            newPISetField.setText("");
                        } else {
                            //Detect if test dir field has not been set, if not display message
                            //Detect if testDir could not be found, if not display message.
                            if (newPIDirField.getText().toString().equals("")){
                                Toast.makeText(getApplicationContext(),
                                        "Please enter the name of the folder where the practice item assets are located.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                //detect if external storage is mounted correctly
                                // and readable!
                                newPIDirName = newPIDirField.getText().toString();
                                if (Environment.getExternalStorageState().startsWith(
                                        Environment.MEDIA_MOUNTED) || (isExternalStorageWritable() || isExternalStorageReadable())) {

                                    pathToNewPIDir = Environment.getExternalStorageDirectory() + "/" + newPIDirName  + "/";
                                    newPIDir = new File (pathToNewPIDir);
                                    //detect if file created is an actual file
                                    if (newPIDir.exists()){
                                        loadPISet();

                                    } else {
                                        Toast.makeText(getApplicationContext(),
                                                "The directory you typed in does not exist. Please recheck your spelling and" +
                                                        " confirm that the directory is correctly stored on your device.",
                                                Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "There is problem with the tablets memory, please contact the administrators",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
        });
    }

    public void loadPISet() {
        //Loading the files from the actual directory and generating a new test id for the test to be created
        newPISetID = generatePISetID(dop);
        imagesFiles = newPIDir.listFiles();


        //sort the images from the directory by question
        ArrayList<File[]> filesSorted = getSortedFileList(imagesFiles);

        //create questions based on the files, and insert into question table
        insertNewPracticeSet(filesSorted);

        //reset the add test UI and create a new test with the appropriate id.
        finishLoading();
    }


    public void insertNewPracticeSet(ArrayList<File[]> sortedList){
        //creating the options which will be used to fetch the bitmaps from the the directory.
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap target;
        Bitmap them_foil;
        Bitmap con_foil;
        Bitmap phon_foil;
        byte[] audio;
        byte[] targetBlob = null;
        byte[] them_foilBlob = null;
        byte[] con_foilBlob = null;
        byte[] phon_foilBlob = null;
        for (int i = 0; i < sortedList.size(); i++){

            String[] fileName1 = getFileName(sortedList.get(i)[0]);
            String extension1 = (fileName1[fileName1.length - 1].split("\\."))[1];
            if (extension1.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {
                target = BitmapFactory.decodeFile(sortedList.get(i)[1].getAbsolutePath(), options);
                targetBlob = dop.jpgToByteArray(target);
            } else if (extension1.equalsIgnoreCase("png")){
                target = BitmapFactory.decodeFile(sortedList.get(i)[1].getAbsolutePath(), options);
                targetBlob  = dop.pngToByteArray(target);
            }

            String[] fileName2 = getFileName(sortedList.get(i)[1]);
            String extension2 = (fileName2[fileName1.length - 1].split("\\."))[1];
            if (extension1.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {


                them_foil = BitmapFactory.decodeFile(sortedList.get(i)[0].getAbsolutePath(), options);
                them_foilBlob = dop.jpgToByteArray(them_foil);
            } else if (extension1.equalsIgnoreCase("png")){

                them_foil = BitmapFactory.decodeFile(sortedList.get(i)[0].getAbsolutePath(), options);
                them_foilBlob = dop.pngToByteArray(them_foil);
            }

            String[] fileName3 = getFileName(sortedList.get(i)[2]);
            String extension3 = (fileName3[fileName1.length - 1].split("\\."))[1];
            if (extension1.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {
                con_foil = BitmapFactory.decodeFile(sortedList.get(i)[2].getAbsolutePath(), options);
                con_foilBlob = dop.jpgToByteArray(con_foil);
            } else if (extension1.equalsIgnoreCase("png")){
                con_foil = BitmapFactory.decodeFile(sortedList.get(i)[2].getAbsolutePath(), options);
                con_foilBlob = dop.pngToByteArray(con_foil);
            }

            try {
                audio = dop.audioToByteArray(sortedList.get(i)[4]);
            } catch (IOException io){
                audio = null;
            }


            if (sortedList.get(i)[3] != null) {
                String[] fileName4 = getFileName(sortedList.get(i)[0]);
                String extension4 = (fileName4[fileName1.length - 1].split("\\."))[1];
                if (extension1.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {
                    phon_foil = BitmapFactory.decodeFile(sortedList.get(i)[3].getAbsolutePath(), options);
                    phon_foilBlob = dop.jpgToByteArray(phon_foil);
                } else if (extension1.equalsIgnoreCase("png")){
                    phon_foil = BitmapFactory.decodeFile(sortedList.get(i)[3].getAbsolutePath(), options);
                    phon_foilBlob = dop.pngToByteArray(phon_foil);
                }
            }


            String word = fileName1[1];
            String type = "Practice";
            String difficulty = null;


            char[] questionIndex = fileName1[0].toCharArray();
            int questionNum = 0;
            //some numbers are greater than 10, so we account for that here.
            if (questionIndex.length == 3) {
                questionNum = Character.getNumericValue(questionIndex[1]);
            } else if (questionIndex.length == 4){
                questionNum = (Character.getNumericValue(questionIndex[1]) * 10) + Character.getNumericValue(questionIndex[2]);
            } else if (questionIndex.length == 5){
                questionNum = (Character.getNumericValue(questionIndex[1]) * 100) +
                        (Character.getNumericValue(questionIndex[2]) * 10) + Character.getNumericValue(questionIndex[3]) ;

            }
            if (sortedList.get(i)[3] != null){
                dop.addQuestion(dop, word, "PRACTICE", null, them_foilBlob, targetBlob, con_foilBlob,
                        phon_foilBlob, audio, newPISetID, questionNum, 4);

            } else {
                dop.addQuestion(dop, word, "PRACTICE", null, them_foilBlob, targetBlob, con_foilBlob,
                        null, audio, newPISetID, questionNum, 3);
            }
        }

    }




    //Beacuse other kinds of files exist can exist inside the directory and also beacuse we want to
    //break the process down, we will go through the directory and load all the files
    //into an array list of arrays; the array list is an array list beacuse we dont know
    //exactly how much files will be thrown out of the directory, but each array in the array list
    //has only 4 files; the test files will be sorted out by the prefix at the beginning of their file name.
    public ArrayList<File[]> getSortedFileList(File[] listOfImageFiles){
        ArrayList<File[]> sortedFiles = new ArrayList<File[]>();
        //need a hashmap to keep track of where in our array list an array with a certain question number
        //is stored.
        HashMap<Integer, Integer> indexer = new HashMap<Integer, Integer>();
        int newIndex = 0;
        int index = 0;
        int questionNum = 0;
        char itemLetter = ' ';
        for (int i = 0; i < listOfImageFiles.length; i++){
            String[] fileName = getFileName(listOfImageFiles[i]);
            char[] questionIndex = fileName[0].toCharArray();

            //some numbers are greater than 10, so we account for that here.
            if (questionIndex[0] == 'a') {
                if (questionIndex.length == 2){
                    questionNum = Character.getNumericValue(questionIndex[1]);
                } else if (questionIndex.length == 3) {
                    questionNum = (Character.getNumericValue(questionIndex[1]) * 10) + Character.getNumericValue(questionIndex[2]);
                }
            } else if (questionIndex[0] == 'p') {

                if ((questionIndex.length == 3)) {
                    questionNum = Character.getNumericValue(questionIndex[1]);
                } else if (questionIndex.length == 4) {
                    questionNum = (Character.getNumericValue(questionIndex[1]) * 10) + Character.getNumericValue(questionIndex[2]);

                } else if (questionIndex.length == 5) {
                    questionNum = (Character.getNumericValue(questionIndex[1]) * 100) +
                            (Character.getNumericValue(questionIndex[2]) * 10) + Character.getNumericValue(questionIndex[3]);

                }
            }

            //if there is no array of files having that questionNum
            if (indexer.get(questionNum) == null){
                indexer.put(questionNum, newIndex);
                newIndex++;
                //add a new array list of files to the array of array lists
                //the array is size 5, allowing for up to 4 image BLOBs and an audio BLOB
                sortedFiles.add(new File[5]);
                if (questionIndex[0] == 'a'){
                    itemLetter = 'z';
                } else if (questionIndex.length == 3){
                    itemLetter = questionIndex[2];
                } else if (questionIndex.length == 4){
                    itemLetter = questionIndex[3];
                }
                //add the file we are currently iterating on to that array
                if (itemLetter == 'a'){
                    (sortedFiles.get(sortedFiles.size() - 1))[0] = listOfImageFiles[i];
                } else if (itemLetter == 'b'){
                    (sortedFiles.get(sortedFiles.size() - 1))[1] = listOfImageFiles[i];
                } else if (itemLetter == 'c'){
                    (sortedFiles.get(sortedFiles.size() - 1))[2] = listOfImageFiles[i];
                } else if (itemLetter == 'd'){
                    (sortedFiles.get(sortedFiles.size() - 1))[3] = listOfImageFiles[i];
                } else if (itemLetter == 'z'){ //handling an audio file
                    (sortedFiles.get(sortedFiles.size() - 1))[4] = listOfImageFiles[i];
                }
                //if there is an array of files having that questionNum
            } else {
                index = indexer.get(questionNum);
                if (questionIndex[0] == 'a') {
                    itemLetter = 'z';
                } else if (questionIndex.length == 3){
                    itemLetter = questionIndex[2];
                } else if (questionIndex.length == 4){
                    itemLetter = questionIndex[3];
                }
                if (itemLetter == 'a'){
                    (sortedFiles.get(index))[0] = listOfImageFiles[i];
                } else if (itemLetter == 'b'){
                    (sortedFiles.get(index))[1] = listOfImageFiles[i];
                } else if (itemLetter == 'c'){
                    (sortedFiles.get(index)[2]) = listOfImageFiles[i];
                } else if (itemLetter == 'd'){
                    (sortedFiles.get(index))[3] = listOfImageFiles[i];
                } else if (itemLetter == 'z'){ //Handling an audio file
                    (sortedFiles.get(index))[4] = listOfImageFiles[i];
                }
                //go to the array in the arrayList having the
                // index of questionNum and insert
                //the file we are currently on.
            }
        }
        return sortedFiles;
    }


    public String[] getFileName(File file){
        String fullFilePath = file.getAbsolutePath();
        String[] splitFilePath = fullFilePath.split("/");
        String fileName =  splitFilePath[splitFilePath.length - 1];
        String[] brokenDownFileName = fileName.split("-");
        return brokenDownFileName;
    }


    //Activity that takes place at end of test loading process is everything goes successfully
    public void finishLoading(){
        //Code here for creating new test in database based
        //on given test name and conditions given.
        // you can go on
        Toast.makeText(getApplicationContext(),
                "Loaded new practice set " + newPISetName,
                Toast.LENGTH_LONG).show();
        dop.addNewPracItemSet(dop,newPISetID, newPISetName);
        resetUIItems();
    }


    //Method to set the items of the actual test to null.
    public void resetUIItems(){

        newPIDirField.setText("");
        newPISetField.setText("");
    }

    public int generatePISetID(DatabaseOperations DOP){
        int id = 1;
        Cursor CR = DOP.getPracItemSets(DOP);
        int numTests = CR.getCount();
        if (numTests == 0){
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


    public boolean isUniqueId(int newId, Cursor CR){
        do {
            if (newId == CR.getInt(1)) {
                return false;
            }
        }
        while (CR.moveToNext());
        return true;
    }

    public boolean isUnique(DatabaseOperations DOP, String piSetName){
        Cursor CR = DOP.getPracItemSets(DOP);
        int numPISets = CR.getCount();
        if (numPISets == 0){
            return true;
        }
        CR.moveToFirst();
        do {

            if (piSetName.equalsIgnoreCase(CR.getString(1))){
                return false;
            }
        }
        while (CR.moveToNext());
        return true;
    }



    public static boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

}
