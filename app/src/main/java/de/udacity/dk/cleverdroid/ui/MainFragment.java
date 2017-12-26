package de.udacity.dk.cleverdroid.ui;

import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.adapter.RecyclerViewClickListener;
import de.udacity.dk.cleverdroid.adapter.UsecaseAdapter;
import de.udacity.dk.cleverdroid.analytics.AnalyticsApplication;
import de.udacity.dk.cleverdroid.database.QuestionContract;
import de.udacity.dk.cleverdroid.util.Constants;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = MainFragment.class.getSimpleName();
    private static final int SCORE_LOADER = 1;
    private static final int WRONG_QUESTIONS_LOADER = 2;
    private static final int FAVORITE_QUESTIONS_LOADER = 3;
    private int wrongQuestions, favoriteQuestions;
    private ArrayList<String> usecaseList = new ArrayList<>();
    private UsecaseAdapter usecaseAdapter;
    private RecyclerView recyclerView;
    private Tracker tracker;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();
        prepareUsecaseData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,
                container, false);
        recyclerView = view.findViewById(R.id.recycler_cards);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.app_name));

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle bundle = new Bundle();
                boolean startQuiz = true;
                switch (position) {
                    case 0:
                        bundle.putString(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS.toString());
                        Log.i(TAG, "Setting screen name: " + (Constants.SCREEN_QUESTIONS_ALL));
                        tracker.setScreenName(Constants.SCREEN_QUESTIONS_ALL);
                        break;
                    case 1:
                        bundle.putString(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_WRONG.toString());
                        Log.i(TAG, "Setting screen name: " + Constants.SCREEN_QUESTIONS_WRONG);
                        tracker.setScreenName(Constants.SCREEN_QUESTIONS_WRONG);
                        if (wrongQuestions == 0) {
                            startQuiz = false;
                            Toast.makeText(getContext(), "You don't have wrong answered questions.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        bundle.putString(getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_FAVORITE.toString());
                        Log.i(TAG, "Setting screen name: " + Constants.SCREEN_QUESTIONS_FAVORITE);
                        tracker.setScreenName(Constants.SCREEN_QUESTIONS_FAVORITE);
                        if (favoriteQuestions == 0) {
                            startQuiz = false;
                            Toast.makeText(getContext(), "You don't have favorite marked questions.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                if (position != 3 && startQuiz == true) {
                    tracker.send(new HitBuilders.ScreenViewBuilder().build());
                    QuizFragment quizFragment = new QuizFragment();
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

        getLoaderManager().initLoader(SCORE_LOADER, null, this);
        getLoaderManager().initLoader(WRONG_QUESTIONS_LOADER, null, this);
        getLoaderManager().initLoader(FAVORITE_QUESTIONS_LOADER, null, this);
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case SCORE_LOADER:
                return new AsyncTaskLoader<Cursor>(getContext()) {

                    // Initialize a Cursor, this will hold all the task data
                    Cursor scoreData = null;

                    // onStartLoading() is called when a loader first starts loading data
                    @Override
                    protected void onStartLoading() {
                        if (scoreData != null) {
                            // Delivers any previously loaded data immediately
                            deliverResult(scoreData);
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

                            return resolver.query(QuestionContract.URI_QUESTIONS_CORRECT,
                                    null, null, null, null);

                        } catch (Exception e) {
                            Log.e(TAG, "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return null;
                        }
                    }

                    // deliverResult sends the result of the load, a Cursor, to the registered listener
                    public void deliverResult(Cursor data) {
                        scoreData = data;
                        super.deliverResult(data);
                    }
                };
            case WRONG_QUESTIONS_LOADER:
                return new AsyncTaskLoader<Cursor>(getContext()) {

                    // Initialize a Cursor, this will hold all the task data
                    Cursor wrongData = null;

                    // onStartLoading() is called when a loader first starts loading data
                    @Override
                    protected void onStartLoading() {
                        if (wrongData != null) {
                            // Delivers any previously loaded data immediately
                            deliverResult(wrongData);
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

                            return resolver.query(QuestionContract.URI_QUESTIONS_WRONG,
                                    null, null, null, null);

                        } catch (Exception e) {
                            Log.e(TAG, "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return null;
                        }
                    }

                    // deliverResult sends the result of the load, a Cursor, to the registered listener
                    public void deliverResult(Cursor data) {
                        wrongData = data;
                        super.deliverResult(data);
                    }
                };

            case FAVORITE_QUESTIONS_LOADER:
                return new AsyncTaskLoader<Cursor>(getContext()) {

                    // Initialize a Cursor, this will hold all the task data
                    Cursor favoriteData = null;

                    // onStartLoading() is called when a loader first starts loading data
                    @Override
                    protected void onStartLoading() {
                        if (favoriteData != null) {
                            // Delivers any previously loaded data immediately
                            deliverResult(favoriteData);
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

                            return resolver.query(QuestionContract.URI_QUESTIONS_FAVORITE,
                                    null, null, null, null);

                        } catch (Exception e) {
                            Log.e(TAG, "Failed to asynchronously load data.");
                            e.printStackTrace();
                            return null;
                        }
                    }

                    // deliverResult sends the result of the load, a Cursor, to the registered listener
                    public void deliverResult(Cursor data) {
                        favoriteData = data;
                        super.deliverResult(data);
                    }
                };

            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId()) {
            case SCORE_LOADER:
                Log.i(TAG, "The current score is: " + data.getCount() + "/" + Constants.TOTAL_QUESTIONS);
                usecaseList.remove(3);
                usecaseAdapter.notifyItemRemoved(3);
                usecaseList.add(data.getCount() + "/" + Constants.TOTAL_QUESTIONS);
                usecaseAdapter.notifyItemInserted(3);
                break;
            case WRONG_QUESTIONS_LOADER:
                wrongQuestions = data.getCount();
                break;
            case FAVORITE_QUESTIONS_LOADER:
                favoriteQuestions = data.getCount();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
