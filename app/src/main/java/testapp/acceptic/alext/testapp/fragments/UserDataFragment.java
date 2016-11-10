package testapp.acceptic.alext.testapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import testapp.acceptic.alext.testapp.R;
import testapp.acceptic.alext.testapp.events.LogOutEvent;

/**
 * Created by Alex Tereshchenko on 09.11.2016.
 */

public class UserDataFragment extends BaseFragment {

    @BindView(R.id.imgProfilePic)
    ImageView imgProfilePic;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    private Unbinder unbinder;

    public static UserDataFragment newInstance(GoogleSignInAccount account) {
        UserDataFragment userDataFragment = new UserDataFragment();
        Bundle args = new Bundle();
        args.putParcelable(GoogleSignInAccount.class.getSimpleName(), account);
        userDataFragment.setArguments(args);
        return userDataFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_data, container, false);
        unbinder = ButterKnife.bind(this, v);
        setUserData((GoogleSignInAccount) getArguments().getParcelable(GoogleSignInAccount.class.getSimpleName()));
        return v;
    }

    private void setUserData(GoogleSignInAccount acct) {
        if (acct != null) {
            String personName = acct.getDisplayName();
            String email = acct.getEmail();

            txtName.setText(personName);
            txtEmail.setText(email);

            Uri photoUri = acct.getPhotoUrl();
            if (photoUri != null) {
                String personPhotoUrl = photoUri.toString();
                Glide.with(getActivity().getApplicationContext()).load(personPhotoUrl)
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfilePic);
            }
        }
    }

    @OnClick(R.id.btn_sign_out)
    public void logOutFromG(){
        EventBus.getDefault().post(new LogOutEvent());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
