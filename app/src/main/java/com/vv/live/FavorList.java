package com.vv.live;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavorList extends ArrayList<FavorObject> {

    private static String filename = "favor.db";

    public FavorList(Context context) {
        try {
            String json = ChannelManager.readfile(context, filename);
            JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				int type = jsonObject.optInt("type");
				int pos = jsonObject.optInt("pos");
				FavorObject favorObject = new FavorObject(type, pos);
				add(favorObject);
			}
		} catch (Exception e) {
        }
    }

    public boolean isChannelFavored(int type, int pos) {
        for (int i = 0; i < size(); i++) {
            if (get(i).type == type && get(i).pos == pos) {
                return true;
            }
        }
        return false;
    }

    private String toJson() {
        String json = "[";
        for (int k = 0; k < size(); k++) {
            if (k == 0) {
                json += get(k).toJson();
            } else {
                json += "," + get(k).toJson();
            }
        }
        json += "]";
        return json;
    }

    public void saveToFile(Context context) {
        ChannelManager.writefile(context, filename, toJson());
    }

    public void delete(int type, int pos) {
        for (int i = 0; i < size(); i++) {
            if (get(i).type == type && get(i).pos == pos) {
                remove(i);
                break;
            }
        }
    }

    public static void clearFavor(Context context) {
        ChannelManager.writefile(context, filename, "[]");
    }

    public void delete(int pos) {
        remove(pos);
    }


}
