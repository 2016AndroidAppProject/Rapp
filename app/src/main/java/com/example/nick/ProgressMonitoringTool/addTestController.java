package com.example.nick.ProgressMonitoringTool;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class addTestController extends AppCompatActivity {
    String picturesDirPath;
    File picturesDir;
    File newTestDir;
    String pathToNewTestDir;
    EditText newTestDirField;
    Button newTestButton;
    String newTestDirName;
    File[] imagesFiles;
    Bitmap[] images;

    String newTestName;
    EditText newTestNameField;

    String newTestType;
    RadioButton AdministratorOnly;
    RadioButton AdministratorAndTeacher;

    Spinner pracItemSpinner;
    ArrayAdapter<String> pracAdapter;
    String selectedPracItem;
    boolean pracItemSelected;


    DatabaseOperations dop;


    Context ctx;

    int newTestId;

    boolean validFileExtensions;
    boolean invalidFileContent;
    String invalidFileMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtest_screen);
        newTestDirField = (EditText) findViewById(R.id.newTestDirField);
        newTestButton = (Button) findViewById(R.id.addTest);
        ctx = this;

        newTestNameField = (EditText) findViewById(R.id.newTestNameField);

        AdministratorOnly = (RadioButton) findViewById(R.id.administratorOnly);
        AdministratorAndTeacher = (RadioButton) findViewById(R.id.administratorAndTeacher);
        pracItemSelected = false;

        selectedPracItem = "";

        validFileExtensions = true;

        invalidFileContent = false;

        invalidFileMessage = "";



        dop = new DatabaseOperations(ctx);

        pracItemSpinner = (Spinner) findViewById(R.id.pracItemSpinner);

        ArrayList<String> pracItemNames = dop.getPracNames();
        ArrayAdapter<String> pracAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, pracItemNames);
        pracAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pracItemSpinner.setAdapter(pracAdapter);

        if (pracItemNames.size() == 0){
            Toast.makeText(getBaseContext(), "There are no practice item sets currently registered", Toast.LENGTH_LONG).show();
        }

        pracItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (pracItemSelected == true) {
                    Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " selected", Toast.LENGTH_LONG).show();
                    selectedPracItem = (String) parent.getItemAtPosition(position);
                } else {
                    pracItemSelected = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });






//        picturesDirPath = Environment.getExternalStorageDirectory() + "/Pictures";
//        Bitmap bitmap1 = BitmapFactory.decodeFile(picturesDirPath + "/Test1/p9c-devise-verb-hi.jpg");
       // picturesDir =
               // Environment.DIRECTORY_PICTURES);



//        File subDirectory = new File(directory, "Test1");
//
//        File file = new File(subDirectory, "p9c-devise-verb-hi.jpg");//or any other format supported

