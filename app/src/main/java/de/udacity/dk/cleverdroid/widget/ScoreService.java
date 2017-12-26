package de.udacity.dk.cleverdroid.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;

import de.udacity.dk.cleverdroid.database.QuestionContract;
import de.udacity.dk.cleverdroid.util.Constants;

/**
 * Created by Deniz Kalem on 26.12.2017.
 */

public class ScoreService extends IntentService {
    public static final String ACTION_GET_CURRENT_SCORE =
            "de.udacity.dk.cleverdroid.widget.action.get_current_score";

    public ScoreService() {
        super("ScoreService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_CURRENT_SCORE.equals(action)) {
                handleActionGetCurrentScore();
            }
        }
    }

    public static void startActionGetCurrentScore(Context context) {
        Intent intent = new Intent(context, ScoreService.class);
        intent.setAction(ACTION_GET_CURRENT_SCORE);
        context.startService(intent);

    }

    private void handleActionGetCurrentScore() {
        ContentResolver resolver = getContentResolver();

        Cursor cursor = resolver.query(QuestionContract.URI_QUESTIONS_CORRECT,
                null, null, null, null);

        String score = cursor.getCount() + "/" + Constants.TOTAL_QUESTIONS;

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, QuestionWidgetProvider.class));
        //Now update all widgets
        QuestionWidgetProvider.updateScore(this, appWidgetManager, score, appWidgetIds);
    }
}
