package de.udacity.dk.cleverdroid.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class MyDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "cleverdroid.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
