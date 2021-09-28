package com.vv.live;

import android.content.Context;
import android.os.Environment;

import com.alibaba.fastjson.JSON;
import com.setting.MySettings;
import com.vv.live.data.ChannelListItem;
import com.vv.live.data.ChannelRule;
import com.vv.live.ui.PlayerActivity;
import com.vv.live.util.CollectionUtil;
import com.vv.live.util.FileUtil;
import com.vv.live.util.FilesInAppUtils;
import com.vv.live.util.PreferenceMgr;
import com.vv.live.util.StringUtil;
import com.vv.live.util.ToastMgr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.MEDIA_MOUNTED_READ_ONLY;

public class ChannelDatas extends ArrayList<ChannelData> {
    private static final long serialVersionUID = 1L;
    public static ArrayList<String> categoryList;
    private MySettings mySettings;
    public static final String FILE_DATA = "data" + PlayerActivity.myappver;

    private static String tryGetRootDir() {
        try {
            if (MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState())) {
                File doc = PlayerActivity.context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                if (doc == null) {
                    return getRootDirInFiles();
                }
                if (!doc.exists()) {
                    boolean ok = doc.mkdirs();
                    if (!ok) {
                        return getRootDirInFiles();
                    }
                }
                File logs = new File(doc.getAbsoluteFile() + File.separator + "崩溃日志");
                if (!logs.exists() && !logs.mkdirs()) {
                    return getRootDirInFiles();
                }
                return doc.getAbsolutePath();
            } else {
                return getRootDirInFiles();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return getRootDirInFiles();
        }
    }

