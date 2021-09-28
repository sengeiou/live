package com.vv.live;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class ChannelManager {
    public static ArrayList<String> cntvNameArrayList = new ArrayList<String>();
    public static ArrayList<String> cntvUrlArrayList = new ArrayList<String>();

    public static ArrayList<String> userNameArrayList = new ArrayList<String>();
    public static ArrayList<String> userUrlArrayList = new ArrayList<String>();


    public static void writefile(Context context, String filename, String data) {
        try {
            FileOutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
            osw.write(data);
            osw.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clearFiles(Context context) throws Exception {
        File filedir = new File(context.getFilesDir().toString());
        File[] files = filedir.listFiles();
        for (File file : files) {
            if (file.getName().contains("data")) {
                file.delete();
            }
        }
    }

    public static String readfile(Context context, String filename) {
        StringBuffer buffer = new StringBuffer();
        try {
            FileInputStream inputStream = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public static void saveSetting(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("live", context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();// 获取编辑器
        editor.putString(key, value);
        editor.commit();// 提交修改
    }

    private static void copyfile(File src, File dst) {
        try {
            FileOutputStream fos = new FileOutputStream(dst);
            InputStream is = new FileInputStream(src);
            byte[] buf = new byte[1024];
            int i = 0;
            while ((i = is.read(buf)) != -1) {
                fos.write(buf);
            }
            fos.flush();
            fos.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void copyAssetFile(Context context, String srcFileName, String dstFile) {
        try {
            InputStream is = context.getAssets().open(srcFileName);
            FileOutputStream fos = new FileOutputStream(dstFile);
            int br;
            byte[] buf = new byte[1024];
            while (-1 != (br = is.read(buf))) {
                fos.write(buf, 0, br);
            }
            is.close();
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    if ("tun0".equals(intf.getName())) {
                        return true;
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void downLoad(String path, Context context) throws Exception {
        File file = new File(context.getFilesDir() + "/home_bj.png");

        URL url = new URL(path);
        InputStream is = url.openStream();
        OutputStream os = context.openFileOutput("home_bj.png", Context.MODE_PRIVATE);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }
        is.close();
        os.close();
    }

    private static String httpget(String geturl) {
        try {
            URL url = new URL(geturl);

            URLConnection connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.connect();
            String result = "";
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line + "\n";
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }
}
