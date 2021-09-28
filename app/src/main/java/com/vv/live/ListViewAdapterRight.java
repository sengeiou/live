package com.vv.live;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.vv.live.ui.PlayerActivity;

import java.util.ArrayList;

public class ListViewAdapterRight extends BaseAdapter {
    private int defaultSelection = 0;
    private Context mContext;
    private ArrayList<ChannelObject> list;
    public static float fontSize = 20;
    private int currentSelectPos = 0;

    public ListViewAdapterRight(Context mContext, ArrayList<ChannelObject> tmpNameArrayList, int selection) {
        this.mContext = mContext;
        this.list = tmpNameArrayList;
        this.defaultSelection = selection;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        ChannelText channelText = new ChannelText();
        channelText.num = list.get(position).num + "";
        channelText.text = list.get(position).name;
        return channelText;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setSelection(int position) {
        defaultSelection = position;
        notifyDataSetChanged();
    }

    public void setFontSize(float size) {
        fontSize = size;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_right, parent, false);
            viewHolder.txt_num = (TextView) convertView.findViewById(R.id.txt_num);
            viewHolder.txt_item = (TextView) convertView.findViewById(R.id.txt_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ChannelText channelnameString = (ChannelText) getItem(position);
        if (position == currentSelectPos) {// 选中时设置单纯颜色
            viewHolder.txt_item.setActivated(true);
        } else {
            viewHolder.txt_item.setActivated(false);
        }

        viewHolder.txt_num.setText(channelnameString.num);
        viewHolder.txt_num.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize - 5);
        viewHolder.txt_item.setText(channelnameString.text);
        viewHolder.txt_item.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        //收藏过的增加图标
        if (PlayerActivity.favorList.isChannelFavored(PlayerActivity.mLeftPos, position)) {
            try {
                viewHolder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(PlayerActivity.context, R.drawable.favor), null);
            } catch (Exception e) {
            }
        } else {
            try {
                viewHolder.txt_item.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } catch (Exception e) {
            }
        }
        if (PlayerActivity.curType == PlayerActivity.mLeftPos) {
            if (position == defaultSelection) {
                viewHolder.txt_item.setActivated(true);
                viewHolder.txt_num.setActivated(true);
            } else {
                viewHolder.txt_item.setActivated(false);
                viewHolder.txt_num.setActivated(false);
            }
        }
        LayoutParams lp = viewHolder.txt_item.getLayoutParams();
        lp.height = PlayerActivity.rightHeight;
        viewHolder.txt_item.setLayoutParams(lp);

        return convertView;
    }

    class ViewHolder {
        TextView txt_item;
        TextView txt_num;
    }

    class ChannelText {
        public String num;
        public String text;
    }

    public int getchannelnum(int pos) {
        if (pos >= list.size()) {
            return list.get(list.size() - 1).num;
        }
        return list.get(pos).num;
    }

    public void clear() {
        this.list = new ArrayList<ChannelObject>();
        ChannelObject passchannel = new ChannelObject();
        passchannel.name = "请输入密码";
        passchannel.num = 0;
        list.add(passchannel);
        notifyDataSetChanged();
    }

}
