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

public class VimeoDeserializer implements JsonDeserializer<VimeoListBean> {


    @Override
    public VimeoListBean deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        VimeoListBean dmVideosListBean = new VimeoListBean();
        dmVideosListBean.setNextPage(jsonObject.get("page").getAsInt());
        dmVideosListBean.setTotal(jsonObject.get("total").getAsInt());
        dmVideosListBean.setPerPage(jsonObject.get("per_page").getAsInt());
        JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject item = jsonArray.get(i).getAsJsonObject();
            VimeoItemListBean itemListBean = context.deserialize(item,
                    VimeoItemListBean.class);
            dmVideosListBean.getItemList().add(itemListBean);
        }
        return dmVideosListBean;
    }
}