    private static String getRootDirInFiles() {
        try {
            String fileDir = PlayerActivity.context.getFilesDir().getAbsolutePath();
            File logs = new File(fileDir + File.separator + "崩溃日志");
            if (!logs.exists() && !logs.mkdirs()) {
                return getRootDirInPrivateFiles();
            } else {
                return fileDir;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getRootDirInPrivateFiles();
        }
    }

    private static String getRootDirInPrivateFiles() {
        try {
            String fileDir = PlayerActivity.context.getCacheDir().getAbsolutePath();
            File logs = new File(fileDir + File.separator + "崩溃日志");
            if (!logs.exists() && !logs.mkdirs()) {
                ToastMgr.shortBottomCenter(PlayerActivity.context, "找不到可读写的存储目录，请联系管理员");
                return "/haiker";
            } else {
                return fileDir;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastMgr.shortBottomCenter(PlayerActivity.context, "找不到可读写的存储目录，请联系管理员");
            return "/haiker";
        }
    }


    public static String getRootDir() {
        String rootDir = PreferenceMgr.getString(PlayerActivity.context, "rootDir", null);
        if (!StringUtil.isNotEmpty(rootDir)) {
            rootDir = tryGetRootDir();
            PreferenceMgr.put(PlayerActivity.context, "rootDir", rootDir);
        }
        return rootDir;
    }

    public static String DIR_DIY = "/diy";

    public static void initDir(){
        DIR_DIY = getRootDir() + "/diy";
    }

    public ChannelDatas(Context context) {
        mySettings = new MySettings(context);
    }

    public void loadData(Context context) {
        categoryList = new ArrayList<>();
        String channel = FilesInAppUtils.getAssetsString(context, "channel.json");
        List<ChannelRule> rules = JSON.parseArray(channel, ChannelRule.class);
        if (CollectionUtil.isNotEmpty(rules)) {
            for (ChannelRule rule : rules) {
                if (CollectionUtil.isEmpty(rule.getData())) {
                    continue;
                }
                ChannelData channelData = new ChannelData();
                channelData.name = rule.getTitle();
                categoryList.add(channelData.name);
                int num = getLastChannel() + 1;
                for (ChannelListItem datum : rule.getData()) {
                    if (datum.getUrl() == null || datum.getUrl().isEmpty()) {
                        continue;
                    }
                    ChannelObject channelObject = new ChannelObject();
                    channelObject.num = num;
                    channelObject.name = datum.getName();
                    channelObject.source = new ArrayList<>();
                    channelObject.source.add(datum.getUrl());
                    channelData.data.add(channelObject);
                    num++;
                }
                add(channelData);
            }
        }
        loadDiy();
    }

//    public void loadData() {
//        try {
//            categoryList = new ArrayList<String>();
//            JSONArray dataArray = jsonString == null || jsonString.length() <= 0 ? new JSONArray() : new JSONArray(jsonString);
//            for (int i = 0; i < dataArray.length(); i++) {
//                ChannelData channelData = new ChannelData();
//                JSONObject jsonCategorys = dataArray.getJSONObject(i);
//                channelData.name = jsonCategorys.optString("name");
//                channelData.psw = jsonCategorys.optString("psw", "");
//
//                if (!channelData.name.equals("我的收藏")) categoryList.add(channelData.name);
//                if (mySettings.getSettingboolean("Hide" + channelData.name)) {
//                    continue;
//                }
//
//                JSONArray channelArray = jsonCategorys.optJSONArray("data");
//                if (channelArray != null) {
//                    for (int j = 0; j < channelArray.length(); j++) {
//                        ChannelObject channelObject = new ChannelObject();
//                        JSONObject jsonChannel = channelArray.getJSONObject(j);
//                        channelObject.num = jsonChannel.optInt("num");
//                        channelObject.name = jsonChannel.optString("name");
//                        JSONArray srcJsonArray = jsonChannel.optJSONArray("source");
//                        if (srcJsonArray != null) {
//                            for (int k = 0; k < srcJsonArray.length(); k++) {
//                                channelObject.source.add(srcJsonArray.optString(k));
//                            }
//                        }
//                        channelData.data.add(channelObject);
//                    }
//                }
//                add(channelData);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        loadDiy();
//    }

    public void loadFavor(FavorList favorList) {
        try {
            boolean shouldAdd = true;
            if (size() > 0) {
                for (ChannelData ch : this) {
                    if (ch.name.equals("我的收藏")) {
                        ch.data.clear();
                        shouldAdd = false;
                        break;
                    }
                }
            }
            if (shouldAdd) {
                ChannelData channelData = new ChannelData();
                channelData.name = "我的收藏";
                channelData.data = new ArrayList<>();
                add(0, channelData);
            }
            if (favorList == null || favorList.isEmpty()) {
                return;
            }
            for (int i = 0; i < favorList.size(); i++) {
                int type = favorList.get(i).type;
                int pos = favorList.get(i).pos;
                if (type > 0 && type < size() && pos >= 0 && pos < get(type).data.size()) {
                    get(0).data.add(get(type).data.get(pos));
                }
            }
        } catch (Exception e) {
        }
    }

    public void loadDiy() {
        try {
            int newVer = 5;
            if (mySettings.getSettingInt("initVer") < newVer) {
                File file = new File(DIR_DIY);
                if (file.exists()) {
                    File[] listFiles = file.listFiles();
                    if (listFiles != null && listFiles.length > 0) {
                        for (File listFile : listFiles) {
                            listFile.delete();
                        }
                    }
                }
                FilesInAppUtils.copyAssets(PlayerActivity.context, "diy", DIR_DIY);
                mySettings.saveSetting("initVer", newVer);
            }

            File file = new File(DIR_DIY);
            if (!file.exists()) {
                file.mkdirs();
            }
            File[] listFiles = file.listFiles();
            if (null == listFiles) return;

            List<File> list = new ArrayList<File>();
            for (File file1 : listFiles) {
                if (file1.isFile() && file1.getName().endsWith(".txt")) {
                    list.add(file1);
                }
            }
            Collections.sort(list, new Comparator<File>() {
                @Override
                public int compare(File f1, File f2) {
                    if (!f1.getName().contains("#")) {
                        return 1;
                    } else {
                        if (!f2.getName().contains("#")) {
                            return -1;
                        } else {
                            try {
                                int id1 = Integer.parseInt(f1.getName().split("#")[0]);
                                int id2 = Integer.parseInt(f2.getName().split("#")[0]);
                                if (id1 > id2) {
                                    return 1;
                                } else {
                                    return -1;
                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return 0;
                            }
                        }
                    }
                }
            });

            for (File file2 : list) {
                try {
                    String name = file2.getName();
                    ChannelData channelData = new ChannelData();
                    if (name.endsWith(".txt")) {
                        if (name.contains("#")) {
                            name = name.split("#")[1];
                        }
                        if (name.contains("_") && name.indexOf("_") > 0 && name.indexOf("_") < name.length() - 1) {
                            channelData.name = name.split("_")[0];
                            channelData.psw = name.substring(0, name.lastIndexOf(".")).split("_")[1];
                        } else {
                            channelData.name = name.substring(0, name.lastIndexOf("."));
                            channelData.psw = "";
                        }

                        try {
                            FileReader fr = new FileReader(file2);
                            BufferedReader bf = new BufferedReader(fr);
                            String str;

                            ArrayList<String> nameArrayList = new ArrayList<String>();
                            ArrayList<ArrayList<String>> sourceArrayList = new ArrayList<ArrayList<String>>();
                            boolean isM3u = false;
                            String lastName = null;
                            while ((str = bf.readLine()) != null) {
                                try {
                                    if (str.isEmpty()) {
                                        continue;
                                    }
                                    str = str.trim();
                                    if (str.startsWith("#EXTM3U")) {
                                        //m3u格式
                                        isM3u = true;
                                    } else if (str.startsWith("#EXTINF")) {
                                        //m3u格式
                                        isM3u = true;
                                        if (str.contains(",")) {
                                            lastName = str.split(",")[1];
                                        }
                                    } else if (isM3u && lastName != null && str.contains("://")) {
                                        //m3u格式
                                        if (!nameArrayList.contains(lastName)) {
                                            nameArrayList.add(lastName);
                                            sourceArrayList.add(new ArrayList<>(Collections.singletonList(str)));
                                        } else {
                                            int pos = nameArrayList.indexOf(lastName);
                                            sourceArrayList.get(pos).add(str);
                                        }
                                    } else if (!isM3u && str.contains(",")) {
                                        //通用txt格式
                                        String channelname = str.substring(0, str.indexOf(","));
                                        String srcString = str.substring(str.indexOf(",") + 1);
                                        //读入数据到数组中
                                        ArrayList<String> srcArrayList = new ArrayList<>();
                                        if (srcString.contains("#")) {
                                            srcArrayList.addAll(Arrays.asList(srcString.split("#")));
                                        } else {
                                            srcArrayList.add(srcString);
                                        }
                                        if (!nameArrayList.contains(channelname)) {
                                            nameArrayList.add(channelname);
                                            sourceArrayList.add(srcArrayList);
                                        } else {
                                            int pos = nameArrayList.indexOf(channelname);
                                            sourceArrayList.get(pos).addAll(srcArrayList);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            int num = getLastChannel() + 1;
                            for (int i = 0; i < nameArrayList.size(); i++) {
                                ChannelObject channelObject = new ChannelObject();
                                channelObject.num = num;
                                channelObject.name = nameArrayList.get(i);
                                channelObject.source = sourceArrayList.get(i);
                                channelData.data.add(channelObject);
                                num++;
                            }
                            bf.close();
                            fr.close();
                            add(channelData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
    }

    public static int addEndToDiy(String name, String data) {
        return addEndToDiy(name, data, true);
    }

    public static int addEndToDiy(String name, String data, boolean refresh) {
        String[] dataRow = data.split(",");
        if (dataRow.length != 2) {
            return 0;
        }
        File file = new File(DIR_DIY + "/" + name + ".txt");
        if (!file.exists()) {
            savetoDiy(name, data);
            return 1;
        }
        String existText = FileUtil.fileToString(DIR_DIY + "/" + name + ".txt");
        String[] channels = existText.split("\n");
        StringBuilder text = new StringBuilder();
        int count = 0;
        int start = channels.length < 100 ? 0 : channels.length - 99;
        for (int i = start; i < channels.length; i++) {
            String row = channels[i];
            if (StringUtil.isEmpty(row)) {
                continue;
            }
            String[] rows = row.split(",");
            if (rows.length != 2) {
                continue;
            }
            if (rows[1].equals(dataRow[1])) {
                //链接重复，以前的不要了
                continue;
            }
            if (rows[0].equals(dataRow[0])) {
                //标题重复，以前的不要了
                continue;
            }
            text.append(channels[i]).append("\n");
            count++;
        }
        text.append(data);
        count++;
        savetoDiy(name, text.toString(), refresh);
        return count;
    }

    public static void savetoDiy(String name, String data) {
        savetoDiy(name, data, true);
    }

    public static void savetoDiy(String name, String data, boolean refresh) {
        try {
            FileOutputStream out = new FileOutputStream(new File(DIR_DIY + "/" + name + ".txt"));
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            osw.write(data);
            osw.close();
            out.close();
            if(refresh) {
                PlayerActivity.loadChannelData();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playLastVideo(String type) {
        PlayerActivity.playLastVideo(type);
    }

    public static void playThisVideo(String type, String title) {
        PlayerActivity.playThisVideo(type, title);
    }

    public static void deleteFromDiy(int deleteId) {
        try {
            File file = new File(DIR_DIY);
            File[] listFiles = file.listFiles();
            if (null == listFiles) return;
            int id = 0;
            for (File file2 : listFiles) {
                id++;
                if (id == deleteId) {
                    file2.delete();
                }
            }
            PlayerActivity.loadChannelData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDiy(String fileName) {
        try {
            File file = new File(DIR_DIY);
            File[] listFiles = file.listFiles();
            if (null == listFiles) return;
            for (File file2 : listFiles) {
                if (file2.getName().equals(fileName + ".txt")) {
                    file2.delete();
                }
            }
            PlayerActivity.loadChannelData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static JSONObject jsonArrayFromDiy() {
        JSONObject jsonObject = new JSONObject();
        File file = new File(DIR_DIY);
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File[] listFiles = file.listFiles();
            if (null == listFiles) return null;
            JSONArray jsonArray = new JSONArray();
            int id = 0;
            for (File file2 : listFiles) {
                id++;
                String name = file2.getName();
                if (name.endsWith(".txt")) {
                    name = name.substring(0, name.lastIndexOf("."));
                    JSONObject object = new JSONObject();
                    object.put("id", "" + id);
                    object.put("name", name);
                    object.put("data", FileUtil.fileToString(file2.getAbsolutePath()));
                    jsonArray.put(object);
                }
            }
            jsonObject.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static void clearDiy() {
        try {
            File file = new File(DIR_DIY);
            if (!file.exists()) {
                file.mkdirs();
            }
            File[] listFiles = file.listFiles();
            if (null == listFiles) return;
            for (File file2 : listFiles) {
                file2.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> getChannel(int num) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        try {
            for (int i = 0; i < size(); i++) {
                for (int j = 0; j < get(i).data.size(); j++) {
                    if (num == get(i).data.get(j).num) {
                        map.put("type", i);
                        map.put("pos", j);
                        return map;
                    }
                }
            }
        } catch (Exception e) {
        }
        map.put("type", 0);
        map.put("pos", 0);
        return map;
    }

    public int getLastChannel() {
        try {
            return get(size() - 1).data.get(get(size() - 1).data.size() - 1).num;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 压缩
     *
     * @param data 待压缩数据
     * @return byte[] 压缩后的数据
     */
    public static byte[] compress(byte[] data) {
        byte[] output = new byte[0];

        Deflater compresser = new Deflater();

        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        return output;
    }

    public static byte[] gzuncompress(byte[] data) {
        byte[] unCompressed = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        Inflater deCompressor = new Inflater();
        try {
            deCompressor.setInput(data);
            final byte[] buf = new byte[256];
            while (!deCompressor.finished()) {
                int count = deCompressor.inflate(buf);
                bos.write(buf, 0, count);
            }
            unCompressed = bos.toByteArray();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deCompressor.end();
        }

        return unCompressed;
    }

    public String toJson() {
        try {
            String json = "[";
            for (int i = 0; i < size(); i++) {
                if (i == 0) {
                    json += get(i).toJson();
                } else {
                    json += "," + get(i).toJson();
                }
            }
            json += "]";
            return json;
        } catch (Exception e) {
        }
        return "";
    }

}
