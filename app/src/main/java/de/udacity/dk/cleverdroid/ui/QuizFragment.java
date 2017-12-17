package de.udacity.dk.cleverdroid.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.data.QuestionBank;
import de.udacity.dk.cleverdroid.database.CleverDroidDb;
import de.udacity.dk.cleverdroid.database.MyDatabaseHelper;

public class QuizFragment extends Fragment {

    private MyDatabaseHelper myDatabaseHelper;
    private QuestionBank questionBank = new QuestionBank();
    private String correctAnswer;
    private int score;
    private int number = 0;
    private int clickCounter = 0;
    private Uri uri;
    private boolean questionIsAnswered;

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

    @BindView(R.id.rg_singlechoice)
    RadioGroup radioGroupSingleChoice;

    @BindView(R.id.rb_choice1)
    RadioButton singleChoice1;

    @BindView(R.id.rb_choice2)
    RadioButton singleChoice2;

    @BindView(R.id.rb_choice3)
    RadioButton singleChoice3;

    @BindView(R.id.rb_choice4)
    RadioButton singleChoice4;

    public QuizFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            uri = Uri.parse(bundle.getString(getString(R.string.key_usecase)));
        }

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
            Cursor cursor = resolver.query(uri,
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
        }
    }

    private void updateQuestion() {

        if (number < questionBank.getLength() && number >= 0) {
            if (questionBank.getType(number) == 1) {
                // Single Choice
                singleChoiceLayout.setVisibility(View.VISIBLE);
                singleChoice1.setText(questionBank.getChoice(number, 1));
                singleChoice2.setText(questionBank.getChoice(number, 2));
                singleChoice3.setText(questionBank.getChoice(number, 3));
                singleChoice4.setText(questionBank.getChoice(number, 4));
            } else {
                // Multiple Choice
                multipleChoiceLayout.setVisibility(View.VISIBLE);
                multipleChoice1.setText(questionBank.getChoice(number, 1));
                multipleChoice2.setText(questionBank.getChoice(number, 2));
                multipleChoice3.setText(questionBank.getChoice(number, 3));
                multipleChoice4.setText(questionBank.getChoice(number, 4));
            }
            questionNumber.setText(getString(R.string.quiz_title) + " " + number + "/" + questionBank.getLength());
            question.setText(questionBank.getQuestion(number));
        } else if (number > 0) {
            // last question
            ResultFragment resultFragment = new ResultFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.key_score), score);
            bundle.putInt(getString(R.string.key_questions_amount), questionBank.getLength());
            resultFragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, resultFragment).addToBackStack(resultFragment.getClass().getName()).commit();
        }
    }

    private void updateUserInterface() {
        if (!questionIsAnswered) {
            radioGroupSingleChoice.clearCheck();
            for (int i = 0; i < radioGroupSingleChoice.getChildCount(); i++) {
                radioGroupSingleChoice.getChildAt(i).setEnabled(true);
            }
            answer.setVisibility(View.INVISIBLE);
        }
        number++;
        clickCounter = 0;
        questionIsAnswered = false;
    }

    private void showAnswer(int type) {
        String userAnswer = "";
        if (number < questionBank.getLength()) {

            switch (type) {
                case 1:
                    int checkedRadioButtonId = radioGroupSingleChoice.getCheckedRadioButtonId();

                    switch (checkedRadioButtonId) {
                        case R.id.rb_choice1:
                            userAnswer = (String) singleChoice1.getText();
                            break;
                        case R.id.rb_choice2:
                            userAnswer = (String) singleChoice2.getText();
                            break;

                        case R.id.rb_choice3:
                            userAnswer = (String) singleChoice3.getText();
                            break;

                        case R.id.rb_choice4:
                            userAnswer = (String) singleChoice4.getText();
                            break;
                    }

                    for (int i = 0; i < radioGroupSingleChoice.getChildCount(); i++) {
                        radioGroupSingleChoice.getChildAt(i).setEnabled(false);
                    }
                    break;

                case 2:
                    break;

            }

            correctAnswer = questionBank.getCorrectAnswer(number);
            answer.setText(correctAnswer);
            answer.setVisibility(View.VISIBLE);
            ContentResolver resolver = getActivity().getContentResolver();
            ContentValues values = new ContentValues();

            if (correctAnswer.equals(userAnswer)) {
                values.put(CleverDroidDb.QuestionColumns.CORRECT, 1);
                int updateUserResult = resolver.update(uri,
                        values, null, null);

                answer.setBackgroundColor(getResources().getColor(R.color.correctBackground));
                score = score + 1;
            } else {
                values.put(CleverDroidDb.QuestionColumns.CORRECT, 0);
                int updateUserResult = resolver.update(uri,
                        values, null, null);
                answer.setBackgroundColor(getResources().getColor(R.color.wrongBackground));
            }

            clickCounter++;
        }
    }

    @OnClick(R.id.bt_next)
    void nextQuestion(View view) {
        // Singlechoice
        if (clickCounter == 0 && !questionIsAnswered) {
            if (questionBank.getType(number) == 1) {
                if (radioGroupSingleChoice.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), "Please answer the question.", Toast.LENGTH_SHORT).show();
                } else {
                    showAnswer(1);
                }

            } else if (questionBank.getType(number) == 2) {
                showAnswer(2);
            }


        } else {
            updateUserInterface();
            updateQuestion();
        }
    }

    @OnClick(R.id.bt_back)
    void previousQuestion(View view) {
        questionIsAnswered = true;
        number = number - 1;
        updateQuestion();
        showAnswer(questionBank.getType(number));
    }

}
