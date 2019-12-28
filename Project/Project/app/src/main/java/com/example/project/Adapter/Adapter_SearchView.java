package com.example.project.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.project.APIConnect.SearchUserResponse;
import com.example.project.R;
import java.util.ArrayList;

    public class Adapter_SearchView extends BaseAdapter {
        Activity activity;
        ArrayList<SearchUserResponse.SearchUserObject> items;

        public Adapter_SearchView(Activity activity, ArrayList<SearchUserResponse.SearchUserObject> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
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
            convertView = inflater.inflate(R.layout.item_result_search, null);

            TextView tv = (TextView) convertView.findViewById(R.id.tvNameRS);
            tv.setText(items.get(position).getFullName());
            TextView tv2 = (TextView) convertView.findViewById(R.id.tvEmailRS);
            tv2.setText(items.get(position).getEmail());

            return convertView;
        }
    }
