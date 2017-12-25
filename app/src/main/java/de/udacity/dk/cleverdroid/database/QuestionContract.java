package de.udacity.dk.cleverdroid.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Deniz Kalem on 13.12.2017.
 */

public class QuestionContract {

    public static final String PATH_QUESTIONS = "questions";
    public static final String AUTHORITY = "de.udacity.dk.cleverdroid";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri URI_QUESTIONS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUESTIONS).build();
    public static final Uri URI_QUESTIONS_WRONG =
            BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_QUESTIONS + "/wrong").build();
    public static final Uri URI_QUESTIONS_FAVORITE =
            BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_QUESTIONS + "/favorite").build();
    public static final Uri URI_QUESTIONS_CORRECT =
            BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_QUESTIONS + "/correct").build();


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
