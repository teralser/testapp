package testapp.acceptic.alext.testapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import testapp.acceptic.alext.testapp.R;
import testapp.acceptic.alext.testapp.activities.MainActivity;
import testapp.acceptic.alext.testapp.events.NumberGeneratedEvent;
import testapp.acceptic.alext.testapp.other.Constants;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class ForegroundService extends Service {

    private static final String TAG = ForegroundService.class.getSimpleName();
    private int currentNumber = -1;
    private ScheduledExecutorService scheduler;
    private final IBinder mBinder = new MyBinder();
    private boolean isStarted = false;
    private boolean isInForeground = false;
    private NotificationCompat.Builder notification;

    public static Intent newIntentStart(Context context) {
        return new Intent(context, ForegroundService.class)
                .setAction(Constants.ACTION.START_ACTION);
    }

    public static Intent newIntentForegroundStart(Context context) {
        return new Intent(context, ForegroundService.class)
                .setAction(Constants.ACTION.START_FOREGROUND_ACTION);
    }

    public static Intent newIntentStop(Context context) {
        return new Intent(context, ForegroundService.class)
                .setAction(Constants.ACTION.STOP_ACTION);
    }

    public static Intent newIntentForegroundStop(Context context) {
        return new Intent(context, ForegroundService.class)
                .setAction(Constants.ACTION.STOP_FOREGROUND_ACTION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.START_ACTION)) {
            Log.i(TAG, "Received Start Intent ");
            initRandomizer();
        } else if (intent.getAction().equals(Constants.ACTION.START_FOREGROUND_ACTION)) {
            Log.i(TAG, "Received Start Foreground Intent ");
            initRandomizer();
            if (!isInForeground) {
                isInForeground = true;
                Bitmap icon = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher);

                notification = new NotificationCompat.Builder(this)
                        .setContentTitle(getString(R.string.random_numbers_generator))
                        .setTicker(getString(R.string.random_numbers_generator))
                        .setContentText(String.format(getString(R.string.random_number), currentNumber))
                        .setContentIntent(PendingIntent.getActivity(this, 0,
                                MainActivity.newNotificationIntent(this), 0))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(
                                Bitmap.createScaledBitmap(icon, 128, 128, false))
                        .setOngoing(true);

                startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification.build());
            }
        } else if (intent.getAction().equals(Constants.ACTION.STOP_FOREGROUND_ACTION)) {
            Log.i(TAG, "Received Stop Foreground Intent");
            stopForeground();
        } else if (intent.getAction().equals(Constants.ACTION.STOP_ACTION)) {
            Log.i(TAG, "Received Stop Intent");
            isStarted = false;
            scheduler.shutdown();
            stopForeground();
            stopSelf();
        }
        return START_STICKY;
    }

    private void initRandomizer() {
        if (!isStarted) {
            Log.i(TAG, "Initiating randomizer");
            isStarted = true;
            generateRandomNumber();

            Runnable task = new Runnable() {
                public void run() {
                    generateRandomNumber();
                }
            };
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(task, 0, 10, SECONDS);
        } else {
            Log.i(TAG, "Service already started");
        }
    }

    private void generateRandomNumber() {
        currentNumber = (int) (Math.random() * 10000);
        EventBus.getDefault().post(new NumberGeneratedEvent(currentNumber));

        updateNotification();
    }

    private void updateNotification() {
        if (notification != null) {
            // update notification text
            notification.setContentText(String.format(getString(R.string.random_number), currentNumber));
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification.build());
        }
    }

    private void dismissNotification() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE);
        notification = null;
    }

    private void stopForeground() {
        if (isInForeground) {
            Log.i(TAG, "stop foreground");
            isInForeground = false;
            stopForeground(true);
            dismissNotification();
        } else {
            Log.i(TAG, "service not in foreground");
        }
    }

    public class MyBinder extends Binder {
        public ForegroundService getService() {
            return ForegroundService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getCurrentNumber() {
        return currentNumber;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isInForeground() {
        return isInForeground;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        dismissNotification();
        scheduler = null;
    }
}
