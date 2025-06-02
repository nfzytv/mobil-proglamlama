package com.example.clockapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class StopwatchActivity extends AppCompatActivity {
    private TextView stopwatchTextView;
    private Button startStopwatchButton, resetStopwatchButton;
    private Handler handler = new Handler();
    private long startTime, timeInMilliseconds = 0L;
    private boolean stopwatchRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        stopwatchTextView = findViewById(R.id.stopwatchTextView);
        startStopwatchButton = findViewById(R.id.startStopwatchButton);
        resetStopwatchButton = findViewById(R.id.resetStopwatchButton);

        startStopwatchButton.setOnClickListener(v -> {
            if (!stopwatchRunning) {
                startStopwatch();
            } else {
                stopStopwatch();
            }
        });

        resetStopwatchButton.setOnClickListener(v -> resetStopwatch());
    }

    private void startStopwatch() {
        startTime = SystemClock.uptimeMillis();
        stopwatchRunning = true;
        startStopwatchButton.setText("Durdur");
        handler.postDelayed(updateStopwatch, 0);
    }

    private void stopStopwatch() {
        stopwatchRunning = false;
        startStopwatchButton.setText("Başlat");
        handler.removeCallbacks(updateStopwatch);
    }

    private void resetStopwatch() {
        stopwatchRunning = false;
        timeInMilliseconds = 0L;
        startStopwatchButton.setText("Başlat");
        stopwatchTextView.setText("00:00:000");
        handler.removeCallbacks(updateStopwatch);
    }

    private Runnable updateStopwatch = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            int secs = (int) (timeInMilliseconds / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (timeInMilliseconds % 1000);
            stopwatchTextView.setText(String.format(Locale.getDefault(), "%02d:%02d:%03d", mins, secs, milliseconds));
            handler.postDelayed(this, 10);
        }
    };
}