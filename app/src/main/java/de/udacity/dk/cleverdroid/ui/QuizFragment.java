package de.udacity.dk.cleverdroid.ui;


import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.database.Tables;

import static de.udacity.dk.cleverdroid.database.MyContentProvider.ALL_QUESTIONS_URI;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizFragment extends Fragment {

    private Cursor data;
    private int questionCol, answerCol;

    @BindView(R.id.tv_question)
    TextView question;

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
        ButterKnife.bind(this, view);
        new QuestionFetchTask().execute();
        return view;
    }

    public class QuestionFetchTask extends AsyncTask<Void, Void, Cursor> {

        // Invoked on a background thread
        @Override
        protected Cursor doInBackground(Void... params) {
            // Make the query to get the data

            // Get the content resolver
            ContentResolver resolver = getActivity().getContentResolver();

            // Call the query method on the resolver with the correct Uri from the contract class
            Cursor cursor = resolver.query(ALL_QUESTIONS_URI,
                    null, "Question._id = Answer.question_id", null, null);
            return cursor;
        }


        // Invoked on UI thread
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            data = cursor;

            questionCol = data.getColumnIndex(Tables.QuestionColumns.TEXT);

            nextQuestion(getView());
        }
    }

    public void nextQuestion(View view) {

        if (data != null) {
            if (!data.moveToNext()) {
                data.moveToFirst();
            }

            question.setText(data.getString(questionCol));
        }
    }

    public void previousQuestion(View view) {

    }

}
