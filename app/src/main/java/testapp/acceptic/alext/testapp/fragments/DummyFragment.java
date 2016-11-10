package testapp.acceptic.alext.testapp.fragments;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import testapp.acceptic.alext.testapp.R;
import testapp.acceptic.alext.testapp.events.StartStopServiceEvent;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class DummyFragment extends BaseFragment {

    private static final String COLOR = "COLOR";
    private Unbinder unbinder;

    public static DummyFragment newInstance(@ColorRes int backgroundColor) {
        DummyFragment dummyFragment = new DummyFragment();
        Bundle args = new Bundle();
        args.putInt(COLOR, backgroundColor);
        dummyFragment.setArguments(args);
        return dummyFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dummy, container, false);
        unbinder = ButterKnife.bind(this, v);
        v.setBackgroundColor(ContextCompat.getColor(inflater.getContext(), getArguments().getInt(COLOR)));
        return v;
    }

    @OnClick(R.id.btn_stop_service)
    public void switchService(View v) {
        EventBus.getDefault().post(new StartStopServiceEvent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
