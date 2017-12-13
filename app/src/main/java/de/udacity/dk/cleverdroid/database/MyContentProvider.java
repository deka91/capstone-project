package de.udacity.dk.cleverdroid.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class MyContentProvider extends ContentProvider {

    private MyDatabaseHelper myDatabaseHelper;
    private static final UriMatcher uriMatcher;
    private static final int ALL_QUESTIONS = 1;
    private static final int WRONG_QUESTIONS = 2;
    private static final int FAVORITE_QUESTIONS = 3;
    private static final String ALL_QUESTIONS_PATH = "all_questions";
    private static final String WRONG_QUESTIONS_PATH = "wrong_questions";
    private static final String FAVORITE_QUESTIONS_PATH = "favorite_questions";
    private static final String AUTHORITY = "de.udacity.dk.cleverdroid";
    public static final Uri ALL_QUESTIONS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + ALL_QUESTIONS_PATH);
    public static final Uri WRONG_QUESTIONS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + WRONG_QUESTIONS_PATH);
    public static final Uri FAVORITE_QUESTIONS_URI =
            Uri.parse("content://" + AUTHORITY + "/" + FAVORITE_QUESTIONS_PATH);
    private Cursor cursor;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ALL_QUESTIONS_PATH, ALL_QUESTIONS);
        uriMatcher.addURI(AUTHORITY, WRONG_QUESTIONS_PATH, WRONG_QUESTIONS);
        uriMatcher.addURI(AUTHORITY, FAVORITE_QUESTIONS_PATH, FAVORITE_QUESTIONS);
    }


    @Override
    public boolean onCreate() {
        myDatabaseHelper = new MyDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case ALL_QUESTIONS:
                String query = "SELECT * FROM Question, Answer WHERE ";
                cursor = db.rawQuery(query + selection, selectionArgs);
                break;
            case WRONG_QUESTIONS:
                break;
            case FAVORITE_QUESTIONS:
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_QUESTIONS:
                return "vnd.android.cursor.dir/vnd.de.udacity.dk.cleverdroid.all_questions";
            case WRONG_QUESTIONS:
                return "vnd.android.cursor.dir/vnd.de.udacity.dk.cleverdroid.wrong_questions";
            case FAVORITE_QUESTIONS:
                return "vnd.android.cursor.dir/vnd.de.udacity.dk.cleverdroid.favorite_questions";
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
