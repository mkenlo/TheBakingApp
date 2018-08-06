package com.mkenlo.baking.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mkenlo.baking.R;
import com.mkenlo.baking.db.AppExecutors;
import com.mkenlo.baking.db.BasicApp;
import com.mkenlo.baking.db.DataRepository;
import com.mkenlo.baking.db.model.Ingredient;
import com.mkenlo.baking.db.model.Recipe;


import java.util.List;

public class WidgetService extends RemoteViewsService {
    public WidgetService() {
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;
        private List<Ingredient> mWidgetItems;
        private int mChosenRecipeId;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            Recipe recipe = WidgetConfigureActivity.loadRecipePref(mContext, mAppWidgetId);
            mChosenRecipeId = recipe.getID();
        }

        @Override
        public void onDataSetChanged() {

            AppExecutors.getInstance().diskIO().execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            DataRepository repository = ((BasicApp) getApplication()).getRepository();
                            mWidgetItems = repository.getIngredientsByRecipeId(mChosenRecipeId);

                        }
                    });
        }

        @Override
        public void onDestroy() {
            if(mWidgetItems!=null)
                mWidgetItems.clear();
        }

        @Override
        public int getCount() {
            return (mWidgetItems!=null)? mWidgetItems.size():0;
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
            rv.setTextViewText(R.id.widget_item_text, mWidgetItems.get(position).toString());
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }


}
