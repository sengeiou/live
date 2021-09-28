package net.media;

public class mpc {
    private static mpc mp;
    private static boolean isLoaded = false;
    private static int port = 8523;

    private mpc() {
        try {
            if (!isLoaded) {
                isLoaded = true;
                System.loadLibrary("mpc_jni");
                if (MPCStart(port, "mis.tvbdo.info", "5000", "5002", 1) != -1) {
                    MPCAddMISBackup("mis-backup.tvbdo.info", "5000", "5002", 1);
                }
            }
        } catch (Exception e) {
        }
    }

    public String getPro() {
        return "hls";
    }

    public static mpc getInstance() {
        if (mp == null) {
            mp = new mpc();
        }
        return mp;
    }

    public String getUrl(String paramString) {
        paramString = paramString.substring(6);
        return "http://127.0.0.1:" + port + "/" + paramString + ".m3u8?t=" + "&u=tvb@gmail.com&p=0&resp_mode=1&i=akamai";
    }

    public native int MPCAddMISBackup(String paramString1, String paramString2, String paramString3, int paramInt);

    public native int MPCReset();

    public native int MPCStart(int paramInt1, String paramString1, String paramString2, String paramString3, int paramInt2);

    public native int MPCStop();
}
