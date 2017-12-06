package de.udacity.dk.cleverdroid.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.adapter.RecyclerViewClickListener;
import de.udacity.dk.cleverdroid.adapter.UsecaseAdapter;
import de.udacity.dk.cleverdroid.data.Usecase;

public class MainFragment extends Fragment {

    private List<Usecase> usecaseList = new ArrayList<>();
    private UsecaseAdapter usecaseAdapter;
    private RecyclerView recyclerView;

    public MainFragment() {
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
                switch (position) {
                    case 0:
                        QuizFragment quizFragment = new QuizFragment();

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, quizFragment).addToBackStack(quizFragment.getClass().getName()).commit();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
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

        prepareUsecaseData();

        return view;
    }

    private void prepareUsecaseData() {
        Usecase usecase = new Usecase(getString(R.string.main_start));
        usecaseList.add(usecase);
        usecase = new Usecase(getString(R.string.main_repeat));
        usecaseList.add(usecase);
        usecase = new Usecase(getString(R.string.main_favorites));
        usecaseList.add(usecase);
        usecase = new Usecase(getString(R.string.main_score));
        usecaseList.add(usecase);

        usecaseAdapter.notifyDataSetChanged();
    }
}
