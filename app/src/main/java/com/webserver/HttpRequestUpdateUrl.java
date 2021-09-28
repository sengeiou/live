package com.webserver;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.ChannelDatas;
import com.vv.live.util.StringUtil;

public class HttpRequestUpdateUrl implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            String title = req.getQuery().get("title").get(0);
            String url = req.getQuery().get("url").get(0);
            if (StringUtil.isEmpty(title) || StringUtil.isEmpty(url)) {
                return;
            }
            title = title.replace(",", "_");
            url = url.replace(",", "，，，");
            int count = ChannelDatas.addEndToDiy("100#本地投屏", title + "," + url);
            if (count < 1) {
                return;
            }
            ChannelDatas.playLastVideo("本地投屏");
            resp.send("");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
