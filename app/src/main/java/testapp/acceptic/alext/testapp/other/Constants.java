package testapp.acceptic.alext.testapp.other;

import testapp.acceptic.alext.testapp.BuildConfig;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class Constants {

    public interface ACTION {
        String MAIN_ACTION = BuildConfig.APPLICATION_ID + ".action.main";
        String START_ACTION = BuildConfig.APPLICATION_ID + ".action.start";
        String STOP_ACTION = BuildConfig.APPLICATION_ID + ".action.stop";
        String START_FOREGROUND_ACTION = BuildConfig.APPLICATION_ID + ".action.startforeground";
        String STOP_FOREGROUND_ACTION = BuildConfig.APPLICATION_ID  + ".action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
