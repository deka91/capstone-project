package de.udacity.dk.cleverdroid.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import de.udacity.dk.cleverdroid.database.QuestionDbHelper;

/**
 * Created by Deniz Kalem on 16.12.2017.
 */

public class QuestionBank implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.questions);
    }

    public QuestionBank() {
    }

    protected QuestionBank(Parcel in) {
        this.questions = new ArrayList<Question>();
        in.readList(this.questions, Question.class.getClassLoader());
    }

    public static final Creator<QuestionBank> CREATOR = new Creator<QuestionBank>() {
        @Override
        public QuestionBank createFromParcel(Parcel source) {
            return new QuestionBank(source);
        }

        @Override
        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };
}
