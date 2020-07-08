package com.idam.idam_tech.fcm;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.idam.idam_tech.R;
import com.idam.idam_tech.activities.splashscreen.SplashActivity;

public class FirebaseMessagingReceiver extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Log.d("remote", String.valueOf(remoteMessage.getData().size()>0));
        String id = remoteMessage.getData().get("id");
        if (remoteMessage.getData().size()>0){
            showNotif(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"), id);
        }

        if (remoteMessage.getNotification()!=null){
            showNotif(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), id);
        }

    }

    private RemoteViews getCustomDesign(String title, String message, String id){
        RemoteViews remoteViews=new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon, R.drawable.nksport2020);
        return remoteViews;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void showNotif(String title, String message, String id){
        Log.d("id notif:",id);
        Intent intent=new Intent(this, SplashActivity.class);
        intent.putExtra("firstOpen",id);
        String channel_id="web_app_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channel_id)
                .setSmallIcon(R.drawable.nksport2020)
                .setSound(uri)
                .setAutoCancel(true)
                .setLights(0xff0000ff, 300, 1000)
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setOnlyAlertOnce(false)
                .setGroupSummary(true)
                .setPriority(NotificationCompat.FLAG_NO_CLEAR)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            builder=builder.setContent(getCustomDesign(title,message,id));
        }else{
            builder = builder
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.nksport2020);
        }

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel=new NotificationChannel(channel_id, "Web_app", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setSound(uri, null);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0, builder.build());

    }

}
