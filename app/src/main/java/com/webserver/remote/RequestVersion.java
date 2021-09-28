package com.webserver.remote;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

public class RequestVersion implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            resp.send("2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
