package com.example.project.Adapter;


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

import com.example.project.APIConnect.IforStopPoint;
import com.example.project.APIConnect.MyMember;
import com.example.project.APIConnect.StopPointObjectEdit;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class stopPointAdapter  extends RecyclerView.Adapter<stopPointAdapter.ViewHolder> implements Filterable {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tvName;
        public TextView tvComment;
        public TextView tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageavatar);
            tvName=(TextView)itemView.findViewById(R.id.tvNamecm);
            tvComment=(TextView)itemView.findViewById(R.id.tvComment);
            tvTime=(TextView)itemView.findViewById(R.id.tvtime);

        }
    }

    private List<StopPointObjectEdit> mListTour;
    private List<StopPointObjectEdit> mListTourFull;
    public stopPointAdapter(List<StopPointObjectEdit> contacts) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
    };
    @NonNull
    @Override
    public stopPointAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.stopointindetail, parent, false);
        stopPointAdapter.ViewHolder viewHolder = new stopPointAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull stopPointAdapter.ViewHolder viewHolder, int position) {

        StopPointObjectEdit X = mListTour.get(position);
        TextView Gia = viewHolder.tvName;
        Gia.setText(X.getName());

        TextView nguoi3=viewHolder.tvComment;
        nguoi3.setText(X.getArrivalAt() + " - "+X.getLeaveAt());
        ImageView anh=viewHolder.image;
        TextView Time=viewHolder.tvTime;
        Time.setText(X.getMinCost() + "vnd - "+X.getMaxCost()+"vnd");
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

