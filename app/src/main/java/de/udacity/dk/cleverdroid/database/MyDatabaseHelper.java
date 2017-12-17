package de.udacity.dk.cleverdroid.database;

import android.content.Context;
import android.database.Cursor;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.udacity.dk.cleverdroid.data.Question;

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class MyDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "cleverdroid.db";
    private static final int DATABASE_VERSION = 1;
    private Cursor cursor;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Question> getAllQuestions() {
        cursor = getCursor();
        List<Question> questions = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();

                int type = cursor.getInt(cursor.getColumnIndex(Tables.QuestionColumns.TYPE));
                question.setType(type);

                int favorite = cursor.getInt(cursor.getColumnIndex(Tables.QuestionColumns.FAVORITE));
                question.setFavorite(favorite);

                String questionText = cursor.getString(cursor.getColumnIndex(Tables.QuestionColumns.QUESTION));
                question.setQuestion(questionText);

                String choice1Text = cursor.getString(cursor.getColumnIndex(Tables.QuestionColumns.CHOICE1));
                question.setChoice(0, choice1Text);

                String choice2Text = cursor.getString(cursor.getColumnIndex(Tables.QuestionColumns.CHOICE2));
                question.setChoice(1, choice2Text);

                String choice3Text = cursor.getString(cursor.getColumnIndex(Tables.QuestionColumns.CHOICE3));
                question.setChoice(2, choice3Text);

                String choice4Text = cursor.getString(cursor.getColumnIndex(Tables.QuestionColumns.CHOICE4));
                question.setChoice(3, choice4Text);

                String answerText = cursor.getString(cursor.getColumnIndex(Tables.QuestionColumns.ANSWER));
                question.setAnswer(answerText);

                questions.add(question);
            } while (cursor.moveToNext());
            Collections.shuffle(questions);
        }

        return questions;
    }


    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }


}
