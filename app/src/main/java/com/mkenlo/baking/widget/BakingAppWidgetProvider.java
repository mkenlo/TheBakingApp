package com.mkenlo.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mkenlo.baking.R;
import com.mkenlo.baking.RecipeDetailActivity;
import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Recipe;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingAppWidgetConfigureActivity BakingAppWidgetConfigureActivity}
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    static RemoteViews updateAppWidget(Context context, int appWidgetId) {

        int recipeId = BakingAppWidgetConfigureActivity.loadRecipePref(context, appWidgetId);
        Recipe recipe = new DataUtils(context).getData().get(recipeId - 1);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        views.setTextViewText(R.id.appwidget_recipe_name, recipe.getName());

        //open detail Activity for the widget element
        Intent recipeDetailActivityIntent = new Intent(context, RecipeDetailActivity.class);
        recipeDetailActivityIntent.putExtra(RecipeDetailActivity.ARG_RECIPE_ID, recipeId);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,0, recipeDetailActivityIntent,0);
        views.setOnClickPendingIntent(R.id.appwidget_recipe_name, pendingIntent);

        Intent intentService = new Intent(context, BakingAppWidgetService.class);
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

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingAppWidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
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

