package com.webserver;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.ChannelDatas;

public class HttpRequestRead implements HttpServerRequestCallback {

	@Override
	public void onRequest(AsyncHttpServerRequest req,
			AsyncHttpServerResponse resp) {
		resp.send(ChannelDatas.jsonArrayFromDiy());
	}

}
