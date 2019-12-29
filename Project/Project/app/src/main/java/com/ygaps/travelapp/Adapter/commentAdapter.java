package com.ygaps.travelapp.Adapter;

import com.ygaps.travelapp.APIConnect.commentList;
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
public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> implements Filterable {


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tvName;
        public TextView tvComment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageavatar);
            tvName=(TextView)itemView.findViewById(R.id.tvNamecm);
            tvComment=(TextView)itemView.findViewById(R.id.tvComment);

        }
    }

    private List<commentList> mListTour;
    private List<commentList> mListTourFull;
    public commentAdapter(List<commentList> contacts) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
    };
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_comment, parent, false);
        commentAdapter.ViewHolder viewHolder = new commentAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        commentList X = mListTour.get(position);
        TextView Gia = viewHolder.tvName;
        Gia.setText(X.getName());
        TextView nguoi3=viewHolder.tvComment;
        nguoi3.setText(X.getComment());
        ImageView anh=viewHolder.image;

    }

    @Override
    public int getItemCount() {
        return mListTour.size();
    }
    @Override
    public Filter getFilter() {
        return null;
    }

}