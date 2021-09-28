package com.webserver.sub;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.data.SubManager;

public class HttpRequestSubCheck implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            String title = req.getQuery().get("title").get(0);
            boolean exist = SubManager.exists(title);
            resp.send(exist + "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
