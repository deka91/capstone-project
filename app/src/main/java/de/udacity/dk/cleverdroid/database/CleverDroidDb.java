package de.udacity.dk.cleverdroid.database;

import android.provider.BaseColumns;

/**
 * Created by Deniz Kalem on 13.12.2017.
 */

public class CleverDroidDb {

    public static final class QuestionColumns implements BaseColumns {
        public static final String TABLE = "Question";
        public static final String TYPE = "type";
        public static final String FAVORITE = "favorite";
        public static final String QUESTION = "question";
        public static final String CHOICE1 = "choice1";
        public static final String CHOICE2 = "choice2";
        public static final String CHOICE3 = "choice3";
        public static final String CHOICE4 = "choice4";
        public static final String ANSWER = "answer";
        public static final String CORRECT = "correct";
    }
}