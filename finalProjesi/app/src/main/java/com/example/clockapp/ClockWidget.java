package com.example.clockapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockWidget extends AppWidgetProvider {
    private static final String TAG = "ClockWidget";
    private static final String ACTION_UPDATE_TIME = "com.example.clockapp.UPDATE_TIME";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate called with " + appWidgetIds.length + " widgets");
        // Tüm widget’ları güncelle
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
        // Düzenli güncelleme için AlarmManager’ı ayarla
        setUpdateAlarm(context);
    }

    private void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        Log.d(TAG, "Updating widget ID: " + appWidgetId);
        // Widget arayüzünü güncelle
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_widget);

        // Geçerli saati al
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        views.setTextViewText(R.id.widgetTimeText, time);
        Log.d(TAG, "Setting time to: " + time);

        // Widget’a tıklandığında MainActivity’yi aç
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widgetTimeText, pendingIntent);

        // Widget’ı güncelle
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private void setUpdateAlarm(Context context) {
        Log.d(TAG, "Setting update alarm");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ClockWidget.class);
        intent.setAction(ACTION_UPDATE_TIME);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Her dakika güncelle
        long interval = 60 * 1000; // 1 dakika
        long triggerAtMillis = System.currentTimeMillis() + interval;
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, triggerAtMillis, pendingIntent);
        Log.d(TAG, "Alarm set for: " + new Date(triggerAtMillis));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive called with action: " + intent.getAction());
        // Güncelleme olaylarını işle
        if (ACTION_UPDATE_TIME.equals(intent.getAction()) || AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisWidget = new ComponentName(context, ClockWidget.class);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
            Log.d(TAG, "Updating " + appWidgetIds.length + " widgets");
            for (int appWidgetId : appWidgetIds) {
                updateWidget(context, appWidgetManager, appWidgetId);
            }
            // Bir sonraki güncelleme için alarmı yeniden ayarla
            setUpdateAlarm(context);
        }
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(TAG, "onEnabled called");
        // İlk widget eklendiğinde AlarmManager’ı başlat
        setUpdateAlarm(context);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(TAG, "onDisabled called");
        // Son widget kaldırıldığında AlarmManager’ı iptal et
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ClockWidget.class);
        intent.setAction(ACTION_UPDATE_TIME);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
    }
}