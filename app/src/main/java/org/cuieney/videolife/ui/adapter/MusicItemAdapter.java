package org.cuieney.videolife.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.cuieney.videolife.R;
import org.cuieney.videolife.common.base.BaseRecycerViewAdapter;
import org.cuieney.videolife.entity.wyBean.TracksBean;
import org.cuieney.videolife.ui.widget.DownloadBottomSheetDialog;

import java.util.List;

/**
 * Created by cuieney on 17/3/4.
 */

public class MusicItemAdapter extends BaseRecycerViewAdapter<TracksBean,RecyclerView.ViewHolder> {

    private FragmentManager manager;

    public MusicItemAdapter(Context context, List<TracksBean> list, FragmentManager manager) {
        super(context, list);
        this.manager = manager;
    }

    @Override
    public RecyclerView.ViewHolder getCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicItemAdapter.MyViewHolder(inflater.inflate(R.layout.music_detail_item,parent,false));
    }

    @Override
    public void getBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        final TracksBean tracksBean = list.get(position);
        viewHolder.num.setText((position+1)+"");
        viewHolder.title.setText(tracksBean.getSongname());
        viewHolder.dis.setText(tracksBean.getSonger());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DownloadBottomSheetDialog
                        .newInstance(tracksBean)
                        .showBottomSheetFragment(manager);
            }
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView num;
        private final TextView title;
        private final TextView dis;
        private final ImageView playIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            num = ((TextView) itemView.findViewById(R.id.title_num));
            title = ((TextView) itemView.findViewById(R.id.title));
            dis = ((TextView) itemView.findViewById(R.id.title_dis));
            playIcon = ((ImageView) itemView.findViewById(R.id.songdetail_play));

        }
    }
}
