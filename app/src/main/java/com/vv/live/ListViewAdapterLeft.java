package com.vv.live;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vv.live.ui.PlayerActivity;

import java.util.ArrayList;

public class ListViewAdapterLeft extends BaseAdapter {
    private int currentPlayPos = 0;  
    private int currentSelectPos = 0;  
    private Context mContext;  
    private ArrayList<ChannelData> list;
	public static float fontSize=20;  
	
	public ListViewAdapterLeft(Context mContext, ArrayList<ChannelData> channelDatas,int selection) {  
		this.mContext=mContext;
		this.list=channelDatas;
		this.currentPlayPos=selection;
		this.currentSelectPos=selection;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		 return list == null ? 0 : list.size();  
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position).name;  
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		 return position;  
	}
	
	public void setCurrentSelectPos(int pos){
		currentSelectPos=pos;
		notifyDataSetChanged();
	}

	public void setFontSize(float size){
		fontSize=size;
		notifyDataSetChanged();
	}
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 ViewHolder viewHolder = null;  
	        if (convertView == null) {  
	            viewHolder = new ViewHolder();  
	            convertView = LayoutInflater.from(mContext).inflate(  
	                    R.layout.item_list_left, parent, false);  
	            viewHolder.txt_item = (TextView) convertView  
	                    .findViewById(R.id.txt_item);  
	            convertView.setTag(viewHolder);  
	        } else {  
	            viewHolder = (ViewHolder) convertView.getTag();  
	        }  
	        String typeName=getItem(position).toString();
	        viewHolder.txt_item.setText(typeName);  
	        viewHolder.txt_item.setTextSize(TypedValue.COMPLEX_UNIT_PX,fontSize);
	        
	        if (position == currentSelectPos) {// 选中时设置单纯颜色 
        		viewHolder.txt_item.setActivated(true);
	        }else{
        		viewHolder.txt_item.setActivated(false);
	        }
	        
	        LayoutParams lp = viewHolder.txt_item.getLayoutParams();
	        lp.height= PlayerActivity.leftHeight;
	        viewHolder.txt_item.setLayoutParams(lp);
	        
	        return convertView;  
	}
	
    class ViewHolder {  
        TextView txt_item;  
    }


}
