package com.webserver.sub;

import com.alibaba.fastjson.JSON;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.data.SubManager;

public class HttpRequestSub implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            resp.send(JSON.toJSONString(SubManager.getSubRecords()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
