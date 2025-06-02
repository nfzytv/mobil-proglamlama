package com.example.clockapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity {
    private EditText hourInput;
    private EditText minuteInput;
    private CheckBox mondayCheckBox, tuesdayCheckBox, wednesdayCheckBox, thursdayCheckBox,
            fridayCheckBox, saturdayCheckBox, sundayCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        // UI bileşenlerini bağla
        TextView alarmTextView = findViewById(R.id.alarmTextView);
        hourInput = findViewById(R.id.hourInput);
        minuteInput = findViewById(R.id.minuteInput);
        mondayCheckBox = findViewById(R.id.mondayCheckBox);
        tuesdayCheckBox = findViewById(R.id.tuesdayCheckBox);
        wednesdayCheckBox = findViewById(R.id.wednesdayCheckBox);
        thursdayCheckBox = findViewById(R.id.thursdayCheckBox);
        fridayCheckBox = findViewById(R.id.fridayCheckBox);
        saturdayCheckBox = findViewById(R.id.saturdayCheckBox);
        sundayCheckBox = findViewById(R.id.sundayCheckBox);
        Button setAlarmButton = findViewById(R.id.setAlarmButton);

        setAlarmButton.setOnClickListener(v -> setAlarm());
    }

    private void setAlarm() {
        // Kullanıcı girişlerini al
        String hourStr = hourInput.getText().toString();
        String minuteStr = minuteInput.getText().toString();

        // Giriş kontrolü
        if (hourStr.isEmpty() || minuteStr.isEmpty()) {
            Toast.makeText(this, "Lütfen saat ve dakika girin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = Integer.parseInt(hourStr);
        int minute = Integer.parseInt(minuteStr);

        if (hour < 0 || hour > 23) {
            Toast.makeText(this, "Saat 0-23 arasında olmalı!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (minute < 0 || minute > 59) {
            Toast.makeText(this, "Dakika 0-59 arasında olmalı!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tekrarlama günlerini kontrol et
        boolean[] repeatDays = new boolean[7];
        repeatDays[0] = sundayCheckBox.isChecked(); // Pazar
        repeatDays[1] = mondayCheckBox.isChecked(); // Pazartesi
        repeatDays[2] = tuesdayCheckBox.isChecked(); // Salı
        repeatDays[3] = wednesdayCheckBox.isChecked(); // Çarşamba
        repeatDays[4] = thursdayCheckBox.isChecked(); // Perşembe
        repeatDays[5] = fridayCheckBox.isChecked(); // Cuma
        repeatDays[6] = saturdayCheckBox.isChecked(); // Cumartesi

        boolean isRepeating = false;
        for (boolean day : repeatDays) {
            if (day) {
                isRepeating = true;
                break;
            }
        }

        // AlarmManager ve PendingIntent
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (isRepeating) {
            // Her seçilen gün için ayrı alarm ayarla
            for (int i = 0; i < 7; i++) {
                if (repeatDays[i]) {
                    setRepeatingAlarm(alarmManager, hour, minute, i);
                }
            }
            Toast.makeText(this, String.format("Tekrarlayan alarm %02d:%02d için ayarlandı!", hour, minute), Toast.LENGTH_SHORT).show();
        } else {
            // Tek seferlik alarm
            setOneTimeAlarm(alarmManager, hour, minute);
            Toast.makeText(this, String.format("Alarm %02d:%02d için ayarlandı!", hour, minute), Toast.LENGTH_SHORT).show();
        }
    }

    private void setOneTimeAlarm(AlarmManager alarmManager, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Eğer zaman geçmişse, bir sonraki güne ayarla
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private void setRepeatingAlarm(AlarmManager alarmManager, int hour, int minute, int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1); // Calendar.SUNDAY = 1, vb.

        // Eğer zaman geçmişse, bir sonraki haftaya ayarla
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        // Her gün için benzersiz bir requestCode kullan
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, dayOfWeek, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }
}