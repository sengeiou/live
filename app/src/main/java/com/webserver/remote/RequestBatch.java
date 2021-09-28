package com.webserver.remote;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.ChannelDatas;
import com.vv.live.parser.HttpParser;
import com.vv.live.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class RequestBatch implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            JSONArray params = ((JSONObject) req.getBody().get()).getJSONArray("playSources");
            try {
                if (params.length() < 1) {
                    resp.send("");
                    return;
                }
                String firstTitle = null;
                for (int i = 0; i < params.length(); i++) {
                    JSONObject jsonObject = params.getJSONObject(i);
                    String title = jsonObject.getString("title");
                    String url = jsonObject.getString("url");
                    if (StringUtil.isEmpty(title) || StringUtil.isEmpty(url)) {
                        return;
                    }
                    String[] urls = url.split(";");
                    if (urls.length > 1) {
                        //只需要header部分
                        url = urls[0] + ";" + urls[urls.length - 1];
                    }
                    title = HttpParser.decodeUrl(title, "UTF-8").replace(",", "_");
                    url = HttpParser.decodeUrl(url, "UTF-8").replace(",", "，，，");
                    ChannelDatas.addEndToDiy("100#本地投屏", title + "," + url, i == params.length() - 1);
                    if (i == 0) {
                        firstTitle = title;
                    }
                }
                ChannelDatas.playThisVideo("本地投屏", firstTitle);
                resp.send("");
            } catch (Exception e) {
                e.printStackTrace();
                resp.send("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.send("");
        }

    }

}
