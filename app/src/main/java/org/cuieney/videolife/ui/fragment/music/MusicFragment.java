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

    public static final int ALBUM_TYPE = 1;
    public static final int SONGS_TYPE = 2;
    public static final int ARTISTS_TYPE = 3;
    public static final int ALBUM_SEARCH_TYPE = 4;
    public static final int SONGS_SEARCH_TYPE = 5;
    public static final int ARTISTS_SEARCH_TYPE = 6;

    public static MusicFragment newInstance(int type, String query){
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("query", query);
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setArguments(bundle);
        return musicFragment;
    }

    public static MusicFragment newInstance(int type){
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        MusicFragment musicFragment = new MusicFragment();
        musicFragment.setArguments(bundle);
        return musicFragment;
    }

    public void addSearchFragment(int type, String query) {
        MusicHomeFragment musicFragment = (MusicHomeFragment) getChildFragmentManager().findFragmentById(R.id.search_container);
        LogUtil.d("addSearchFragment musicFragment " + musicFragment);
        if (musicFragment == null || musicFragment.type != type) {
            replaceLoadRootFragment(R.id.search_container, MusicHomeFragment.newInstance(type, query), true);
        } else {
            musicFragment.setArguments(type, query);
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
            loadRootFragment(R.id.fl_first_container, MusicHomeFragment.newInstance(getArguments().getInt("type"),
                    getArguments().getString("query")));
        }
    }
}
