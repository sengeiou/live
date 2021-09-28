package com.vv.live.data;

import java.util.List;

/**
 * 作者：By 15968
 * 日期：On 2021/4/23
 * 时间：At 21:54
 */

public class ChannelRule {

    private String title;

    private List<ChannelListItem> data;

    public List<ChannelListItem> getData() {
        return data;
    }

    public void setData(List<ChannelListItem> data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
