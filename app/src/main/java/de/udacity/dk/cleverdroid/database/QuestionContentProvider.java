package de.udacity.dk.cleverdroid.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static de.udacity.dk.cleverdroid.database.QuestionContract.AUTHORITY;
import static de.udacity.dk.cleverdroid.database.QuestionContract.PATH_QUESTIONS;

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class QuestionContentProvider extends ContentProvider {

    private QuestionDbHelper questionDbHelper;
    private static final UriMatcher uriMatcher;

    private static final int QUESTIONS_ALL = 1;
    private static final int QUESTIONS_WRONG = 2;
    private static final int QUESTIONS_FAVORITE = 3;
    private static final int QUESTIONS_CORRECT = 4;
    private static final int QUESTIONS_ID = 5;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS, QUESTIONS_ALL);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/wrong", QUESTIONS_WRONG);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/favorite", QUESTIONS_FAVORITE);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/correct", QUESTIONS_CORRECT);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/#", QUESTIONS_ID);
    }

    @Override
    public boolean onCreate() {
        questionDbHelper = new QuestionDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = questionDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case QUESTIONS_ALL:
                break;
            case QUESTIONS_WRONG:
                selection = "correct = 0";
                break;
            case QUESTIONS_FAVORITE:
                selection = "favorite = 1";
                break;
            case QUESTIONS_CORRECT:
                selection = "correct = 1";
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        cursor = db.query(QuestionContract.QuestionColumns.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = questionDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case QUESTIONS_ID:
                String id = uri.getPathSegments().get(1);
                selection = QuestionContract.QuestionColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            case QUESTIONS_WRONG:
                contentValues = new ContentValues();
                contentValues.put(QuestionContract.QuestionColumns.CORRECT, 0);
                break;
            case QUESTIONS_FAVORITE:
                contentValues = new ContentValues();
                contentValues.put(QuestionContract.QuestionColumns.FAVORITE, 0);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int updateCount = db.update(QuestionContract.QuestionColumns.TABLE, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
