package de.udacity.dk.cleverdroid.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import de.udacity.dk.cleverdroid.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            MainFragment mainFragment = new MainFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mainFragment).commit();
        }
    }
}
