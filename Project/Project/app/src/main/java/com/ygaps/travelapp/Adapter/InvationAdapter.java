package com.ygaps.travelapp.Adapter;
import android.content.SharedPreferences;
import android.net.Uri;

import com.ygaps.travelapp.APIConnect.APIService;
import com.ygaps.travelapp.APIConnect.InivationResponse;
import com.ygaps.travelapp.APIConnect.ResponseBody;
import com.ygaps.travelapp.APIConnect.RetrofitClient;
import com.ygaps.travelapp.APIConnect.Tour;
import com.ygaps.travelapp.APIConnect.TourInvation;
import com.ygaps.travelapp.Activity.MainActivity;
import com.ygaps.travelapp.Activity.NotificationActivity;
import com.ygaps.travelapp.BuildConfig;
import com.ygaps.travelapp.R;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class InvationAdapter  extends RecyclerView.Adapter<InvationAdapter.ViewHolder> implements Filterable{



    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView invication;
        public TextView time;
        public Button btCF;
        public Button delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageavatar);
            invication=(TextView)itemView.findViewById(R.id.tvInvication);
            time=(TextView)itemView.findViewById(R.id.tvTime);
            btCF=(Button) itemView.findViewById(R.id.btCF);
            delete=(Button) itemView.findViewById(R.id.btDelete);

        }
    }
    private Context context;
    private List<TourInvation> mListTour;
    private List<TourInvation> mListTourFull;
    private static SharedPreferences prefs;
    final String token;
    public InvationAdapter(List<TourInvation> contacts,Context context ) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
        prefs = context.getSharedPreferences("tokenUser",MODE_PRIVATE);
        token = (prefs.getString("token", ""));
        this.context=context;
    };


    @Override
    public InvationAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_notification, parent, false);
        InvationAdapter.ViewHolder viewHolder = new InvationAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InvationAdapter.ViewHolder viewHolder, final int position) {
        final  TourInvation X = mListTour.get(position);
        TextView invi = viewHolder.invication;
        invi.setText(X.getHostName()+" sent you invation to tour "+ X.getName());
        TextView Time=viewHolder.time;
        Time.setText(X.getCreatedOn());
        Button bt1=viewHolder.btCF;
        Button bt2=viewHolder.delete;
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);
                apiService.ResponseInivation(token,X.getId().toString(),true).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        mListTour.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Confim", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = RetrofitClient.getClient();
                APIService apiService = retrofit.create(APIService.class);
                apiService.ResponseInivation(token,X.getId().toString(),false).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        mListTour.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
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
            List<TourInvation> filterList=new ArrayList<>();

            if(constraint==null || constraint.length()==0)
            {
                filterList.addAll(mListTourFull);
            }
            else
            {
                String pattern =constraint.toString().toLowerCase().trim();
                for(TourInvation item : mListTourFull)
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
