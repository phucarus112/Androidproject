package com.example.project;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter_StopPointList extends BaseAdapter {

    Activity activity;
    ArrayList<StopPointObject> items;

    public MyAdapter_StopPointList(Activity activity, ArrayList<StopPointObject> items){
        this.activity = activity;
        this.items=items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Gọi layoutInflater ra để bắt đầu ánh xạ view và data.
        LayoutInflater inflater = activity.getLayoutInflater();

        // Đổ dữ liệu vào biến View, view này chính là những gì nằm trong item_name.xml
        convertView = inflater.inflate(R.layout.item_stop_point, null);

        TextView tv = (TextView)convertView.findViewById(R.id.tvNameStopPoint);
        ImageButton imgBtn = (ImageButton)convertView.findViewById(R.id.imgBtnDelete);

        tv.setText(items.get(position).name);

        return convertView;
    }
}
