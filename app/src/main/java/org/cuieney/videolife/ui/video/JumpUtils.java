package org.cuieney.videolife.ui.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import org.cuieney.videolife.R;
import org.cuieney.videolife.entity.MusicListBean;
import org.cuieney.videolife.ui.act.PlayMusciActivity;


/**
 * Created by cuieney on 2016/11/11.
 */

public class JumpUtils {

    public static void goToMusicPlayer(Activity activity, View view, MusicListBean dataBean) {
        Intent intent = new Intent(activity, PlayMusciActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PlayMusciActivity.DATA,dataBean);
        intent.putExtras(bundle);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, "IMG_TRANSITION");
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }
}
