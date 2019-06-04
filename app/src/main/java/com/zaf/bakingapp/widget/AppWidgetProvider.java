package com.zaf.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.zaf.bakingapp.R;
import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.models.Ingredients;
import com.zaf.bakingapp.service.AppWidgetService;

import java.util.ArrayList;
import java.util.List;

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    private static final String SELECTED_CAKE = "WidgetSelectedCake";
    public static List<Ingredients> ingredients = new ArrayList<>();
    private static String cakeName;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        views.setTextViewText(R.id.tv_recipe_title_widget, cakeName);

        Intent intent = new Intent(context, AppWidgetService.class);
        views.setRemoteAdapter(R.id.lv_widget, intent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra(SELECTED_CAKE)) {
            Cake selectedCake = intent.getParcelableExtra(SELECTED_CAKE);
            cakeName = selectedCake.getName();
            ingredients = selectedCake.getIngredients();

            Toast.makeText(context, cakeName + " " + context.getResources().getString(R.string.toast_added_to_widget), Toast.LENGTH_SHORT).show();
        } else {
            cakeName = context.getResources().getString(R.string.no_cake_selected_title_widget);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName thisWidget = new ComponentName(context.getApplicationContext(), AppWidgetProvider.class);

        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget);

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            onUpdate(context, appWidgetManager, appWidgetIds);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }
}
