package com.webserver.remote;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.ChannelDatas;
import com.vv.live.parser.HttpParser;
import com.vv.live.util.StringUtil;

import org.json.JSONObject;

public class RequestUpdate implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            JSONObject parms = (JSONObject) req.getBody().get();
            try {
                String title = parms.getString("title");
                String url = parms.getString("url");
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
                int count = ChannelDatas.addEndToDiy("100#本地投屏", title + "," + url);
                if (count < 1) {
                    return;
                }
                ChannelDatas.playLastVideo("本地投屏");
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
