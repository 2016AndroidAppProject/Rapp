package com.example.nick.rapp;

import android.provider.BaseColumns;

/**
 * Created by Nick on 7/19/2016.
 */
public class tableData {

    public tableData(){

    }



    public static abstract class USERINFO implements BaseColumns
    {
        public static final String USER_NAME = "user_name";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_TYPE = "user_type";
        public static final String USER_LASTNAME = "user_lastname";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "user_info";

    }

    public static abstract class STUDENTINFO implements BaseColumns
    {
        public static final String STUDENT_FIRSTNAME = "student_firstname";
        public static final String STUDENT_ID = "student_id";
        public static final String TEACHER_NAME = "teacher_name";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "student_info";

    }

    public static abstract class RESULTINDEX implements BaseColumns {

        public static final String STUDENT_ID = "student_id";
        public static final String RESULT_ID = "result_id";
        public static final String TIME = "date";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "results_index";
    }

    public static abstract class RESULTS implements BaseColumns {
        public static final String RESULT_ID = "result_id";
        public static final String QUESTION_ID = "question_id";
        public static final String FOIL_TYPE = "foil_type";
        public static final String WORD = "word";
        public static final String CORRECT = "correct";
        public static final String TEST_ID = "test_id";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "results";
    }

    public static abstract class QUESTIONS implements BaseColumns {
        public static final String TEST_ID = "test_id";
        public static final String QUESTION_ID = "question_id";
        public static final String THEM_FOIL = "them_foil";
        public static final String TARGET = "target";
        public static final String CON_FOIL = "con_foil";
        public static final String PHON_FOIL = "phon_foil";
        public static final String WORD = "word";
        public static final String AUDIO = "audio";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "questions";
    }

    public static abstract class TESTS implements BaseColumns {
        public static final String TEST_ID = "test_id";
        public static final String TEST_NAME = "test_name";
        public static final String TEST_TYPE = "test_type";

        public static final String DATABASE_NAME = "Rapp_info";

        public static final String TABLE_NAME = "tests";
    }
}
