package com.example.todolist.Model;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

/**
 Broadcast Receiver to schedule notifications
 @author Jay Stewart
 */
public class NotificationsPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";


    /**
     Creates a notification object
     @author Jay Stewart
     @param context the context of the super class calling this method
     @param intent used to obtain the notification object received
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        assert notification != null;
        notificationManager.notify(id, notification);
    }
}