//        try {
//      //      FileInputStream streamIn = new FileInputStream(file);
//
//            Bitmap bitmap = BitmapFactory.decodeStream(streamIn); //This gets the image
//
//            streamIn.close();
//        } catch (IOException ex){
//            System.out.println("Could not find file");
//        }

        AdministratorOnly.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                //handle the boolean flag here.
                if (arg1 == true) {
                    newTestType = "Administrator";
                }
                //Do something

                else {
                    //do something else

                }
            }
        });

        AdministratorAndTeacher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                //handle the boolean flag here.
                if (arg1 == true) {
                    newTestType = "Teacher";
                }
                //Do something

                else {
                    //do something else

                }
            }
        });

        newTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPracItem.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please select a practice item or select NO PRACTICE ITEM.",
                            Toast.LENGTH_LONG).show();
                } else {
                    //Detect if admin/teacher options have been set, if not display message
                    if (newTestType == null) {
                        Toast.makeText(getApplicationContext(),
                                "Please select either Administrator ONLY or Administrator and Teacher Only",
                                Toast.LENGTH_LONG).show();
                    } else {


                        //check if name field is filled, if not display message
                        if (newTestNameField.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter a test name for the new test",
                                    Toast.LENGTH_LONG).show();

                        } else {
                            newTestName = newTestNameField.getText().toString();
                            //check if entered name is unique (and thus valid), if not display message.
                            if (isUnique(dop, newTestName) == false) {
                                Toast.makeText(getApplicationContext(),
                                        "There is a test with the name " + newTestName + " already, please enter a different" +
                                                "name",
                                        Toast.LENGTH_LONG).show();
                                newTestName = null;
                                newTestNameField.setText("");
                            } else {
                                //Detect if test dir field has not been set, if not display message
                                //Detect if testDir could not be found, if not display message.
                                if (newTestDirField.getText().toString().equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            "Please enter the name of the folder where the test assets are located.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    //detect if external storage is mounted correctly
                                    // and readable!
                                    newTestDirName = newTestDirField.getText().toString();
                                    if (Environment.getExternalStorageState().startsWith(
                                            Environment.MEDIA_MOUNTED) || (isExternalStorageWritable() || isExternalStorageReadable())) {

                                        pathToNewTestDir = Environment.getExternalStorageDirectory() + "/" + newTestDirName + "/";
                                        newTestDir = new File(pathToNewTestDir);
                                        //detect if file created is an actual file
                                        if (newTestDir.exists()) {
                                            loadTest();

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
                }
            }
        });
    }

    public void loadTest() {
        //Loading the files from the actual directory and generating a new test id for the test to be created
        newTestId = generateTestID(dop);
        imagesFiles = newTestDir.listFiles();


        //sort the images from the directory by question
            ArrayList<File[]> filesSorted = getSortedFileList(imagesFiles);
            //create questions based on the files, and insert into question table
        if (invalidFileContent == true){
            Toast.makeText(getApplicationContext(),
                    invalidFileMessage,
                    Toast.LENGTH_LONG).show();
            invalidFileContent = false;
        } else if (validFileExtensions == true) {
            insertNewQuestions(filesSorted);
            //reset the add test UI and create a new test with the appropriate id.
            finishLoading();
        } else {
            Toast.makeText(getApplicationContext(),
                    "One or more files are an incorrect file type. Please make sure " +
                            "all image files are PNG, JPG, or JPEG, and all audio files" +
                            "are mp3. ",
                    Toast.LENGTH_LONG).show();
            validFileExtensions = true;
        }

    }





//        Log.v("Files", newTestDir.exists() + " EXISTS");
//        Log.v("Files", newTestDir.isDirectory() + "IS DIRECTORY");
//        Log.v("Files", newTestDir.listFiles() + " LIST FILES");






//        //This int declares how many items we should be inserting into a question.
//        int numItemsNeeded = 0;
//
//        //new question ids do not need to be unique, just unique among
//        //all questions having that id.
//        int newQuestionId = 1;
//
//        if (newTestType.equals("Administrator")) {
//            numItemsNeeded = 4;
//
//        } else if (newTestType.equals("Teacher")) {
//            numItemsNeeded = 3;
//        }
//        Bitmap them_foilPic;
//        Bitmap targetPic;
//        Bitmap con_foilPic;
//        Bitmap phon_foilPic;
//        byte[] them_foilBlob = null;
//        byte[] targetBlob = null;
//        byte[] con_foilBlob = null;
//        byte[] phon_foilBlob = null;
//        //A integer to count how many times we have grabbed a picture to put in our test.
//        int numItemsObtained = 0;
//        int questionsProcessed = 0;
//        try {
//            for (int i = 0; i < images.length; ++i) {
//
//                //Getting the name of the file.
//
//                String[] lastPart = itemName[itemName.length - 1].split("\\.");
//                if (lastPart[lastPart.length - 1].equals("jpg") ||
//                        lastPart[lastPart.length - 1].equals("jpeg") ||
//                        lastPart[lastPart.length - 1].equals("png")) {
//
//                    String extension = lastPart[lastPart.length - 1];
//                    char[] firstPart = itemName[0].toCharArray();
//
//
//                    if (firstPart[firstPart.length - 1] == 'a') {
//                        them_foilPic = images[i];
//                        if (extension.equals("png")) {
//                            them_foilBlob = dop.pngToByteArray(them_foilPic);
//                        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
//                            them_foilBlob = dop.jpgToByteArray(them_foilPic);
//                        }
//                    } else if (firstPart[firstPart.length - 1] == 'b') {
//                        targetPic = images[i];
//                        if (extension.equals("png")) {
//                            targetBlob = dop.pngToByteArray(targetPic);
//                        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
//                            targetBlob = dop.jpgToByteArray(targetPic);
//                        }
//                    } else if (firstPart[firstPart.length - 1] == 'c') {
//                        con_foilPic = images[i];
//                        if (extension.equals("png")) {
//                            con_foilBlob = dop.pngToByteArray(con_foilPic);
//                        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
//                            con_foilBlob = dop.jpgToByteArray(con_foilPic);
//                        }
//                    } else if (firstPart[firstPart.length - 1] == 'd') {
//                        phon_foilPic = images[i];
//                        if (extension.equals("png")) {
//                            phon_foilBlob = dop.pngToByteArray(phon_foilPic);
//                        } else if (extension.equals("jpg") || extension.equals("jpeg")) {
//                            phon_foilBlob = dop.jpgToByteArray(phon_foilPic);
//                        }
//                    }
//                    numItemsObtained++;
//                    if (numItemsObtained == numItemsNeeded) {
//                        numItemsObtained = 0;
//                        questionsProcessed++;
//                        newQuestionId++;
//                        if (numItemsNeeded == 4) {
//                            //addQuestion
//                            dop.addQuestion(dop, itemName[1], newTestType, itemName[2], itemName[3], them_foilBlob,
//                                    targetBlob, con_foilBlob, phon_foilBlob, null, newTestId, newQuestionId, 4);
//                        } else if (numItemsNeeded == 3) {
//                            dop.addQuestion(dop, itemName[1], newTestType, itemName[2], itemName[3], them_foilBlob,
//                                    targetBlob, con_foilBlob, phon_foilBlob, null, newTestId, newQuestionId, 3);
//                        }
//
//                    }
//                } else {
//                    String doNothing = "hahaha";
//                }
//            }
//
//
//        } catch (NullPointerException e) {
//            Toast.makeText(getApplicationContext(),
//                    "One of the sets of question items does not match the others. Please verify" +
//                            " that all sets of question items are the same length.",
//                    Toast.LENGTH_LONG).show();
//        }



    public void insertNewQuestions(ArrayList<File[]> sortedList){
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
                try {
                    targetBlob = dop.jpgToByteArray(target);
                } catch (IOException e){

                }
            } else if (extension1.equalsIgnoreCase("png")){
                target = BitmapFactory.decodeFile(sortedList.get(i)[1].getAbsolutePath(), options);
                try {
                    targetBlob = dop.pngToByteArray(target);
                } catch (IOException e){

                }
            }

            String[] fileName2 = getFileName(sortedList.get(i)[1]);
            String extension2 = (fileName2[fileName1.length - 1].split("\\."))[1];
            if (extension1.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {


                them_foil = BitmapFactory.decodeFile(sortedList.get(i)[0].getAbsolutePath(), options);
                try {
                    them_foilBlob = dop.jpgToByteArray(them_foil);
                } catch (IOException e){

                }
            } else if (extension1.equalsIgnoreCase("png")){

                them_foil = BitmapFactory.decodeFile(sortedList.get(i)[0].getAbsolutePath(), options);
                try {
                    them_foilBlob = dop.pngToByteArray(them_foil);
                } catch (IOException e){

                }
            }

            String[] fileName3 = getFileName(sortedList.get(i)[2]);
            String extension3 = (fileName3[fileName1.length - 1].split("\\."))[1];
            if (extension1.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {
                con_foil = BitmapFactory.decodeFile(sortedList.get(i)[2].getAbsolutePath(), options);
                try {
                    con_foilBlob = dop.jpgToByteArray(con_foil);
                } catch (IOException e){

                }
            } else if (extension1.equalsIgnoreCase("png")){
                con_foil = BitmapFactory.decodeFile(sortedList.get(i)[2].getAbsolutePath(), options);
                try {
                    con_foilBlob = dop.pngToByteArray(con_foil);
                } catch (IOException e){

                }
            }

            try {
                audio = dop.audioToByteArray(sortedList.get(i)[4]);
            } catch (IOException io){
                audio = null;
            }


            if (sortedList.get(i)[3] != null) {

                String[] fileName4 = getFileName(sortedList.get(i)[3]);
                String extension4 = (fileName4[fileName4.length - 1].split("\\."))[1];
                if (extension4.equalsIgnoreCase("jpg") || extension1.equalsIgnoreCase("jpeg")) {
                    phon_foil = BitmapFactory.decodeFile(sortedList.get(i)[3].getAbsolutePath(), options);
                    try {
                        phon_foilBlob = dop.jpgToByteArray(phon_foil);
                    } catch (IOException e){

                    }
                } else if (extension4.equalsIgnoreCase("png")){
                        phon_foil = BitmapFactory.decodeFile(sortedList.get(i)[3].getAbsolutePath(), options);
                    try {
                        phon_foilBlob = dop.pngToByteArray(phon_foil);
                    } catch (IOException e){

                    }
                }
            }


            String word = fileName1[1];
            String type = fileName1[2];
            String difficulty = null;
            if (fileName1.length >= 4) {
                difficulty = fileName1[3];
            }

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
                 dop.addQuestion(dop, word, type, difficulty, them_foilBlob, targetBlob, con_foilBlob,
                         phon_foilBlob, audio, newTestId, questionNum, 4);

            } else {
                dop.addQuestion(dop, word, type, difficulty, them_foilBlob, targetBlob, con_foilBlob,
                        null, audio, newTestId, questionNum, 3);
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
            String[] fileName = new String[0];
            fileName = getFileName(listOfImageFiles[i]);
            isFileContentValid(listOfImageFiles[i]);
            if ((validFileExtensions == false) || (invalidFileContent == true)){
                break;
            }
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


    public void isFileContentValid(File file){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap currentImage;
        byte[] currentBlob = null;
        byte[] audioBlob = null;

        boolean valid = false;
        String[] brokenDownFileName = new String[4];
        String fullFilePath = file.getAbsolutePath();
        String[] splitFilePath = fullFilePath.split("/");
        String fileName =  splitFilePath[splitFilePath.length - 1];
        brokenDownFileName = fileName.split("-");
        String[] lastPart = brokenDownFileName[brokenDownFileName.length - 1].split("\\.");

        if (lastPart[1].equalsIgnoreCase("jpg") || lastPart[1].equalsIgnoreCase("jpeg")) {
            try {
                currentImage = BitmapFactory.decodeFile(fullFilePath, options);
                currentBlob = dop.jpgToByteArray(currentImage);
            } catch (IOException e) {
                invalidFileContent = true;
                invalidFileMessage = "The file " + file.getPath() + " did not convert file types correctly." +
                        " Please convert the file again (Save it as a proper file type in a image editing program).";
            } catch (NullPointerException e){
                invalidFileContent = true;
                invalidFileMessage = "The file " + file.getPath() + " did not convert file types correctly." +
                        " Please convert the file again (Save it as a proper file type in a image editing program).";
            }
        } else if (lastPart[1].equalsIgnoreCase("png")){
            try {
                    currentImage = BitmapFactory.decodeFile(fullFilePath, options);
                    currentBlob = dop.jpgToByteArray(currentImage);
                } catch (IOException e){
                invalidFileContent = true;
                invalidFileMessage = "The file " + file.getPath() + " did not convert file types correctly." +
                        " Please convert the file again (Save it as a proper file type in a image editing program).";

            }
            catch (NullPointerException e){
                invalidFileContent = true;
                invalidFileMessage = "The file " + file.getPath() + " did not convert file types correctly." +
                        " Please convert the file again (Save it as a proper file type in a image editing program).";
            }
        } else if (lastPart[1].equalsIgnoreCase("mp3")){
            try {
                audioBlob = dop.audioToByteArray(file);
            } catch (IOException e){
                invalidFileContent = true;
                invalidFileMessage = "The file " + file.getPath() + " did not convert file types correctly." +
                        " Please convert the file again (Save it as a proper file type in a image editing program).";
            }
            catch (NullPointerException e){
                invalidFileContent = true;
                invalidFileMessage = "The file " + file.getPath() + " did not convert file types correctly." +
                        " Please convert the file again (Save it as a proper file type in a image editing program).";
            }

        }

    }


    public String[] getFileName(File file){
        String[] brokenDownFileName = new String[4];
        String fullFilePath = file.getAbsolutePath();
        String[] splitFilePath = fullFilePath.split("/");
        String fileName =  splitFilePath[splitFilePath.length - 1];
        brokenDownFileName = fileName.split("-");
        String[] lastPart = brokenDownFileName[brokenDownFileName.length - 1].split("\\.");
        if (lastPart[1].equalsIgnoreCase("jpg") || lastPart[1].equalsIgnoreCase("jpeg")  ||
                lastPart[1].equalsIgnoreCase("png") || lastPart[1].equalsIgnoreCase("mp3")){
            return brokenDownFileName;
        }
        validFileExtensions = false;
        return brokenDownFileName;

    }


    //Activity that takes place at end of test loading process is everything goes successfully
    public void finishLoading(){
        //Code here for creating new test in database based
        //on given test name and conditions given.
        // you can go on
        Toast.makeText(getApplicationContext(),
                "Loaded new test " + newTestName,
                Toast.LENGTH_LONG).show();
        int selectedPracID = dop.getPracIDByName(selectedPracItem);
        dop.addNewTest(dop, newTestName, newTestType, newTestId, selectedPracID);
        resetUIItems();
    }


    //Method to set the items of the actual test to null.
    public void resetUIItems(){
        //set all the items of the addTest interface to blank.
        AdministratorOnly.setChecked(false);
        AdministratorAndTeacher.setChecked(false);
        newTestDirField.setText("");
        newTestNameField.setText("");
    }

    public int generateTestID(DatabaseOperations DOP){
        int id = 1;
        Cursor CR = DOP.getTests(DOP);
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

    public boolean isUnique(DatabaseOperations DOP, String testName){
        Cursor CR = DOP.getTests(DOP);
        int numTests = CR.getCount();
        if (numTests == 0){
            return true;
        }
        CR.moveToFirst();
        do {

            if (testName.equalsIgnoreCase(CR.getString(2))){
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
