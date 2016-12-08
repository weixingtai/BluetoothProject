package com.lumex.bluetoothproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lumex.bluetoothproject.R;
import com.lumex.bluetoothproject.util.db.Command;

import java.util.List;

/**
 * Created by 阿泰Charles on 2016/12/08.
 */

public class CommandAdapter extends BaseAdapter{
    private List<Command> mCommandList;
    private Context mContext;
    public CommandAdapter(Context mContext,List<Command> mCommandList){
        this.mCommandList = mCommandList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mCommandList==null?0:mCommandList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.lv_command_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvCaption = (TextView) view.findViewById(R.id.tv_command_list_item);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Command command = mCommandList.get(i);
        viewHolder.tvCaption.setText(String.valueOf(command.getCommandCaption()));
        return view;
    }
    class ViewHolder{
        TextView tvCaption;
    }
}
