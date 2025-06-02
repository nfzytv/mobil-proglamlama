package com.example.clockapp;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class WorldClockActivity extends AppCompatActivity {
    private TextView worldClockTextView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);

        worldClockTextView = findViewById(R.id.worldClockTextView);
        updateWorldClock("America/New_York");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateWorldClock("America/New_York");
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateWorldClock(String timeZoneId) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(timeZoneId));
        worldClockTextView.setText("New York: " + sdf.format(new Date()));
    }
}