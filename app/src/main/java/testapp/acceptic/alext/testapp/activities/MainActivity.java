package testapp.acceptic.alext.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import butterknife.ButterKnife;
import butterknife.OnClick;
import testapp.acceptic.alext.testapp.R;
import testapp.acceptic.alext.testapp.other.Constants;
import testapp.acceptic.alext.testapp.services.ForegroundService;

public class MainActivity extends GActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    public static Intent newNotificationIntent(Context context) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return notificationIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        startService(ForegroundService.newIntentStart(this));
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUser();
    }

    protected void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null)
                Log.d(TAG, acct.toString());
            startActivity(TabsActivity.newIntent(this, acct));
            finish();
        } else {
            // Signed out, show unauthenticated UI.
            Log.e(TAG, "User unauthenticated");
        }
    }

    @OnClick(R.id.btn_sign_in)
    public void signInClicked(View v) {
        signIn();
    }
}
