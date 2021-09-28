package com.vv.live.data;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.vv.live.ChannelDatas;
import com.vv.live.util.CollectionUtil;
import com.vv.live.util.FileUtil;
import com.vv.live.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 作者：By 15968
 * 日期：On 2021/5/3
 * 时间：At 22:56
 */

public class SubManager {

    private static String filename = "sub_rules.txt";

    public static List<SubRule> getSubRecords() {
        File file = new File(ChannelDatas.getRootDir() + File.separator + filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        String text = FileUtil.fileToString(file.getAbsolutePath());
        if (StringUtil.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return JSON.parseArray(text, SubRule.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void checkSub(Context context) {
        try {
            List<SubRule> ruleList = getSubRecords();
            if (CollectionUtil.isEmpty(ruleList)) {
                return;
            }
            for (SubRule subRule : ruleList) {
                checkSub(subRule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkSub(final SubRule subscribeRecord) {
        checkSub(subscribeRecord, false);
    }

    private static void checkSub(final SubRule subscribeRecord, boolean ignoreDate) {
        if (subscribeRecord == null) {
            return;
        }
        if (!subscribeRecord.isUse()) {
            return;
        }
        if (subscribeRecord.getErrorCount() > 10) {
            //连续失败大于10次，永远不再检查更新
            return;
        }
        if (!ignoreDate && subscribeRecord.getModifyDate() != null &&
                System.currentTimeMillis() - subscribeRecord.getModifyDate().getTime() < 3600 * 24 * 1000) {
            //一天内只检查更新一次
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    try (Response response = OkGo.<String>get(subscribeRecord.getUrl()).execute();
                         ResponseBody body = response.body()) {
                        if (response.code() == 200 && body != null) {
                            String s = body.string();
                            if (!StringUtil.isEmpty(s)) {
                                ChannelDatas.savetoDiy(subscribeRecord.getTitle(), s);
                                subscribeRecord.setRulesCount(s.startsWith("#EXTM3U") ? s.split("\n").length / 2 : s.split("\n").length);
                                subUpdateSuccess(subscribeRecord);
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    subUpdateError(subscribeRecord);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, "thread-sub-" + subscribeRecord.getTitle()).start();
    }

    private static void subUpdateError(SubRule subscribeRecord) {
        subscribeRecord.setModifyDate(new Date());
        subscribeRecord.setErrorCount(subscribeRecord.getErrorCount() + 1);
        subscribeRecord.setLastUpdateSuccess(false);
        updateSubRecords(subscribeRecord);
    }

    private static void subUpdateSuccess(SubRule subscribeRecord) {
        subscribeRecord.setModifyDate(new Date());
        subscribeRecord.setErrorCount(0);
        subscribeRecord.setLastUpdateSuccess(true);
        updateSubRecords(subscribeRecord);
    }

    public static void addSubRecords(SubRule subscribeRecord) {
        try {
            List<SubRule> records = getSubRecords();
            if (CollectionUtil.isEmpty(records)) {
                records = new ArrayList<>();
                records.add(subscribeRecord);
                FileUtil.stringToFile(JSON.toJSONString(records), ChannelDatas.getRootDir() + File.separator + filename);
                checkSub(subscribeRecord, true);
                return;
            }
            for (int i = 0; i < records.size(); i++) {
                SubRule record = records.get(i);
                if (subscribeRecord.getTitle().equals(record.getTitle())) {
                    records.remove(i);
                    subscribeRecord.setRulesCount(record.getRulesCount());
                    subscribeRecord.setLastUpdateSuccess(record.isLastUpdateSuccess());
                    subscribeRecord.setErrorCount(record.getErrorCount());
                    records.add(i, subscribeRecord);
                    FileUtil.stringToFile(JSON.toJSONString(records), ChannelDatas.getRootDir() + File.separator + filename);
                    checkSub(subscribeRecord, true);
                    return;
                }
            }
            records.add(subscribeRecord);
            FileUtil.stringToFile(JSON.toJSONString(records), ChannelDatas.getRootDir() + File.separator + filename);
            checkSub(subscribeRecord, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delSubRecords(String subscribeRecord) {
        SubRule subRule = new SubRule();
        subRule.setTitle(subscribeRecord);
        delSubRecords(subRule);
    }

    public static void delSubRecords(SubRule subscribeRecord) {
        try {
            List<SubRule> records = getSubRecords();
            if (CollectionUtil.isEmpty(records)) {
                return;
            }
            for (int i = 0; i < records.size(); i++) {
                SubRule record = records.get(i);
                if (subscribeRecord.getTitle().equals(record.getTitle())) {
                    records.remove(i);
                    FileUtil.stringToFile(JSON.toJSONString(records), ChannelDatas.getRootDir() + File.separator + filename);
                    ChannelDatas.deleteDiy(subscribeRecord.getTitle());
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateSubRecords(SubRule subscribeRecord) {
        updateSubRecords(subscribeRecord, false, false);
    }

    public static void updateSubRecords(SubRule subscribeRecord, boolean check, boolean onlyUrl) {
        try {
            List<SubRule> records = getSubRecords();
            for (int i = 0; i < records.size(); i++) {
                SubRule record = records.get(i);
                if (subscribeRecord.getTitle().equals(record.getTitle())) {
                    records.remove(i);
                    if (onlyUrl) {
                        subscribeRecord.setRulesCount(record.getRulesCount());
                        subscribeRecord.setLastUpdateSuccess(record.isLastUpdateSuccess());
                        subscribeRecord.setErrorCount(record.getErrorCount());
                    }
                    records.add(i, subscribeRecord);
                    FileUtil.stringToFile(JSON.toJSONString(records), ChannelDatas.getRootDir() + File.separator + filename);
                    if (check) {
                        checkSub(subscribeRecord, true);
                    }
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean exists(String subscribeRecord) {
        SubRule rule = new SubRule();
        rule.setTitle(subscribeRecord);
        return exists(rule);
    }

    public static boolean exists(SubRule subscribeRecord) {
        List<SubRule> records = getSubRecords();
        if (CollectionUtil.isEmpty(records)) {
            return false;
        }
        for (int i = 0; i < records.size(); i++) {
            SubRule record = records.get(i);
            if (subscribeRecord.getTitle().equals(record.getTitle())) {
                return true;
            }
        }
        return false;
    }
}
