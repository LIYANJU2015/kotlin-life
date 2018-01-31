package org.cuieney.videolife.ui.fragment.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;
import org.cuieney.videolife.common.utils.LogUtil;

/**
 * Created by paohaile on 17/2/24.
 */

public class MusicFragment extends BaseMainFragment {

    public static MusicFragment newInstance(String query){
        Bundle bundle = new Bundle();
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

    public void addSearchFragment(String query) {
        MusicSearchFragment musicFragment = (MusicSearchFragment) getChildFragmentManager().findFragmentById(R.id.search_container);
        if (musicFragment == null) {
            replaceLoadRootFragment(R.id.search_container, MusicSearchFragment.newInstance(query), true);
        } else {
            musicFragment.setArguments(query);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.music_fragment, container, false);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_first_container, MusicHomeFragment.newInstance());
        }
    }
}
