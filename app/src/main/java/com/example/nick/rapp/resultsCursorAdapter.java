package com.example.nick.rapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Nick on 8/22/2016.
 */
public class resultsCursorAdapter extends CursorAdapter {

    String word;
    String correct;
    String studentName;
    String date;
    int recordID;
    int numComplete;
    int numCorrect;
    int numQuestions;
    double percentageCorrect;
    double percentageComplete;
    DatabaseOperations dop;
    int recordFetched;
    Cursor recordCursor;
    Cursor settings;

    String testMode;
    HashMap<String, Double> wordsCorrectPercentages;

    public resultsCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, 0);
        dop = new DatabaseOperations(context);
        recordFetched = -1;
        recordCursor = null;
        settings = dop.getResultMode(dop);
        settings.moveToFirst();
        testMode = settings.getString(0);
        wordsCorrectPercentages = dop.getWordsCorrrect(dop, dop.getAllResults(dop));

    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor resultCursor) {




        // Find fields to populate in inflated template

        // Extract properties from cursor

        if (testMode.equals("disaggregated")) {
            TextView resultStudent = (TextView) view.findViewById(R.id.student);
            TextView resultWord = (TextView) view.findViewById(R.id.word);
            TextView resultCorrect = (TextView) view.findViewById(R.id.correct);
            TextView resultTestCompletionDate = (TextView) view.findViewById(R.id.testCompletionDate);

                            if (resultCursor.getInt(1) != recordFetched) {
                                recordCursor = dop.getCompletionRecordByID(dop, resultCursor.getInt(1));
                                recordCursor.moveToFirst();
                                recordFetched = resultCursor.getInt(1);
                            }


                            studentName = resultCursor.getString(3) + " got ";
                            word = resultCursor.getString(2);
                            if (resultCursor.getInt(4) == 1) {
                                correct = "Correct on ";
                            } else if (resultCursor.getInt(4) == 0) {
                                correct = "Incorrect on";
                            }
                            date = recordCursor.getString(3);
                            //percentageCorrect = ((double) recordCursor.getInt(2)) / recordCursor.getInt(0);
                            //percentageComplete = ((double) recordCursor.getInt(0)) / recordCursor.getInt(1);

                            // Populate fields with extracted properties
                            resultStudent.setText(studentName);
                            resultWord.setText(word);
                            resultCorrect.setText(correct);
                            resultTestCompletionDate.setText(date);
                            //resultTestPercentageCorrect.setText(String.valueOf(percentageCorrect));
                            //resultTestPercentageComplete.setText(String.valueOf(percentageComplete));
        } else
        if (testMode.equals("word")){


            TextView resultWord = (TextView) view.findViewById(R.id.word);
            TextView resultTestPercentageCorrect = (TextView) view.findViewById(R.id.percentageCorrect);
            word = resultCursor.getString(1);
            percentageCorrect = wordsCorrectPercentages.get(word) * 100;
            double finalPercCorrect = (double) Math.round(percentageCorrect * 100) / 100;

            resultWord.setText("Students answered  " + word + " correctly");
            resultTestPercentageCorrect.setText(String.valueOf(finalPercCorrect) + "% of the time");

        } else
        if (testMode.equals("wordAndChild")){

        } else
        if (testMode.equals("child")){
            TextView resultStudent = (TextView) view.findViewById(R.id.student);
            TextView resultTestPercentageCorrect = (TextView) view.findViewById(R.id.percentageCorrect);

            studentName = resultCursor.getString(1);
            percentageCorrect = ((double) resultCursor.getInt(2) / resultCursor.getInt(3));
            double finalPercCorrect = (double) Math.round(percentageCorrect * 100) / 100;

            resultStudent.setText(studentName + " answered correctly ");
            resultTestPercentageCorrect.setText(finalPercCorrect + "% of the time.");
        }
    }
}
