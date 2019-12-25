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

import com.example.project.APIConnect.MyMember;
import com.example.project.APIConnect.commentList;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class memberAdapter extends RecyclerView.Adapter<memberAdapter.ViewHolder> implements Filterable {


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

    private List<MyMember> mListTour;
    private List<MyMember> mListTourFull;
    public memberAdapter(List<MyMember> contacts) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
    };
    @NonNull
    @Override
    public memberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.itemtuychon, parent, false);
        memberAdapter.ViewHolder viewHolder = new memberAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull memberAdapter.ViewHolder viewHolder, int position) {

        MyMember X = mListTour.get(position);
        TextView Gia = viewHolder.tvName;
        Gia.setText(X.getName());
        TextView nguoi3=viewHolder.tvComment;
        nguoi3.setText(X.getPhone());
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
