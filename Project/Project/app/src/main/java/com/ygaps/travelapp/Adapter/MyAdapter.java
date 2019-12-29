package com.ygaps.travelapp.Adapter;

import android.net.Uri;

import com.ygaps.travelapp.APIConnect.Tour;
import com.ygaps.travelapp.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter  extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView lich;
        public TextView nguoi1;
        public TextView nguoi2;
        public TextView gia;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageChinh);
            name=(TextView)itemView.findViewById(R.id.tvName);
            lich=(TextView)itemView.findViewById(R.id.tvLich);
            nguoi1=(TextView)itemView.findViewById(R.id.tvNguoi1);
            nguoi2=(TextView)itemView.findViewById(R.id.tvNguoi2);
            gia=(TextView)itemView.findViewById(R.id.tvGia);
        }
    }
    private List<Tour> mListTour;
    private List<Tour> mListTourFull;
    public MyAdapter(List<Tour> contacts) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
    };

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.itemtour, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int position) {
        Tour X = mListTour.get(position);
        TextView Gia = viewHolder.gia;
        Gia.setText(X.getMinCost()+" - "+X.getMaxCost());
        TextView nguoi3=viewHolder.nguoi1;
        nguoi3.setText(X.getAdults().toString());
        TextView nguoi2 =viewHolder.nguoi2;
        nguoi2.setText(X.getChilds().toString());
        TextView lich=viewHolder.lich;
        lich.setText(X.getStartDate()+" - "+X.getEndDate());
        TextView name=viewHolder.name;
        name.setText(X.getName());
        ImageView anh=viewHolder.image;
        anh.setImageURI((Uri) X.getAvatar());
    }

    @Override
    public int getItemCount() {
        return mListTour.size();
    }
    @Override
    public Filter getFilter() {
        return exFilter;
    };
    private Filter exFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Tour> filterList=new ArrayList<>();

            if(constraint==null || constraint.length()==0)
            {
                filterList.addAll(mListTourFull);
            }
            else
            {
                String pattern =constraint.toString().toLowerCase().trim();
                for(Tour item : mListTourFull)
                {
                    if(item.getName().toLowerCase().contains(pattern)){
                        filterList.add(item);
                    } } }
            FilterResults result=new FilterResults();
            result.values=filterList;
            return result;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListTour.clear();
            mListTour.addAll((List)results.values);
            notifyDataSetChanged();
        }};
}