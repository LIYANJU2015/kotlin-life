package org.cuieney.videolife.ui.fragment.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseMainFragment;

/**
 * Created by liyanju on 2018/1/23.
 */

public class DownloadFragment extends BaseMainFragment {

    public static DownloadFragment newInstance() {
        Bundle bundle = new Bundle();
        DownloadFragment downloadFragment = new DownloadFragment();
        downloadFragment.setArguments(bundle);
        return downloadFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.download_fragment, container, false);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_first_container, new DownloadHomeFragment());
        }
    }
}
