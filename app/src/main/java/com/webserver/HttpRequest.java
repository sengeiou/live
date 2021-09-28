package com.webserver;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

public class HttpRequest implements HttpServerRequestCallback {

	private String filename;
	public HttpRequest(String filename){
		this.filename=filename;
	}
	@Override
	public void onRequest(AsyncHttpServerRequest req,AsyncHttpServerResponse resp) {
		resp.send(FileUtils.readassetfile(filename));
	}
	
}
