package de.udacity.dk.cleverdroid.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.udacity.dk.cleverdroid.R;

public class ResultFragment extends Fragment {

    @BindView(R.id.tv_score)
    TextView score;

    public ResultFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, view);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int userScore = bundle.getInt(getString(R.string.key_score));
            int questionsAmount = bundle.getInt(getString(R.string.key_questions_amount));
            score.setText(userScore + "/" + questionsAmount);
        }


        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_about).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    @OnClick(R.id.bt_again)
    void repeateQuestions(View view) {
        getFragmentManager().findFragmentByTag("yourstringtag");
    }


}
