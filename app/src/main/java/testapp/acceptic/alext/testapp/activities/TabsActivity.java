package testapp.acceptic.alext.testapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import testapp.acceptic.alext.testapp.R;
import testapp.acceptic.alext.testapp.adapters.DummyPagerAdapter;
import testapp.acceptic.alext.testapp.events.LogOutEvent;
import testapp.acceptic.alext.testapp.events.StartStopServiceEvent;
import testapp.acceptic.alext.testapp.fragments.BaseFragment;
import testapp.acceptic.alext.testapp.fragments.DummyFragment;
import testapp.acceptic.alext.testapp.fragments.UserDataFragment;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class TabsActivity extends GActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.pager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    public static Intent newIntent(Context context, GoogleSignInAccount account) {
        Intent intent = new Intent(context, TabsActivity.class);
        intent.putExtra(GoogleSignInAccount.class.getSimpleName(), account);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        GoogleSignInAccount account = getIntent().
                getParcelableExtra(GoogleSignInAccount.class.getSimpleName());
        if (account == null) {
            checkUser();
        } else {
            setData(account);
        }
    }

    @Override
    protected void handleSignInResult(GoogleSignInResult result) {
        super.handleSignInResult(result);
        setData(result.getSignInAccount());
    }

    private void setData(GoogleSignInAccount account) {
        ArrayList<BaseFragment> fragments = new ArrayList<>(2);
        fragments.add(account != null ?
                UserDataFragment.newInstance(account) :
                DummyFragment.newInstance(android.R.color.holo_green_light));
        fragments.add(DummyFragment.newInstance(android.R.color.holo_blue_light));

        String[] titles = new String[]{getString(R.string.user_data), getString(R.string.service_control)};

        DummyPagerAdapter adapter = new DummyPagerAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Subscribe
    public void onLogOutEvent(LogOutEvent event) {
        signOut(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (foregroundService.isStarted()) {
                    // stop service
                    onServerStartStop(new StartStopServiceEvent());
                }
                startActivity(MainActivity.newIntent(TabsActivity.this));
                finish();
            }
        });
    }
}
