package com.vv.live;

import java.util.ArrayList;

public class ChannelData {
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ChannelObject> getData() {
		return data;
	}

	public void setData(ArrayList<ChannelObject> data) {
		this.data = data;
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
	}

	public boolean isHasPassed() {
		return hasPassed;
	}

	public void setHasPassed(boolean hasPassed) {
		this.hasPassed = hasPassed;
	}

	public String name;
    public ArrayList<ChannelObject> data = new ArrayList<>();
    public String psw = "";
    public boolean hasPassed = false;

    public String toJson() {
        String json = "";
        json = "{\"name\":\"" + name + "\",\"data\":[";
        for (int i = 0; i < data.size(); i++) {
            if (i == 0) {
                json += data.get(i).toJson();
            } else {
                json += "," + data.get(i).toJson();
            }
        }
        json += "]}";
        return json;
    }
}
