package org.cuieney.videolife.ui.fragment.newstand;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseBackFragment;


/**
 * Created by cuieney on 2017/3/16.
 *
 */

public class NewstandHomeFragment extends BaseBackFragment{


    public static NewstandHomeFragment newInstance() {
        Bundle bundle = new Bundle();
        NewstandHomeFragment fragment = new NewstandHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newstand_home_fragment, container, false);
        return view;
    }

}
