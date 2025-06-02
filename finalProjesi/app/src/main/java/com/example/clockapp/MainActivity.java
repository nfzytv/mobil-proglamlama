package com.example.clockapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.clockButton).setOnClickListener(v -> {
            startActivity(new Intent(this, ClockActivity.class));
        });

        findViewById(R.id.alarmButton).setOnClickListener(v -> {
            startActivity(new Intent(this, AlarmActivity.class));
        });

        findViewById(R.id.stopwatchButton).setOnClickListener(v -> {
            startActivity(new Intent(this, StopwatchActivity.class));
        });

        findViewById(R.id.timerButton).setOnClickListener(v -> {
            startActivity(new Intent(this, TimerActivity.class));
        });

        findViewById(R.id.worldClockButton).setOnClickListener(v -> {
            startActivity(new Intent(this, WorldClockActivity.class));
        });
    }
}