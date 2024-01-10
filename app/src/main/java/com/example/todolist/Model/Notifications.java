package com.example.todolist.Model;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import java.util.Calendar;



/**
 *  Class to create and schedule push notifications
 * @author Jay Stewart
 */
public class Notifications {


    String channelId = "channel1";
    String channelName = "channelName1";

    /**
     Creates notification channel to post push notifications. Required by Android 8 and up
     @author Jay Stewart
     @param context the context of the super class calling this method
     */
    public void createNotificationChannel(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /**
     Creates a notification object
     @author Jay Stewart
     @param title title of the notification
     @param content the content / description of the notification
     @param image image to display with the notification
     @param context the context of the super class calling this method
     @return Notification
     */
    public Notification createNotification(String title, String content, int image, Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel1")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(image)
                .setAutoCancel(true);
        return builder.build();
    }

    /**
     Schedule a notification to be sent at a specific time
     @author Jay Stewart
     @param notification the notification object to schedule
     @param calendar the time to schedule the notification
     @param context the context of the super class calling this method
     */
    public void scheduleNotification(Notification notification, Calendar calendar, Context context) {
        Intent intent = new Intent(context, NotificationsPublisher.class);
        intent.putExtra(NotificationsPublisher.NOTIFICATION_ID, 1);
        intent.putExtra(NotificationsPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    /**
     Schedule a notification to be sent at a specific time and repeat at specific intervals
     @author Jay Stewart
     @param notification the notification object to schedule
     @param calendar the time to schedule the notification
     @param context the context of the super class calling this method
     */
    public void scheduleRepeatingNotification(Notification notification, Calendar calendar, Calendar repeatInterval, Context context) {
        Intent intent = new Intent(context, NotificationsPublisher.class);
        intent.putExtra(NotificationsPublisher.NOTIFICATION_ID, 1);
        intent.putExtra(NotificationsPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), repeatInterval.getTimeInMillis(), pendingIntent);
    }
}
