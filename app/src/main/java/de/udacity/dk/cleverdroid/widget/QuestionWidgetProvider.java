package de.udacity.dk.cleverdroid.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.database.QuestionContract;
import de.udacity.dk.cleverdroid.ui.QuizActivity;

/**
 * Implementation of App Widget functionality.
 */
public class QuestionWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String score,
                                int appWidgetId) {

        Intent startIntent = new Intent(context, QuizActivity.class);
        startIntent.putExtra(context.getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS.toString());
        PendingIntent pendingStartIntent = PendingIntent.getActivity(context, 0, startIntent, 0);

        Intent repeatIntent = new Intent(context, QuizActivity.class);
        repeatIntent.putExtra(context.getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_WRONG.toString());
        PendingIntent pendingRepeatIntent = PendingIntent.getActivity(context, 1, repeatIntent, 0);

        Intent favoriteIntent = new Intent(context, QuizActivity.class);
        favoriteIntent.putExtra(context.getString(R.string.key_usecase), QuestionContract.URI_QUESTIONS_FAVORITE.toString());
        PendingIntent pendingFavoriteIntent = PendingIntent.getActivity(context, 2, favoriteIntent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_question);
        views.setTextViewText(R.id.tv_score, score);
        views.setOnClickPendingIntent(R.id.tv_start, pendingStartIntent);
        views.setOnClickPendingIntent(R.id.tv_repeat, pendingRepeatIntent);
        views.setOnClickPendingIntent(R.id.tv_favorites, pendingFavoriteIntent);

        Intent scoreIntent = new Intent(context, ScoreService.class);
        scoreIntent.setAction(ScoreService.ACTION_GET_CURRENT_SCORE);
        PendingIntent scorePendingIntent = PendingIntent.getService(context, 0, scoreIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_score, scorePendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        ScoreService.startActionGetCurrentScore(context);
    }

    public static void updateScore(Context context, AppWidgetManager appWidgetManager,
                                   String score, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, score, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

