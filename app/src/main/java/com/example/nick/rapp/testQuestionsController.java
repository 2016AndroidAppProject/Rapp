package com.example.nick.rapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


////This class is part of the controller component of the model-view-controller design.

//This class is responsible for managing the userData and questionData while the user interacts
//with the questions. It is responsible for loading new questions into currentQuestionData, changing the audio and
//visual assets displayed on the testquestions_screen and figuring
// out if that question is of the type practice, filler, or test.
// It will only tell the user if they are incorrect if the question is of the type practice.
// and the class will properly store the results depending on the type of question answered.
// The class is also responsible for detecting when the user has concluded the test
// and will forward the user to the doneController at the end.

public class testQuestionsController extends AppCompatActivity {
    Intent proceedIntent;
    Button answerButton;
    TextView welcomeNotice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testquestions_screen);

        //Create intent to forward user to the real test items
        // (currently doneController) when user finishes all practice questions.
        //In the final version, it will have to detect if the user has answered two
        //practice questions correctly in a row before sending them to the
        //test items.
        proceedIntent = new Intent(this, doneController.class);


        /*
        TESTs USED TO VERIFY USERDATA IS FUNCTIONING CORRECTLY
        welcomeNotice = (TextView) findViewById(R.id.welcomeNotice);
        welcomeNotice.setText(currentUserData.getInstance().getUserType());*/


    }



    //THIS METHOD SHOULD BE BROKEN UP INTO SUB METHODS WHEN WE FINISH THE PROTOTYPE.

    //The answer method responds to a user clicking any option
    //from the set of radio buttons available on the question screen.
    //The method will detect user input and figure out if the user answered correctly. If the user answered
    // incorrectly and the question is a practiceQuestion, the controller will tell the tesyQuestion screen to prompt
    //the user to try again via a toast message. The method will forward them to the next question (currently
    //the good job screen) if they answered correctly by loading a new question into the currentQuestionData,
    //and displaying new items on the screen. The answer is responsible for detecting when the user
    //has answered all the items in the test, and forward them to the doneController accordingly.

    //The answer method is also responsible for controllign the storing of results, depending on the kind
    //of question answered.
    public void answer(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.answer1:
                startActivity(proceedIntent);
                break;
            case R.id.answer2:
                Toast.makeText(getApplicationContext(), "Incorrect, please try again",Toast.LENGTH_SHORT).show();
                break;
            case R.id.answer3:
                Toast.makeText(getApplicationContext(), "Incorrect, please try again",Toast.LENGTH_SHORT).show();
                break;


        }
    }
}
