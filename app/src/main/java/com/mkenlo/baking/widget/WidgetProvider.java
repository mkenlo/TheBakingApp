package com.mkenlo.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mkenlo.baking.R;
import com.mkenlo.baking.RecipeDetailActivity;
import com.mkenlo.baking.db.model.Recipe;
import com.mkenlo.baking.utils.Constants;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetConfigureActivity WidgetConfigureActivity}
 */
public class WidgetProvider extends AppWidgetProvider {


    static RemoteViews updateAppWidget(Context context, int appWidgetId) {

        Recipe recipe = WidgetConfigureActivity.loadRecipePref(context, appWidgetId);
       // Recipe recipe = new DataBuilder(context).getData().get(recipeId - 1);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_recipe_name, recipe.getName());

        //open detail Activity for the widget element
        Intent recipeDetailActivityIntent = new Intent(context, RecipeDetailActivity.class);
        recipeDetailActivityIntent.putExtra(Constants.KEY_ITEM_RECIPE_ID, recipe.getID());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,0, recipeDetailActivityIntent,0);
        views.setOnClickPendingIntent(R.id.appwidget_recipe_name, pendingIntent);

        Intent intentService = new Intent(context, WidgetService.class);
        intentService.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        views.setRemoteAdapter(R.id.appwidget_recipe_ingredients, intentService);

        return views;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            RemoteViews views = updateAppWidget(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_recipe_ingredients);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
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

