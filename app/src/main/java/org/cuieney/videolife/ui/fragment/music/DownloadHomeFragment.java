package org.cuieney.videolife.ui.fragment.music;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseFragment;
import org.cuieney.videolife.provider.DownloadProvider;
import org.cuieney.videolife.ui.adapter.DownloadCursorAdapter;

import butterknife.BindView;

/**
 * Created by liyanju on 2018/1/23.
 */

public class DownloadHomeFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>{

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.loading_mb)
    ProgressBar loadingPB;
    @BindView(R.id.error_tv)
    TextView errorTv;

    private DownloadCursorAdapter cursorAdapter;

    @Override
    protected void initInject() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.download_home_fragment;
    }

    @Override
    protected void initEventAndData() {
        getLoaderManager().initLoader(1, null, this);
        recycler.setLayoutManager(new LinearLayoutManager(mContext));
        recycler.setHasFixedSize(true);
        cursorAdapter = new DownloadCursorAdapter(getActivity(), null, 0);
        recycler.setAdapter(cursorAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(mContext, DownloadProvider.DownloadedTableInfo.URI, null,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() > 0) {
            errorTv.setVisibility(View.INVISIBLE);
            loadingPB.setVisibility(View.INVISIBLE);
        } else {
            errorTv.setVisibility(View.VISIBLE);
            loadingPB.setVisibility(View.INVISIBLE);
        }
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
