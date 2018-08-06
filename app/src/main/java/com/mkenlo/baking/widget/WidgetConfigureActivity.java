package com.mkenlo.baking.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.mkenlo.baking.R;
import com.mkenlo.baking.db.AppExecutors;
import com.mkenlo.baking.db.BasicApp;
import com.mkenlo.baking.db.DataRepository;
import com.mkenlo.baking.db.model.Recipe;
import java.util.List;


/**
 * The configuration screen for the {@link WidgetProvider WidgetProvider} AppWidget.
 */
public class WidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "com.mkenlo.baking.widget.WidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private ListView mRecipeList;
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    SimpleListAdapter mAdapter;


    public WidgetConfigureActivity() {
        super();
    }


    static void saveRecipePref(Context context, int appWidgetId, Recipe recipe){
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipe.getID());
        prefs.putString(PREF_PREFIX_KEY +"_VALUE_"+ appWidgetId, recipe.getName());
        prefs.apply();
    }

    static Recipe loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Recipe  recipe = new Recipe();
        recipe.setID(prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0));
        recipe.setName(prefs.getString(PREF_PREFIX_KEY+"_VALUE_" + appWidgetId,"Recipe"));
        return recipe;
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_app_widget_configure);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mRecipeList = findViewById(R.id.appwidget_recipe_list);
        mAdapter = new SimpleListAdapter();

       populateListRecipe();
    }

    private void populateListRecipe(){

        AppExecutors.getInstance().diskIO().execute(
            new Runnable() {
                @Override
                public void run() {
                    DataRepository repository = ((BasicApp) getApplication()).getRepository();
                    mRecipeList.setAdapter(mAdapter);
                    mAdapter.setItems(repository.getRecipes());
                    mAdapter.notifyDataSetChanged();
                }
            });
    }

    private void launchAppWidget(Recipe recipe){
        final Context context = WidgetConfigureActivity.this;

        // When a recipe is clicked, its Id is saved in Preferences then the widget is created/updated
        saveRecipePref(context, mAppWidgetId, recipe);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews rv = WidgetProvider.updateAppWidget(context, mAppWidgetId);
        appWidgetManager.updateAppWidget(mAppWidgetId, rv);


        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }


    private class SimpleListAdapter extends BaseAdapter {


        List mItems;
        View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAppWidget((Recipe) v.getTag());
            }
        };

        public SimpleListAdapter(){
        }

        public void setItems(List items){
            mItems = items;}

        @Override
        public int getCount() {
            return (mItems!=null)? mItems.size():0;
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Object item = getItem(position);
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.widget_list_item, parent, false);
            }


            ((TextView) convertView.findViewById(R.id.widget_item_text))
                    .setText(item.toString());
            convertView.setTag(item);
            convertView.setOnClickListener(mOnClickListener);

            return convertView;
        }


    }

}

