package com.kimjongwoo.testfcmpush;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class MyFirebaseMsg extends FirebaseMessagingService {

    final String TAG = "test_fcm";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken : " + s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived");
            if (true) {
                scheduleJob();
            } else {
                handleNow();
            }
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, remoteMessage.getNotification().getTitle() + "");
        }

        sendNotification(remoteMessage);
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done");
    }

    private void scheduleJob() {
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = firebaseJobDispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        firebaseJobDispatcher.schedule(myJob);
    }


    private void sendNotification(RemoteMessage rMsg) {
        Log.d(TAG, "sendNotification");
        String title;
        String msg;
        if (rMsg.getData().isEmpty()) {
            title = rMsg.getNotification().getTitle();
            msg = rMsg.getNotification().getBody();
        } else {
            title = rMsg.getData().get("title");
            msg = rMsg.getData().get("message");
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channel = "채널";
        String channel_name = "채널명";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(msg)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(channel, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
            channel1.setDescription("채널 설명");
            channel1.enableLights(true);
            channel1.enableVibration(true);
            channel1.setShowBadge(false);
            channel1.setVibrationPattern(new long[]{100, 200, 100, 200});
            notificationManager.createNotificationChannel(channel1);
        }
        notificationManager.notify(9999, notificationBuilder.build());
    }


}
