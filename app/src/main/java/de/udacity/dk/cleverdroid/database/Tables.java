package de.udacity.dk.cleverdroid.database;

import android.provider.BaseColumns;

/**
 * Created by Deniz Kalem on 13.12.2017.
 */

public class Tables {

    public static final class QuestionColumns implements BaseColumns {
        public static final String TABLE = "Question";
        public static final String TYPE = "type";
        public static final String FAVORITE = "favorite";
        public static final String TEXT = "question_text";
    }

    public static final class AnswerColumns implements BaseColumns {
        public static final String TABLE = "Answer";
        public static final String CORRECT = "correct";
        public static final String QUESTION_ID = "question_id";
        public static final String TEXT = "answer_text";
    }
}
