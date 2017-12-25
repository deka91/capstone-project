package de.udacity.dk.cleverdroid.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.adapter.RecyclerViewClickListener;
import de.udacity.dk.cleverdroid.adapter.UsecaseAdapter;
import de.udacity.dk.cleverdroid.database.QuestionContract;

public class MainFragment extends Fragment {

    private ArrayList<String> usecaseList = new ArrayList<>();
    private UsecaseAdapter usecaseAdapter;
    private RecyclerView recyclerView;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareUsecaseData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,
                container, false);
        recyclerView = view.findViewById(R.id.recycler_cards);

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                QuizFragment quizFragment = new QuizFragment();
                Bundle bundle = new Bundle();
                switch (position) {
                    case 0:
                        bundle.putString(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS.toString());
                        break;
                    case 1:
                        bundle.putString(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_WRONG.toString());
                        break;
                    case 2:
                        bundle.putString(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_FAVORITE.toString());
                        break;
                }
                if (position != 3) {
                    quizFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, quizFragment).addToBackStack(quizFragment.getClass().getName()).commit();
                }
            }
        };

        usecaseAdapter = new UsecaseAdapter(usecaseList, listener);

        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(usecaseAdapter);

        usecaseAdapter.notifyDataSetChanged();

        return view;
    }


    private void prepareUsecaseData() {
        String usecase = getString(R.string.main_start);
        usecaseList.add(usecase);
        usecase = getString(R.string.main_repeat);
        usecaseList.add(usecase);
        usecase = (getString(R.string.main_favorites));
        usecaseList.add(usecase);
        usecase = (getString(R.string.main_score));
        usecaseList.add(usecase);
    }


}
