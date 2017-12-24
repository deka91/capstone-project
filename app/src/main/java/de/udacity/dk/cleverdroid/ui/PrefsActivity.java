package de.udacity.dk.cleverdroid.ui;

import android.content.ContentValues;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.database.QuestionContract;
import de.udacity.dk.cleverdroid.database.QuestionContentProvider;

public class PrefsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference progress = findPreference("progress");
            progress.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    ContentValues values = new ContentValues();
                    values.put(QuestionContract.QuestionColumns.CORRECT, 0);
                    getActivity().getContentResolver().update(QuestionContentProvider.URI_QUESTIONS_WRONG, values, null, null);
                    Toast.makeText(getActivity(), "Your progress was deleted!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            Preference favorites = findPreference("favorites");
            favorites.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    ContentValues values = new ContentValues();
                    values.put(QuestionContract.QuestionColumns.FAVORITE, 0);
                    getActivity().getContentResolver().update(QuestionContentProvider.URI_QUESTIONS_FAVORITE, values, null, null);
                    Toast.makeText(getActivity(), "Your favorites were deleted!", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
