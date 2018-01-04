package org.cuieney.videolife.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.cuieney.videolife.common.utils.LogUtil;
import org.cuieney.videolife.entity.wyBean.TracksBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyanju on 2018/1/4.
 */

public class SoundCloudDeserializer implements JsonDeserializer<MusicListBean> {

    @Override
    public MusicListBean deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            MusicListBean musicListBean = new MusicListBean();
            if ("track" .equals(jsonObject.get("kind").getAsString())) {
                TracksBean tracksBean = context.deserialize(jsonObject,
                        TracksBean.class);
                musicListBean.setTitle(tracksBean.title);
                musicListBean.setArtwork_url(tracksBean.artwork_url);
                musicListBean.setDescription(tracksBean.description);
                musicListBean.getTracks().add(tracksBean);
            } else {
                JsonArray jsonArray = jsonObject.get("tracks").getAsJsonArray();
                ArrayList<TracksBean> tracksBeans = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                    tracksBeans.add(context.deserialize(jsonObject1,
                            TracksBean.class));
                }
                musicListBean.setTracks(tracksBeans);
                musicListBean.setTitle(jsonObject.get("title").getAsString());
                musicListBean.setDescription(jsonObject.get("title").getAsString());
                if (jsonObject.has("artwork_url")
                        && jsonObject.get("artwork_url") != null) {
                    try {
                        musicListBean.setArtwork_url(jsonObject.get("artwork_url").getAsString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return musicListBean;

    }
}
