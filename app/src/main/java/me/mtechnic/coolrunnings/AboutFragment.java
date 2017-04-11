package me.mtechnic.coolrunnings;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The fragment for the about view
 */

public class AboutFragment extends Fragment {

    private final static String TAG = "AboutFragment";
    @BindView(R.id.sputnik)
    KenBurnsView sputnik;
    @BindView(R.id.versionno)
    TextView versionno;
    @BindString(R.string.github_vibbix)
    String githubVibbix;
    @BindString(R.string.github_dechristopher)
    String githubDechristopher;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedinstancestate) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        Picasso.with(view.getContext())
                .load(R.mipmap.sputnik)
                .placeholder(R.mipmap.sputnik_nano)
                .error(R.mipmap.sputnik_nano)
                .fit()
                .into(this.sputnik);
        try {
            versionno.setText(getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException nfe) {
            Log.d(TAG, "Failed to get package name");
        }
        return view;
    }

    @OnClick({R.id.btn_dechristopher, R.id.btn_vibbix})
    public void githubOnClick(View view) {
        Intent browserIntent = null;
        switch (view.getId()) {
            case R.id.btn_dechristopher:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubDechristopher));
                break;
            case R.id.btn_vibbix:
                browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubVibbix));
                break;
        }
        if (null != browserIntent) {
            startActivity(browserIntent);
        }
    }

}