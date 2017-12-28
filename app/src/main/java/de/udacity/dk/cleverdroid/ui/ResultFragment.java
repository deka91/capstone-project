package de.udacity.dk.cleverdroid.ui;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.database.QuestionContract;

public class ResultFragment extends Fragment {

    @BindView(R.id.tv_score)
    TextView score;

    @BindView(R.id.bt_again)
    Button again;

    private String uriString;
    private boolean wrongQuestionsAvailable;
    private boolean favoriteQuestionsAvailable;
    private Intent intent;

    public ResultFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.result_screen));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int userScore = bundle.getInt(getString(R.string.key_score));
            int questionsAmount = bundle.getInt(getString(R.string.key_questions_amount));
            uriString = bundle.getString(getString(R.string.key_uri));
            score.setText(userScore + "/" + questionsAmount);
        }

        if (uriString.equals(QuestionContract.URI_QUESTIONS_WRONG.toString())) {
            FetchWrongQuestionsTask fetchWrongQuestionsTask = new FetchWrongQuestionsTask(new AsyncResponse() {

                @Override
                public void processFinish(Integer output) {
                    if (output != 0) {
                        wrongQuestionsAvailable = true;
                    } else {
                        again.setVisibility(View.INVISIBLE);
                    }

                }
            });
            fetchWrongQuestionsTask.execute();
        } else if (uriString.equals(QuestionContract.URI_QUESTIONS_FAVORITE.toString())) {
            FetchFavoriteQuestionsTask fetchFavoriteQuestionsTask = new FetchFavoriteQuestionsTask(new AsyncResponse() {

                @Override
                public void processFinish(Integer output) {
                    if (output != 0) {
                        favoriteQuestionsAvailable = true;
                    } else {
                        again.setVisibility(View.INVISIBLE);
                    }

                }
            });
            fetchFavoriteQuestionsTask.execute();
        }

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.bt_again)
    void repeateQuestions() {
        intent = new Intent(getActivity(), QuizActivity.class);
        if (uriString.equals(QuestionContract.URI_QUESTIONS.toString())) {
            intent.putExtra(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS.toString());
        } else if (uriString.equals(QuestionContract.URI_QUESTIONS_WRONG.toString())) {
            if (wrongQuestionsAvailable) {
                intent.putExtra(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_WRONG.toString());
            }
        } else if (uriString.equals(QuestionContract.URI_QUESTIONS_FAVORITE.toString())) {
            if (favoriteQuestionsAvailable) {
                intent.putExtra(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_FAVORITE.toString());
            }
        }
        getActivity().finish();
        startActivity(intent);
    }

    private int checkForWrongQuestions() {
        ContentResolver resolver = getActivity().getContentResolver();

        Cursor cursor = resolver.query(QuestionContract.URI_QUESTIONS_WRONG,
                null, null,
                null, null);
        cursor.close();
        return cursor.getCount();
    }

    private int checkForFavoriteQuestions() {
        ContentResolver resolver = getActivity().getContentResolver();

        Cursor cursor = resolver.query(QuestionContract.URI_QUESTIONS_FAVORITE,
                null, null,
                null, null);
        cursor.close();
        return cursor.getCount();
    }

    public interface AsyncResponse {
        void processFinish(Integer output);
    }

    public class FetchWrongQuestionsTask extends AsyncTask<Void, Void, Integer> {

        public AsyncResponse delegate = null;//Call back interface

        public FetchWrongQuestionsTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return checkForWrongQuestions();

        }

        @Override
        protected void onPostExecute(Integer result) {
            delegate.processFinish(result);
        }
    }

    public class FetchFavoriteQuestionsTask extends AsyncTask<Void, Void, Integer> {

        public AsyncResponse delegate = null;//Call back interface

        public FetchFavoriteQuestionsTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return checkForFavoriteQuestions();

        }

        @Override
        protected void onPostExecute(Integer result) {
            delegate.processFinish(result);
        }
    }

}