package de.udacity.dk.cleverdroid.ui;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.data.QuestionBank;
import de.udacity.dk.cleverdroid.database.QuestionContract;
import de.udacity.dk.cleverdroid.database.QuestionDbHelper;

public class QuizFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = QuizFragment.class.getSimpleName();
    private static final int LOADER_ID = 0x02;
    private QuestionBank questionBank;
    private Map<Integer, Integer> userSelection;
    private String correctAnswer;
    private int score;
    private int number = 0;
    private int clickCounter = 0;
    private String uriString;
    private MenuItem favorite;
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

    @BindView(R.id.adView)
    AdView banner;

    public QuizFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.getParcelable(getString(R.string.key_question_bank)) != null) {
                questionBank = savedInstanceState.getParcelable(getString(R.string.key_question_bank));
            }

            if (savedInstanceState.getInt(getString(R.string.key_question_number)) != 0) {
                number = savedInstanceState.getInt(getString(R.string.key_question_number));
            }
        } else {
            questionBank = new QuestionBank();
            userSelection = new HashMap<>();
        }
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.quiz_screen));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            uriString = bundle.getString(getString(R.string.key_usecase));
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        banner.loadAd(adRequest);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        favorite = menu.findItem(R.id.action_favorite);
        checkIfQuestionIsInFavorites();
        favorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                addToFavorites();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<Cursor>(getContext()) {

            // Initialize a Cursor, this will hold all the task data
            Cursor questionData = null;

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (questionData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(questionData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }


            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                try {
                    // Get the content resolver
                    ContentResolver resolver = getActivity().getContentResolver();

                    Uri uri = Uri.parse(uriString);
                    return resolver.query(uri,
                            null, null, null, null);

                } catch (Exception e) {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                questionData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        QuestionDbHelper questionDbHelper = new QuestionDbHelper(getContext());
        questionDbHelper.setCursor(data);
        if (questionBank != null && questionBank.getLength() == 0) {
            questionBank.initQuestions(questionDbHelper);
        }
        updateQuestion();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void checkIfQuestionIsInFavorites() {
        if (questionBank.getLength() > 0) {
            if (isInFavorites(questionBank.getId(number)) == 1) {
                favorite.setIcon(R.drawable.ic_star_black_24dp);
            } else {
                favorite.setIcon(R.drawable
                        .ic_star_border_black_24dp);
            }
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
            int realQuestionNumber = number + 1;
            questionNumber.setText(getString(R.string.quiz_title) + " " + realQuestionNumber + "/" + questionBank.getLength());
            question.setText(questionBank.getQuestion(number));
            if (favorite != null) {
                checkIfQuestionIsInFavorites();
            }
        } else if (number > 0) {
            // last question
            ResultFragment resultFragment = new ResultFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(getString(R.string.key_score), score);
            bundle.putInt(getString(R.string.key_questions_amount), questionBank.getLength());
            bundle.putString("uri", uriString);
            resultFragment.setArguments(bundle);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, resultFragment).commit();
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
        int checkedRadioButtonId = 0;
        if (number < questionBank.getLength()) {
            if (!userSelection.containsKey(questionBank.getId(number))) {
                switch (type) {
                    case 1:
                        checkedRadioButtonId = radioGroupSingleChoice.getCheckedRadioButtonId();

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
            } else {
                int storedCheckedRadioButtonId = userSelection.get(questionBank.getId(number));

                switch (storedCheckedRadioButtonId) {
                    case R.id.rb_choice1:
                        singleChoice1.setChecked(true);
                        userAnswer = (String) singleChoice1.getText();
                        break;
                    case R.id.rb_choice2:
                        singleChoice2.setChecked(true);
                        userAnswer = (String) singleChoice2.getText();
                        break;
                    case R.id.rb_choice3:
                        singleChoice3.setChecked(true);
                        userAnswer = (String) singleChoice3.getText();
                        break;
                    case R.id.rb_choice4:
                        singleChoice4.setChecked(true);
                        userAnswer = (String) singleChoice4.getText();
                        break;
                }

                for (int i = 0; i < radioGroupSingleChoice.getChildCount(); i++) {
                    radioGroupSingleChoice.getChildAt(i).setEnabled(false);
                }
            }

            correctAnswer = questionBank.getCorrectAnswer(number);
            answer.setText(correctAnswer);
            answer.setVisibility(View.VISIBLE);

            if (correctAnswer.equals(userAnswer)) {
                answer.setBackgroundColor(getResources().getColor(R.color.correctBackground));
            } else {
                answer.setBackgroundColor(getResources().getColor(R.color.wrongBackground));
            }

            if (!userSelection.containsKey(questionBank.getId(number))) {
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();

                if (correctAnswer.equals(userAnswer)) {
                    values.put(QuestionContract.QuestionColumns.CORRECT, 1);
                    answer.setBackgroundColor(getResources().getColor(R.color.correctBackground));
                    score = score + 1;
                } else {
                    values.put(QuestionContract.QuestionColumns.CORRECT, 0);
                    answer.setBackgroundColor(getResources().getColor(R.color.wrongBackground));
                }

                Uri uri = Uri.parse(QuestionContract.URI_QUESTIONS + "/" + questionBank.getId(number));

                resolver.update(uri,
                        values, null, null);

                userSelection.put(questionBank.getId(number), checkedRadioButtonId);
            }

            clickCounter++;

        }
    }

    @OnClick(R.id.bt_next)
    void nextQuestion(View view) {
        // Singlechoice
        if ((clickCounter == 0 && !questionIsAnswered)) {
            if (questionBank.getType(number) == 1) {
                if (radioGroupSingleChoice.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getActivity(), getString(R.string.quiz_info_message), Toast.LENGTH_SHORT).show();
                } else {
                    showAnswer(1);
                }

            } else if (questionBank.getType(number) == 2) {
                showAnswer(2);
            }


        } else {
            updateUserInterface();
            updateQuestion();
            if (number < questionBank.getLength() && userSelection.containsKey(questionBank.getId(number))) {
                showAnswer(1);
            }
        }
    }

    @OnClick(R.id.bt_back)
    void previousQuestion(View view) {
        if (number != 0) {
            number = number - 1;
            questionIsAnswered = true;
            updateQuestion();
            clickCounter = 0;
            showAnswer(questionBank.getType(number));
        }
    }

    private void addToFavorites() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                return isInFavorites(questionBank.getId(number));
            }

            @Override
            protected void onPostExecute(Integer isInFavorites) {
                if (isInFavorites == 1) {
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            ContentValues values = new ContentValues();
                            values.put(QuestionContract.QuestionColumns.FAVORITE, 0);

                            Uri uri = Uri.parse(QuestionContract.URI_QUESTIONS + "/" + questionBank.getId(number));

                            return getActivity().getContentResolver().update
                                    (uri,
                                            values, null, null);
                        }

                        @Override
                        protected void onPostExecute(Integer rowsDeleted) {
                            favorite.setIcon(R.drawable
                                    .ic_star_border_black_24dp);
                        }
                    }.execute();
                } else {
                    // adding to favorites
                    new AsyncTask<Void, Void, Integer>() {
                        @Override
                        protected Integer doInBackground(Void... params) {
                            ContentValues values = new ContentValues();
                            values.put(QuestionContract.QuestionColumns.FAVORITE, 1);

                            Uri uri = Uri.parse(QuestionContract.URI_QUESTIONS + "/" + questionBank.getId(number));

                            return getActivity().getContentResolver().update
                                    (uri,
                                            values, null, null);
                        }

                        @Override
                        protected void onPostExecute(Integer returnUri) {
                            favorite.setIcon(R.drawable
                                    .ic_star_black_24dp);
                        }
                    }.execute();
                }
            }
        }.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.key_question_bank), questionBank);
        outState.putInt(getString(R.string.key_question_number), number);
    }

    private int isInFavorites(int id) {
        ContentResolver resolver = getActivity().getContentResolver();

        Cursor cursor = resolver.query(QuestionContract.URI_QUESTIONS,
                null, QuestionContract.QuestionColumns._ID + " = ? AND " + QuestionContract.QuestionColumns.FAVORITE + " = 1",
                new String[]{"" + id}, null);
        return cursor.getCount();
    }
}
