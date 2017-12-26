package de.udacity.dk.cleverdroid.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import de.udacity.dk.cleverdroid.R;
import de.udacity.dk.cleverdroid.ui.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class QuestionWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, String score,
                                int appWidgetId) {

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_question);
        views.setTextViewText(R.id.tv_score, score);
        views.setOnClickPendingIntent(R.id.tv_start, pendingIntent);
        views.setOnClickPendingIntent(R.id.tv_repeat, pendingIntent);
        views.setOnClickPendingIntent(R.id.tv_favorites, pendingIntent);

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

