package com.internship.cristi.internshipapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.internship.cristi.internshipapp.R;
import com.internship.cristi.internshipapp.model.Event;

/**
 * Created by cristi on 1/15/18.
 */

public class SendNotification {

    Context context;


    public SendNotification(Context context) {
        this.context = context;
    }

    public void sendNotification(Event e){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String chanel_id = "3000";
            CharSequence name = "Channel Name";
            String description = "Chanel Description";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(chanel_id, name, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.BLUE);
            notificationManager.createNotificationChannel(mChannel);

            Notification notification = new Notification.Builder(context,chanel_id)
                    .setSmallIcon(R.mipmap.ic_notification)
                    .setContentTitle("New event added")
                    .setContentText(e.getName() +  " Date: " + e.getDatetime())
                    .build();

            notificationManager.notify(0, notification);
        }
    }
}
