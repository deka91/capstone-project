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

/**
 * Created by Deniz Kalem on 12.12.2017.
 */

public class QuestionContentProvider extends ContentProvider {

    private QuestionDbHelper questionDbHelper;
    private static final UriMatcher uriMatcher;

    private static final int ALL_QUESTIONS = 1;
    private static final int WRONG_QUESTIONS = 2;
    private static final int FAVORITE_QUESTIONS = 3;
    private static final int QUESTION_WITH_ID = 4;

    private static final String PATH_QUESTIONS = "questions";

    private static final String AUTHORITY = "de.udacity.dk.cleverdroid";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final Uri URI_QUESTIONS =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUESTIONS).build();
    public static final Uri URI_QUESTIONS_WRONG =
            BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_QUESTIONS + "/wrong").build();
    public static final Uri URI_QUESTIONS_FAVORITE =
            BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_QUESTIONS + "/favorite").build();

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS, ALL_QUESTIONS);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/wrong", WRONG_QUESTIONS);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/favorite", FAVORITE_QUESTIONS);
        uriMatcher.addURI(AUTHORITY, PATH_QUESTIONS + "/#", QUESTION_WITH_ID);
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
            case ALL_QUESTIONS:
                cursor = db.query(QuestionContract.QuestionColumns.TABLE, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case WRONG_QUESTIONS:
                String queryWrong = "SELECT * FROM " + QuestionContract.QuestionColumns.TABLE + " WHERE ";
                cursor = db.rawQuery(queryWrong + selection, selectionArgs);
                break;
            case FAVORITE_QUESTIONS:
                String queryFavorite = "SELECT * FROM " + QuestionContract.QuestionColumns.TABLE + " WHERE ";
                cursor = db.rawQuery(queryFavorite + selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

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

    //FIXME
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = questionDbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case QUESTION_WITH_ID:
                String id = uri.getPathSegments().get(1);
                selection = QuestionContract.QuestionColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            case WRONG_QUESTIONS:
//                selection =
//                        (!TextUtils.isEmpty(selection) ?
//                                " AND (" + "correct = 0" + ')' : "");
                break;
            case FAVORITE_QUESTIONS:
                // UPDATE Question SET favorite=0
//                selection = (!TextUtils.isEmpty(selection) ?
//                        " AND (" + "favorite = 0" + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        int updateCount = db.update(QuestionContract.QuestionColumns.TABLE, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }
}
