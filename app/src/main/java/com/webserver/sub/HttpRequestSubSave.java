package com.webserver.sub;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.vv.live.data.SubManager;
import com.vv.live.data.SubRule;
import com.vv.live.util.CollectionUtil;

import java.util.Date;

public class HttpRequestSubSave implements HttpServerRequestCallback {
    @Override
    public void onRequest(AsyncHttpServerRequest req,
                          AsyncHttpServerResponse resp) {
        try {
            String title = req.getQuery().get("title").get(0);
            String url = req.getQuery().get("url").get(0);
            boolean use = CollectionUtil.isEmpty(req.getQuery().get("use")) ? true : !Boolean.FALSE.toString().equals(req.getQuery().get("use").get(0));
            SubRule subRule = new SubRule();
            subRule.setTitle(title);
            subRule.setUrl(url);
            subRule.setModifyDate(new Date());
            subRule.setUse(use);
            if (!SubManager.exists(subRule)) {
                SubManager.addSubRecords(subRule);
                resp.send("ok");
            } else {
                SubManager.updateSubRecords(subRule, true, true);
                resp.send("ok");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
