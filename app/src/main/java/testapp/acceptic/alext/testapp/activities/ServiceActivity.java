package testapp.acceptic.alext.testapp.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import testapp.acceptic.alext.testapp.R;
import testapp.acceptic.alext.testapp.events.NumberGeneratedEvent;
import testapp.acceptic.alext.testapp.events.StartStopServiceEvent;
import testapp.acceptic.alext.testapp.services.ForegroundService;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class ServiceActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.randomNumber)
    TextView randomNumber;

    protected ForegroundService foregroundService;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(ForegroundService.newIntentForegroundStart(this), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            ForegroundService.MyBinder b = (ForegroundService.MyBinder) binder;
            foregroundService = b.getService();
            setNumber(foregroundService.getCurrentNumber());
            startService(ForegroundService.newIntentForegroundStop(ServiceActivity.this));
        }

        public void onServiceDisconnected(ComponentName className) {
            foregroundService = null;
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
    }

    @Override
    protected void appGoesToBackground() {
        super.appGoesToBackground();
        if (foregroundService != null && foregroundService.isStarted())
            startService(ForegroundService.newIntentForegroundStart(this));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNumberGenerated(NumberGeneratedEvent event) {
        setNumber(event.getNumber());
    }

    @Subscribe
    public void onServerStartStop(StartStopServiceEvent event) {
        boolean isServiceRunning = foregroundService != null && foregroundService.isStarted();
        startService(isServiceRunning ?
                ForegroundService.newIntentStop(this) :
                ForegroundService.newIntentStart(this));
        if (randomNumber != null && isServiceRunning)
            setNumber(-1);
    }

    private void setNumber(int number) {
        if (randomNumber != null)
            randomNumber.setText(number == -1 ?
                    getString(R.string.service_stopped) :
                    String.valueOf(number));
    }
}
