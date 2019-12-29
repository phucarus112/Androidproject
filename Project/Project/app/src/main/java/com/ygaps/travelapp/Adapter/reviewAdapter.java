package com.ygaps.travelapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygaps.travelapp.APIConnect.ReviewList;
import com.ygaps.travelapp.R;

import java.util.ArrayList;
import java.util.List;

public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder> implements Filterable {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tvName;
        public TextView tvDanhGia;
        public TextView tvTime;
        public RatingBar rating;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageavatar);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            rating=(RatingBar)itemView.findViewById(R.id.rating);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvDanhGia=(TextView)itemView.findViewById(R.id.tvDanhGia);

        }
    }

    private List<ReviewList> mListTour;
    private List<ReviewList> mListTourFull;
    public reviewAdapter(List<ReviewList> contacts) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
    };
    @NonNull
    @Override
    public reviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.itemreview, parent, false);
        reviewAdapter.ViewHolder viewHolder = new reviewAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull reviewAdapter.ViewHolder viewHolder, int position) {

        ReviewList X = mListTour.get(position);
        TextView Gia = viewHolder.tvName;
        Gia.setText(X.getName());
        RatingBar ratingBar=viewHolder.rating;
        ratingBar.setRating(X.getPoint());
        TextView time=viewHolder.tvTime;
        time.setText(X.getCreatedOn());
        TextView danhgia=viewHolder.tvDanhGia;
        danhgia.setText(X.getReview());

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