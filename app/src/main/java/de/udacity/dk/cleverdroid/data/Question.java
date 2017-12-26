package de.udacity.dk.cleverdroid.data;

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class Question {
    private int id;
    private int type;
    private int favorite;
    private String question;
    private String[] choice = new String[4];
    private String answer;

    public Question() {

    }

    public Question(int type, int favorite, String question, String[] choice, String answer) {
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
}
