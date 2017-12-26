package de.udacity.dk.cleverdroid.data;

import java.util.ArrayList;
import java.util.List;

import de.udacity.dk.cleverdroid.database.QuestionDbHelper;

/**
 * Created by Deniz Kalem on 16.12.2017.
 */

public class QuestionBank {

    List<Question> questions = new ArrayList<>();

    public int getFavorite(int i) {
        return questions.get(i).getFavorite();
    }

    public int getId(int i) {
        return questions.get(i).getId();
    }

    public int getType(int i) {
        return questions.get(i).getType();
    }

    public int getLength() {
        return questions.size();
    }

    public String getQuestion(int i) {
        return questions.get(i).getQuestion();
    }

    public String getChoice(int i, int num) {
        return questions.get(i).getChoice(num - 1);
    }

    public String getCorrectAnswer(int i) {
        return questions.get(i).getAnswer();
    }

    public void initQuestions(QuestionDbHelper questionDbHelper) {
        questions = questionDbHelper.getAllQuestions();
    }


}
