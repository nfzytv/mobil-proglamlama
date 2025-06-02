package com.example.clockapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ClockApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "timer_records";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_HOURS = "hours";
    private static final String COLUMN_MINUTES = "minutes";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_HOURS + " INTEGER, " +
                COLUMN_MINUTES + " INTEGER, " +
                COLUMN_TIMESTAMP + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addTimerRecord(int hours, int minutes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_HOURS, hours);
        values.put(COLUMN_MINUTES, minutes);
        values.put(COLUMN_TIMESTAMP, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));

        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public List<TimerRecord> getAllTimerRecords() {
        List<TimerRecord> records = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()) {
            do {
                int hours = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HOURS));
                int minutes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MINUTES));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP));
                records.add(new TimerRecord(hours, minutes, timestamp));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return records;
    }
}