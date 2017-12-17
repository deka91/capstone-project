package de.udacity.dk.cleverdroid.ui;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.data.QuestionBank;
import de.udacity.dk.cleverdroid.database.MyDatabaseHelper;

import static de.udacity.dk.cleverdroid.database.MyContentProvider.ALL_QUESTIONS_URI;

public class QuizFragment extends Fragment {

    private MyDatabaseHelper myDatabaseHelper;
    private QuestionBank questionBank = new QuestionBank();
    private int score;
    private int number = 0;

    @BindView(R.id.tv_question_number)
    TextView questionNumber;

    @BindView(R.id.tv_question)
    TextView question;

    @BindView(R.id.tv_answer)
    TextView answer;

    @BindView(R.id.layout_multiplechoice)
    LinearLayout multipleChoiceLayout;

    @BindView(R.id.layout_singlechoice)
    LinearLayout singleChoiceLayout;

    @BindView(R.id.cb_choice1)
    CheckBox multipleChoice1;

    @BindView(R.id.cb_choice2)
    CheckBox multipleChoice2;

    @BindView(R.id.cb_choice3)
    CheckBox multipleChoice3;

    @BindView(R.id.cb_choice4)
    CheckBox multipleChoice4;

    @BindView(R.id.rb_choice1)
    RadioButton singleChoice1;

    @BindView(R.id.rb_choice2)
    RadioButton singleChoice2;

    @BindView(R.id.rb_choice3)
    RadioButton singleChoice3;

    @BindView(R.id.rb_choice4)
    RadioButton singleChoice4;

    @BindView(R.id.bt_next)
    Button next;

    @BindView(R.id.bt_back)
    Button back;

    public QuizFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);
        new QuestionFetchTask().execute();
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    public class QuestionFetchTask extends AsyncTask<Void, Void, Cursor> {

        // Invoked on a background thread
        @Override
        protected Cursor doInBackground(Void... params) {
            // Make the query to get the cursor

            // Get the content resolver
            ContentResolver resolver = getActivity().getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class
            Cursor cursor = resolver.query(ALL_QUESTIONS_URI,
                    null, "", null, null);
            return cursor;
        }


        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            myDatabaseHelper = new MyDatabaseHelper(getContext());
            myDatabaseHelper.setCursor(cursor);
            questionBank.initQuestions(myDatabaseHelper);
            updateQuestion();
            updateScore(score);
        }
    }

    private void updateQuestion() {

        if (number < questionBank.getLength()) {
            if (questionBank.getType(number) == 1) {
                // Single Choice
                multipleChoiceLayout.setVisibility(View.VISIBLE);
                multipleChoice1.setText(questionBank.getChoice(number, 1));
                multipleChoice2.setText(questionBank.getChoice(number, 2));
                multipleChoice3.setText(questionBank.getChoice(number, 3));
                multipleChoice4.setText(questionBank.getChoice(number, 4));
            } else {
                // Multiple Choice
                singleChoiceLayout.setVisibility(View.VISIBLE);
                singleChoice1.setText(questionBank.getChoice(number, 1));
                singleChoice2.setText(questionBank.getChoice(number, 2));
                singleChoice3.setText(questionBank.getChoice(number, 3));
                singleChoice4.setText(questionBank.getChoice(number, 4));
            }

            question.setText(questionBank.getQuestion(number));
            answer.setText(questionBank.getCorrectAnswer(number));
            number++;
            questionNumber.setText(getString(R.string.quiz_title) + " " + number + "/" + questionBank.getLength());
        } else {
            // last question
//            ResultFragment resultFragment = new ResultFragment();
//
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragment_container, resultFragment).addToBackStack(resultFragment.getClass().getName()).commit();
        }
    }

    private void updateScore(int score) {

    }

    @OnClick(R.id.bt_next)
    void nextQuestion(View view) {
        updateQuestion();
    }

    @OnClick(R.id.bt_back)
    void previousQuestion(View view) {
        updateQuestion();
    }

}
