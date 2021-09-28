package com.vv.live;

import java.util.ArrayList;

public class SettingOption {
	public String mLeftValue;
	public ArrayList<String> mRightList;
	
	public SettingOption(String name) {
		mRightList=new ArrayList<String>();
		mLeftValue=name;
	}
}
