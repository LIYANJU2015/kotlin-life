package org.cuieney.videolife.ui.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.image.ImageLoader;
import org.cuieney.videolife.common.utils.Utils;
import org.cuieney.videolife.entity.DownloadSong;
import org.cuieney.videolife.provider.DownloadDao;

import java.io.File;

/**
 * Created by liyanju on 2018/1/26.
 */

public class DownloadCursorAdapter extends RecyclerViewCursorAdapter<DownloadCursorAdapter.DownloadViewHolder>{

    public DownloadCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public void onBindViewHolder(DownloadCursorAdapter.DownloadViewHolder holder, Cursor cursor) {
        final DownloadSong downloadSong = DownloadSong.cursorToDownloadSong(cursor);

        ImageLoader.loadAll(mContext, downloadSong.image, holder.headImageView);
        holder.desTV.setText(downloadSong.artistName);
        holder.titleIV.setText(downloadSong.title);
        holder.deleteIV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle(R.string.delete_text)
                        .setMessage(R.string.delete_if_text).setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.sure_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Utils.runSingleThread(new Runnable() {
                            @Override
                            public void run() {
                                DownloadDao.removeDownloaded(mContext, downloadSong.id);
                                File file = new File(downloadSong.data);
                                if (file.exists()) {
                                    file.delete();
                                }
                            }
                        });
                    }
                }).show();

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Intent it = new Intent(Intent.ACTION_VIEW);
                    it.setDataAndType(Uri.parse("file://" + downloadSong.data), "audio/mp3");
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(it);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onContentChanged() {

    }

    @Override
    public DownloadCursorAdapter.DownloadViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DownloadCursorAdapter.DownloadViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.download_music_item, parent, false));
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class DownloadViewHolder extends RecyclerView.ViewHolder {

        public ImageView headImageView;

        public TextView titleIV;

        public TextView desTV;

        public ImageView deleteIV;

        public DownloadViewHolder(View itemView) {
            super(itemView);
            headImageView = (ImageView) itemView.findViewById(R.id.img);
            titleIV = (TextView) itemView.findViewById(R.id.title);
            desTV = (TextView) itemView.findViewById(R.id.description);
            deleteIV = (ImageView) itemView.findViewById(R.id.delete_iv);
        }
    }
}
