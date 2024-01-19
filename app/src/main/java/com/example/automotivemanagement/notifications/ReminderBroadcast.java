package com.example.automotivemanagement.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.automotivemanagement.R;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String make = intent.getStringExtra("make");
        String model = intent.getStringExtra("model");
        String type = intent.getStringExtra("type");
        String duration = intent.getStringExtra("duration");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminderAlarm")
                .setSmallIcon(R.drawable.icons8_alarm_clock_60)
                .setContentTitle("Reminder")
                .setContentText("The " + type + " for your " + make + " " + model + " is expiring in " + duration)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
