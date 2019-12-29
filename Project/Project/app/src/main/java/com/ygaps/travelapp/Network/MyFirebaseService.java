package com.ygaps.travelapp.Network;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ygaps.travelapp.Activity.NotificationActivity;
import com.ygaps.travelapp.R;

import java.util.Map;
import java.util.Random;

public class MyFirebaseService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("du me", String.valueOf(remoteMessage.getData()));
        sendNotification(remoteMessage.getData());
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

    private Intent createIntent(String actionName,String notificationId, String mission) {
        Intent intent = new Intent(this, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(actionName);
        intent.putExtra("id", notificationId);


        return intent;
    }

    private void sendNotification(Map data) {
        try {
            String name = (String) data.get("name");
            String mission = (String) data.get("mission");
            int status = Integer.parseInt((String) data.get("status"));
            int notificationId = new Random().nextInt();

            String title = "Hi! " ;
            String message = "You have a new invitation to "+ name;

            Intent acceptIntent = createIntent(NotificationActivity.ACCEPT_ACTION, (String) data.get("id"), mission);
            Intent rejectIntent = createIntent(NotificationActivity.REJECT_ACTION,(String) data.get("id"), mission);
            Intent intent = createIntent(NotificationActivity.SHOW_ACTION, (String) data.get("id"), mission);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            String channelId = getString(R.string.project_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                            .setContentTitle(title)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationManager.IMPORTANCE_HIGH)
                            .addAction(new NotificationCompat.Action(
                                    android.R.drawable.sym_call_missed,
                                    "Reject",
                                    PendingIntent.getActivity(this, 0, rejectIntent, PendingIntent.FLAG_CANCEL_CURRENT)))
                            .addAction(new NotificationCompat.Action(
                                    android.R.drawable.sym_call_outgoing,
                                    "Accept",
                                    PendingIntent.getActivity(this, 0, acceptIntent, PendingIntent.FLAG_CANCEL_CURRENT)));

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);

                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(notificationId, notificationBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
