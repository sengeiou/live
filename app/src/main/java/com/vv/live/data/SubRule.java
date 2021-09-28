package com.vv.live.data;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * 作者：By 15968
 * 日期：On 2021/5/3
 * 时间：At 22:58
 */

public class SubRule {
    private String title;
    private String url;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;

    /**
     * 失败次数
     */
    private int errorCount;

    /**
     * 所含规则数
     */
    private int rulesCount;

    /**
     * 上次操作是否成功
     */
    private boolean lastUpdateSuccess;

    /**
     * 是否启用
     */
    private boolean use;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getRulesCount() {
        return rulesCount;
    }

    public void setRulesCount(int rulesCount) {
        this.rulesCount = rulesCount;
    }

    public boolean isLastUpdateSuccess() {
        return lastUpdateSuccess;
    }

    public void setLastUpdateSuccess(boolean lastUpdateSuccess) {
        this.lastUpdateSuccess = lastUpdateSuccess;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }
}
