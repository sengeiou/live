package com.webserver;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.ChannelDatas;

public class HttpRequestDel implements HttpServerRequestCallback {

    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        int id = Integer.parseInt(req.getQuery().getString("id"));
        ChannelDatas.deleteFromDiy(id);
        resp.send(ChannelDatas.jsonArrayFromDiy());
    }

}
