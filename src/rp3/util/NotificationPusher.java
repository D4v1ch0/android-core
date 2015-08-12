package rp3.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import rp3.app.NotificationActivity;
import rp3.app.StartActivity;
import rp3.core.R;

/**
 * Created by magno_000 on 16/06/2015.
 */
public class NotificationPusher {

    public final static String TAG_TITLE = "title";
    public final static String TAG_MESSAGE = "message";
    public final static String TAG_FOOTER = "footer";

    public static void pushNotification(int id, Context ctx, String message)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(ctx.getString(R.string.app_name))
                        .setContentText(message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, StartActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        //stackBuilder.addParentStack(ctx.getPackageManager().getLaunchIntentForPackage(ctx.getPackageName()).)
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(id, mBuilder.build());
    }

    public static void pushNotification(int id, Context ctx, String message, String title)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, NotificationActivity.class);
        resultIntent.putExtra(TAG_TITLE, title);
        resultIntent.putExtra(TAG_MESSAGE, message);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(id, mBuilder.build());
    }

    public static void pushNotification(int id, Context ctx, String message, String title, String footer)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, NotificationActivity.class);
        resultIntent.putExtra(TAG_TITLE, title);
        resultIntent.putExtra(TAG_MESSAGE, message);
        resultIntent.putExtra(TAG_FOOTER, footer);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        stackBuilder.addParentStack(NotificationActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(id, mBuilder.build());
    }

    public static void pushNotification(int id, Context ctx, String message, String title, Class activity)
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(ctx, activity);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);

        stackBuilder.addParentStack(activity);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mBuilder.setAutoCancel(true);
        mNotificationManager.notify(id, mBuilder.build());
    }
}
