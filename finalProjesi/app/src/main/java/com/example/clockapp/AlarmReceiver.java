package com.example.clockapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm Çalıyor!", Toast.LENGTH_LONG).show();
        // Buraya alarm sesi çalma veya bildirim gönderme kodu eklenebilir
    }
}