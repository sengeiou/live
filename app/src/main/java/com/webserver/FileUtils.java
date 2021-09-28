package com.webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.vv.live.ChannelDatas;
import com.vv.live.ui.PlayerActivity;

public class FileUtils {
	public static String readassetfile(String fileName){
        StringBuffer sb=new StringBuffer();
        try {
        	InputStream inputStream = PlayerActivity.context.getAssets().open(fileName);
        	BufferedReader br=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
			String str;
			while ((str=br.readLine())!=null){
			    sb.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return sb.toString();
	}
	public static String readtvfile(String fileName){
		StringBuffer sb=new StringBuffer();
		try {
			File file=new File(ChannelDatas.DIR_DIY+"/"+fileName);
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
			String str;
			while ((str=br.readLine())!=null){
				sb.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
}
