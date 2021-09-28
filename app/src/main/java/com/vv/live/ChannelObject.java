package com.vv.live;

import java.util.ArrayList;

public class ChannelObject {
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getSource() {
		return source;
	}

	public void setSource(ArrayList<String> source) {
		this.source = source;
	}

	public int num;
	public String name;
	public ArrayList<String> source=new ArrayList<String>();
	
	public String getfavor(){
		return name+source.toString();
	}
	public String toJson(){
		String json="";
		json="{\"num\":"+num+",\"name\":\""+name+"\",\"source\":[";
		for(int i=0;i<source.size();i++){
			if(i==0){
				json+="\""+source.get(i)+"\"";
			}else{
				json+=",\""+source.get(i)+"\"";
			}
		}
		json+="]}";
		return json;
	}
}
