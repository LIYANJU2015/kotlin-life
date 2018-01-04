package org.cuieney.videolife.entity;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by liyanju on 2017/11/19.
 */

public class DailymotionDeserializer implements JsonDeserializer<DMVideosListBean> {


    @Override
    public DMVideosListBean deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        DMVideosListBean dmVideosListBean = new DMVideosListBean();
        dmVideosListBean.setNextPage(jsonObject.get("page").getAsInt());
        dmVideosListBean.setHasMore(jsonObject.get("has_more").getAsBoolean());
        dmVideosListBean.list = new ArrayList<>();
        JsonArray jsonArray = jsonObject.get("list").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject item = jsonArray.get(i).getAsJsonObject();
            DMVideoItemListBean itemListBean = context.deserialize(item,
                    DMVideoItemListBean.class);
            dmVideosListBean.list.add(itemListBean);
        }
        return dmVideosListBean;
    }
}
