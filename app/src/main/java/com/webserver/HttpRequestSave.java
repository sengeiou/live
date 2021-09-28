package com.webserver;

import org.json.JSONObject;

import android.util.Base64;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.ChannelDatas;

public class HttpRequestSave implements HttpServerRequestCallback {
	@Override
	public void onRequest(AsyncHttpServerRequest req,
			AsyncHttpServerResponse resp) {
		try {
			 JSONObject parms = (JSONObject) req.getBody().get();
			 String name=new String(Base64.decode(parms.optString("name"), 0));
			 String data=new String(Base64.decode(parms.optString("data"), 0));
			 ChannelDatas.savetoDiy(name, data);
			 resp.send(ChannelDatas.jsonArrayFromDiy());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
