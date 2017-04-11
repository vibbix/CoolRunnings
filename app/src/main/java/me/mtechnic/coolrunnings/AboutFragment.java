package me.mtechnic.coolrunnings;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The fragment for the about view
 */

public class AboutFragment extends Fragment {

    private final static String TAG = "AboutFragment";
    @BindView(R.id.sputnik)
    KenBurnsView sputnik;
    @BindView(R.id.versionno)
    TextView versionno;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedinstancestate) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        Picasso.with(view.getContext())
                .load(R.mipmap.sputnik)
                .placeholder(R.mipmap.sputnik_nano)
                .error(R.drawable.ic_error)
                .fit()
                .into(this.sputnik);
        try {
            versionno.setText(getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException nfe) {
            Log.d(TAG, "Failed to get package name");
        }
        return view;
    }

}