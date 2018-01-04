package org.cuieney.videolife.ui.fragment.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;
import org.cuieney.videolife.ui.fragment.video.VideoHomeFragment;

/**
 * Created by paohaile on 17/2/24.
 */

public class MusicFragment extends BaseMainFragment {

    public static MusicFragment newInstance(int type, String query){
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("query", query);
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setArguments(bundle);
        return musicFragment;
    }

    public static MusicFragment newInstance(){
        Bundle bundle = new Bundle();
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setArguments(bundle);
        return musicFragment;
    }

    public void addSearchFragment(int type, String query) {
        loadRootFragment(R.id.fl_first_container, MusicHomeFragment.newInstance(type, query));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_fragment, container, false);
        return view;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_first_container, MusicHomeFragment.newInstance(getArguments().getInt("type"),
                    getArguments().getString("query")));
        }
    }
}
