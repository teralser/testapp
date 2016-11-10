package testapp.acceptic.alext.testapp.activities;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private boolean mIsFromBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsFromBackground = false;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mIsFromBackground) {
            mIsFromBackground = false;
        }
    }

    @Override
    protected void onPause() {
        mIsFromBackground = isApplicationSentToBackground(this);
        Log.i(TAG, "App is in background: " + mIsFromBackground);
        if (mIsFromBackground) {
            appGoesToBackground();
        }

        super.onPause();
    }

    protected void appGoesToBackground() {

    }

    private boolean isApplicationSentToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
