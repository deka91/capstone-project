package de.udacity.dk.cleverdroid.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.database.QuestionContract;

public class ResultFragment extends Fragment {

    @BindView(R.id.tv_score)
    TextView score;

    private String uriString;

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

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_favorite).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.bt_again)
    void repeateQuestions(View view) {
        Intent intent = new Intent(getActivity(), QuizActivity.class);
        intent.putExtra(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS.toString());
        startActivity(intent);
    }


}
