package com.mkenlo.baking.widget;


import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.mkenlo.baking.R;
import com.mkenlo.baking.model.DataUtils;
import com.mkenlo.baking.model.Ingredient;
import com.mkenlo.baking.widget.BakingAppWidgetConfigureActivity;


import java.util.List;

public class BakingAppWidgetService extends RemoteViewsService {
    public BakingAppWidgetService() {
    }


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }


    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

        private Context mContext;
        private int mAppWidgetId;
        private List<Ingredient> mWidgetItems;

        public ListRemoteViewsFactory(Context context, Intent intent) {
            mContext  = context;
            mAppWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            int recipeId = BakingAppWidgetConfigureActivity.loadRecipePref(mContext, mAppWidgetId);
            mWidgetItems = new DataUtils(mContext).getData().get(recipeId-1).getIngredients();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mWidgetItems.clear();
        }

        @Override
        public int getCount() {
            return mWidgetItems.size();
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
