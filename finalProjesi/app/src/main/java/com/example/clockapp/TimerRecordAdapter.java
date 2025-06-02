package com.example.clockapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Locale;

public class TimerRecordAdapter extends RecyclerView.Adapter<TimerRecordAdapter.TimerRecordViewHolder> {
    private List<TimerRecord> records;

    public TimerRecordAdapter(List<TimerRecord> records) {
        this.records = records;
    }

    @NonNull
    @Override
    public TimerRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timer_record_item, parent, false);
        return new TimerRecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimerRecordViewHolder holder, int position) {
        TimerRecord record = records.get(position);
        String time = String.format(Locale.getDefault(), "%02d:%02d", record.getHours(), record.getMinutes());
        holder.timeTextView.setText(time);
        holder.timestampTextView.setText(record.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public void updateRecords(List<TimerRecord> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }

    static class TimerRecordViewHolder extends RecyclerView.ViewHolder {
        TextView timeTextView, timestampTextView;

        TimerRecordViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.recordTimeTextView);
            timestampTextView = itemView.findViewById(R.id.recordTimestampTextView);
        }
    }
}