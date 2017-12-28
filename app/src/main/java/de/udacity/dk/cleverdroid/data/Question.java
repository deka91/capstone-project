package de.udacity.dk.cleverdroid.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class Question implements Parcelable {
    private int id;
    private int type;
    private int favorite;
    private String question;
    private String[] choice = new String[4];
    private String answer;

    public Question() {

    }

    public Question(int id, int type, int favorite, String question, String[] choice, String answer) {
        this.id = id;
        this.type = type;
        this.favorite = favorite;
        this.question = question;
        this.choice[0] = choice[0];
        this.choice[1] = choice[1];
        this.choice[2] = choice[2];
        this.choice[3] = choice[3];
        this.answer = answer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoice(int i) {
        return choice[i];
    }

    public void setChoice(int i, String choice) {
        this.choice[i] = choice;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.type);
        dest.writeInt(this.favorite);
        dest.writeString(this.question);
        dest.writeStringArray(this.choice);
        dest.writeString(this.answer);
    }

    protected Question(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.favorite = in.readInt();
        this.question = in.readString();
        this.choice = in.createStringArray();
        this.answer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
