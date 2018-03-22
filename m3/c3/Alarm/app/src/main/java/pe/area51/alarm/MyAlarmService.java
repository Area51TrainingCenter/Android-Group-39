package pe.area51.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

public class MyAlarmService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ALARM_ID = "alarm_channel";

    public final static String ARG_COMMAND = "alarmService.command";
    public final static String COMMAND_STOP_ALARM = "alarmService.stop_alarm";
    public final static String COMMAND_START_ALARM = "alarmService.start_alarm";

    private Ringtone ringtone;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initRingtone();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getStringExtra(ARG_COMMAND)) {
            case COMMAND_START_ALARM:
                showNotification();
                playRingtone();
                break;
            case COMMAND_STOP_ALARM:
                finishAlarmService();
                break;
        }
        return Service.START_NOT_STICKY;
    }

    private void showNotification() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    new NotificationChannel(
                            NOTIFICATION_CHANNEL_ALARM_ID,
                            getString(R.string.notification_channel_alarm),
                            NotificationManager.IMPORTANCE_HIGH
                    )
            );
        }
        notificationManager.notify(NOTIFICATION_ID, buildNotification());
    }

    private Notification buildNotification() {
        return new NotificationCompat
                .Builder(this, NOTIFICATION_CHANNEL_ALARM_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.alarm_notification_content))
                .setDeleteIntent(
                        PendingIntent.getService(
                                this,
                                0,
                                new Intent(this, MyAlarmService.class)
                                        .putExtra(ARG_COMMAND, COMMAND_STOP_ALARM),
                                PendingIntent.FLAG_CANCEL_CURRENT
                        )
                )
                .build();
    }

    private void finishAlarmService() {
        stopRingtone();
    }

    private void initRingtone() {
        ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
    }

    private void playRingtone() {
        if (ringtone != null) {
            ringtone.play();
        }
    }

    private void stopRingtone() {
        if (ringtone != null) {
            ringtone.stop();
        }
    }
}
