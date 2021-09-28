package com.vv.live;

public class FavorObject {
	public int type;
	public int pos;
	public String toJson(){
		return "{\"type\":"+type+",\"pos\":"+pos+"}";
	}
	public FavorObject(int type,int pos){
		this.type=type;
		this.pos=pos;
	}
}
