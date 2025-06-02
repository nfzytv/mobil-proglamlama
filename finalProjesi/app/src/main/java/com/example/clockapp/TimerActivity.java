package com.example.clockapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private EditText hourInput, minuteInput;
    private TextView timerDisplay;
    private Button startButton, stopButton, resetButton, addToDbButton;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isTimerRunning;
    private TimerRecordAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        // UI bileşenlerini bağla
        hourInput = findViewById(R.id.hourInput);
        minuteInput = findViewById(R.id.minuteInput);
        timerDisplay = findViewById(R.id.timerDisplay);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resetButton = findViewById(R.id.resetButton);
        addToDbButton = findViewById(R.id.addToDbButton);

        // RecyclerView’ı başlat
        RecyclerView recyclerView = findViewById(R.id.timerRecordsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);
        List<TimerRecord> records = dbHelper.getAllTimerRecords();
        adapter = new TimerRecordAdapter(records);
        recyclerView.setAdapter(adapter);

        startButton.setOnClickListener(v -> startTimer());
        stopButton.setOnClickListener(v -> stopTimer());
        resetButton.setOnClickListener(v -> resetTimer());
        addToDbButton.setOnClickListener(v -> addToDatabase());
    }

    private void startTimer() {
        // Girişleri al
        String hourStr = hourInput.getText().toString();
        String minuteStr = minuteInput.getText().toString();

        int hours = hourStr.isEmpty() ? 0 : Integer.parseInt(hourStr);
        int minutes = minuteStr.isEmpty() ? 0 : Integer.parseInt(minuteStr);

        if (hours == 0 && minutes == 0) {
            Toast.makeText(this, "Lütfen süre girin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Süreyi milisaniyeye çevir
        timeLeftInMillis = (hours * 3600 + minutes * 60) * 1000;

        // Zamanlayıcıyı başlat
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                timerDisplay.setText("00:00");
                Toast.makeText(TimerActivity.this, "Zamanlayıcı bitti!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        isTimerRunning = true;
        updateTimerDisplay();
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isTimerRunning = false;
        timeLeftInMillis = 0;
        timerDisplay.setText("00:00");
    }

    private void updateTimerDisplay() {
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", hours * 60 + minutes, seconds);
        timerDisplay.setText(timeFormatted);
    }

    private void addToDatabase() {
        if (timeLeftInMillis == 0 && !isTimerRunning) {
            Toast.makeText(this, "Önce zamanlayıcıyı başlatın ve durdurun!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kalan süreyi al
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;

        // Veritabanına ekle
        boolean success = dbHelper.addTimerRecord(hours, minutes);

        if (success) {
            Toast.makeText(this, String.format("Süre kaydedildi: %02d:%02d", hours, minutes), Toast.LENGTH_SHORT).show();
            // RecyclerView’ı güncelle
            List<TimerRecord> records = dbHelper.getAllTimerRecords();
            adapter.updateRecords(records);
        } else {
            Toast.makeText(this, "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
        }
    }
}