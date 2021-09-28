package com.webserver;

import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.webserver.remote.RequestBatch;
import com.webserver.remote.RequestUpdate;
import com.webserver.remote.RequestVersion;
import com.webserver.sub.HttpRequestSub;
import com.webserver.sub.HttpRequestSubCheck;
import com.webserver.sub.HttpRequestSubDel;
import com.webserver.sub.HttpRequestSubSave;

public class WebServer {
    public final static int port = 12345;
    public WebServerCallback callback;

    public void play(String url) {
        callback.play(url);
    }

    public void setCallback(WebServerCallback callback) {
        this.callback = callback;
    }

    public void start() {
        AsyncHttpServer httpServer = new AsyncHttpServer();
        httpServer.get("/", new HttpRequest("index.html"));
		httpServer.get("/index.html", new HttpRequest("index.html"));
        httpServer.get("/bundle.js", new HttpRequest("bundle.js"));
        httpServer.get("/sub.html", new HttpRequest("sub.html"));
        httpServer.get("/sub.js", new HttpRequest("sub.js"));
        httpServer.get("/jquery.min.js", new HttpRequest("jquery.min.js"));
        httpServer.get("/bootstrap.min.js", new HttpRequest("bootstrap.min.js"));
        httpServer.get("/api/categories.json", new HttpRequestRead());
        httpServer.post("/api/category.json", new HttpRequestSave());
        httpServer.get("/api/delete.json", new HttpRequestDel());
        httpServer.get("/api/updateUrl", new HttpRequestUpdateUrl());
        httpServer.get("/api/sub/all", new HttpRequestSub());
        httpServer.get("/api/sub/save", new HttpRequestSubSave());
        httpServer.get("/api/sub/del", new HttpRequestSubDel());
        httpServer.get("/api/sub/check", new HttpRequestSubCheck());
        httpServer.get("/api/remote/version", new RequestVersion());
        httpServer.post("/api/remote/update", new RequestUpdate());
        httpServer.post("/api/remote/batch", new RequestBatch());
        httpServer.listen(port);
    }
}
