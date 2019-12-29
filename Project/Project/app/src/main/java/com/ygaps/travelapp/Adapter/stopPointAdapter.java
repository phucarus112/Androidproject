
package com.ygaps.travelapp.Adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygaps.travelapp.APIConnect.APIService;
import com.ygaps.travelapp.APIConnect.DetailServiceResponse;
import com.ygaps.travelapp.APIConnect.ListCommentServiceResponse;
import com.ygaps.travelapp.APIConnect.PointServiceResponse;
import com.ygaps.travelapp.APIConnect.ResponseBody;
import com.ygaps.travelapp.APIConnect.RetrofitClient;
import com.ygaps.travelapp.APIConnect.StopPointObjectEdit;
import com.ygaps.travelapp.Data.Province;
import com.ygaps.travelapp.Data.ServiceType;
import com.ygaps.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class stopPointAdapter  extends RecyclerView.Adapter<stopPointAdapter.ViewHolder> implements Filterable {
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView tvName;
        public TextView tvComment;
        public TextView tvTime;
        LinearLayout ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageavatar);
            tvName=(TextView)itemView.findViewById(R.id.tvNamecm);
            tvComment=(TextView)itemView.findViewById(R.id.tvComment);
            tvTime=(TextView)itemView.findViewById(R.id.tvtime);
            ll = (LinearLayout)itemView.findViewById(R.id.ll);
        }
    }

    private List<StopPointObjectEdit> mListTour;
    private List<StopPointObjectEdit> mListTourFull;
    Context context;
    ArrayList<Province> provinceArrayList = new ArrayList<>();
    ArrayList<ServiceType> serviceTypeArrayList = new ArrayList<>();

    public stopPointAdapter(List<StopPointObjectEdit> contacts,Context context1) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
        this.context = context1;
        setUpProvince();
        setUpServiceTypeId();
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

    private void setUpServiceTypeId() {

        serviceTypeArrayList.add(new ServiceType(1, "Restaurant"));
        serviceTypeArrayList.add(new ServiceType(2, "Hotel"));
        serviceTypeArrayList.add(new ServiceType(3, "Rest Station"));
        serviceTypeArrayList.add(new ServiceType(4, "Other"));
    }

    private void setUpProvince() {
        provinceArrayList.add(new Province(1, "Hồ Chí Minh", "SG"));
        provinceArrayList.add(new Province(2, "Hà Nội", "HN"));
        provinceArrayList.add(new Province(3, "Đà Nẵng", "DDN"));
        provinceArrayList.add(new Province(4, "Bình Dương", "BD"));
        provinceArrayList.add(new Province(5, "Đồng Nai", "DNA"));
        provinceArrayList.add(new Province(6, "Khánh Hòa", "KH"));
        provinceArrayList.add(new Province(7, "Hải Phòng", "HP"));
        provinceArrayList.add(new Province(8, "Long An", "LA"));
        provinceArrayList.add(new Province(9, "Quảng Nam", "QNA"));
        provinceArrayList.add(new Province(10, "Bà Rịa Vũng Tàu", "VT"));
        provinceArrayList.add(new Province(11, "Đắk Lắk", "DDL"));
        provinceArrayList.add(new Province(12, "Cần Thơ", "CT"));
        provinceArrayList.add(new Province(13, "Bình Thuận ", "BTH"));
        provinceArrayList.add(new Province(14, "Lâm Đồng", "LDD"));
        provinceArrayList.add(new Province(15, "Thừa Thiên Huế", "TTH"));
        provinceArrayList.add(new Province(16, "Kiên Giang", "KG"));
        provinceArrayList.add(new Province(17, "Bắc Ninh", "BN"));
        provinceArrayList.add(new Province(18, "Quảng Ninh", "QNI"));
        provinceArrayList.add(new Province(19, "Thanh Hóa", "TH"));
        provinceArrayList.add(new Province(20, "Nghệ An", "NA"));
        provinceArrayList.add(new Province(21, "Hải Dương", "HD"));
        provinceArrayList.add(new Province(22, "Gia Lai", "GL"));
        provinceArrayList.add(new Province(23, "Bình Phước", "BP"));
        provinceArrayList.add(new Province(24, "Hưng Yên", "HY"));
        provinceArrayList.add(new Province(25, "Bình Định", "BDD"));
        provinceArrayList.add(new Province(26, "Tiền Giang", "TG"));
        provinceArrayList.add(new Province(27, "Thái Bình", "TB"));
        provinceArrayList.add(new Province(28, "Bắc Giang", "BG"));
        provinceArrayList.add(new Province(29, "Hòa Bình", "HB"));
        provinceArrayList.add(new Province(30, "An Giang", "AG"));
        provinceArrayList.add(new Province(31, "Vĩnh Phúc", "VP"));
        provinceArrayList.add(new Province(32, "Tây Ninh", "TNI"));
        provinceArrayList.add(new Province(33, "Thái Nguyên", "TN"));
        provinceArrayList.add(new Province(34, "Lào Cai", "LCA"));
        provinceArrayList.add(new Province(35, "Nam Định", "NDD"));
        provinceArrayList.add(new Province(36, "Quảng Ngãi", "QNG"));
        provinceArrayList.add(new Province(37, "Bến Tre", "BTR"));
        provinceArrayList.add(new Province(38, "Đắk Nông", "DNO"));
        provinceArrayList.add(new Province(39, "Cà Mau", "CM"));
        provinceArrayList.add(new Province(40, "Vĩnh Long", "VL"));
        provinceArrayList.add(new Province(41, "Ninh Bình", "NB"));
        provinceArrayList.add(new Province(42, "Phú Thọ", "PT"));
        provinceArrayList.add(new Province(43, "Ninh Thuận", "NT"));
        provinceArrayList.add(new Province(44, "Phú Yên ", "PY"));
        provinceArrayList.add(new Province(45, "Hà Nam", "HNA"));
        provinceArrayList.add(new Province(46, "Hà Tĩnh", "HT"));
        provinceArrayList.add(new Province(47, "Đồng Tháp", "DDT"));
        provinceArrayList.add(new Province(48, "Sóc Trăng", "ST"));
        provinceArrayList.add(new Province(49, "Kon Tum", "KT"));
        provinceArrayList.add(new Province(50, "Quảng Bình", "QB"));
        provinceArrayList.add(new Province(51, "Quảng Trị", "QT"));
        provinceArrayList.add(new Province(52, "Trà Vinh", "TV"));
        provinceArrayList.add(new Province(53, "Hậu Giang", "HGI"));
        provinceArrayList.add(new Province(54, "Sơn La", "SL"));
        provinceArrayList.add(new Province(55, "Bạc Liêu", "BL"));
        provinceArrayList.add(new Province(56, "Yên Bái", "YB"));
        provinceArrayList.add(new Province(57, "Tuyên Quang", "TQ"));
        provinceArrayList.add(new Province(58, "Điện Biên", "DDB"));
        provinceArrayList.add(new Province(59, "Lai Châu", "LCH"));
        provinceArrayList.add(new Province(60, "Lạng Sơn", "LS"));
        provinceArrayList.add(new Province(61, "Hà Giang", "HG"));
        provinceArrayList.add(new Province(62, "Bắc Kạn", "BK"));
        provinceArrayList.add(new Province(63, "Cao Bằng", "CB"));
    }

    @Override
    public void onBindViewHolder(@NonNull stopPointAdapter.ViewHolder viewHolder, final int position) {

        StopPointObjectEdit X = mListTour.get(position);
        TextView Gia = viewHolder.tvName;
        Gia.setText(X.getName());

        TextView nguoi3=viewHolder.tvComment;
        nguoi3.setText(X.getArrivalAt() + " - "+X.getLeaveAt());
        ImageView anh=viewHolder.image;
        TextView Time=viewHolder.tvTime;
        Time.setText(X.getMinCost() + "vnd - "+X.getMaxCost()+"vnd");

        LinearLayout ll = viewHolder.ll;
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences2 = ((Activity)context).getSharedPreferences("tokenUser",MODE_PRIVATE);
                final String Token =  sharedPreferences2.getString("token","");
                Retrofit retrofit2 = RetrofitClient.getClient();
                APIService apiService2 = retrofit2.create(APIService.class);
                final int finalIndex = position;
                final int finalIndex1 = position;
                final int finalIndex2 = position;
                apiService2.detailService(Token, mListTour.get(position).getServiceId())
                        .enqueue(new Callback<DetailServiceResponse>() {
                            @Override
                            public void onResponse(Call<DetailServiceResponse> call, final Response<DetailServiceResponse> response) {
                                if (response.isSuccessful()) {
                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.detail_service_explore);
                                    EditText name = (EditText) dialog.findViewById(R.id.etStopPointExplore);
                                    EditText addr = (EditText) dialog.findViewById(R.id.etAddressExplore);
                                    EditText minCost = (EditText) dialog.findViewById(R.id.etMinCostExplore);
                                    EditText maxCost = (EditText) dialog.findViewById(R.id.etMaxCostExplore);
                                    EditText service = (EditText) dialog.findViewById(R.id.spinnerServiceTypeExplore);
                                    EditText province = (EditText) dialog.findViewById(R.id.spinnerCityExplore);
                                    Button send = (Button) dialog.findViewById(R.id.btnFeedbackExplore);
                                    final EditText etFB = (EditText) dialog.findViewById(R.id.etFeedbackExplore);
                                    final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBarExplore);
                                    final TextView pointAvg = (TextView)dialog.findViewById(R.id.pointFB);
                                    final TextView count = (TextView)dialog.findViewById(R.id.tongDanhGiaService);
                                    final RatingBar ratingBar1 = (RatingBar)dialog.findViewById(R.id.ratingService);
                                    final ProgressBar p1 = (ProgressBar)dialog.findViewById(R.id.progressBar1Service);
                                    final ProgressBar p2 = (ProgressBar)dialog.findViewById(R.id.progressBar2Service);
                                    final ProgressBar p3 = (ProgressBar)dialog.findViewById(R.id.progressBar3Service);
                                    final ProgressBar p4 = (ProgressBar)dialog.findViewById(R.id.progressBar4Service);
                                    final ProgressBar p5 = (ProgressBar)dialog.findViewById(R.id.progressBar5Service);

                                    name.setFocusable(false);
                                    addr.setFocusable(false);
                                    minCost.setFocusable(false);
                                    maxCost.setFocusable(false);
                                    service.setFocusable(false);
                                    province.setFocusable(false);

                                    //lay ds comment cua service
                                    Retrofit retrofit4 = RetrofitClient.getClient();
                                    final APIService apiService4 = retrofit4.create(APIService.class);
                                    apiService4.getListCommentService(Token, mListTour.get(finalIndex2).getServiceId(), 1, 500)
                                            .enqueue(new Callback<ListCommentServiceResponse>() {
                                                @Override
                                                public void onResponse(Call<ListCommentServiceResponse> call, Response<ListCommentServiceResponse> response) {
                                                    if (response.isSuccessful()) {
                                                        ArrayList<ListCommentServiceResponse.CommentUser> list = response.body().getCommentUsers();
                                                        Log.e("size", String.valueOf(list.size()));
                                                        if (list != null) {
                                                            ListView listView = (ListView) dialog.findViewById(R.id.lvComment);
                                                            MyAdapterCommentService myAdapter = new
                                                                    MyAdapterCommentService((Activity)context, list);
                                                            listView.setAdapter(myAdapter);
                                                        }

                                                    } else {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ListCommentServiceResponse> call, Throwable t) {
                                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    dialog.show();

                                    name.setText(response.body().getName());
                                    addr.setText(response.body().getAddress());
                                    minCost.setText(String.valueOf(response.body().getMinCost()));
                                    maxCost.setText(String.valueOf(response.body().getMaxCost()));
                                    for (int i = 0; i < serviceTypeArrayList.size(); i++) {
                                        if (response.body().getServiceTypeId() == serviceTypeArrayList.get(i).getId()) {
                                            service.setText(serviceTypeArrayList.get(i).getService_name());
                                            break;
                                        }
                                    }

                                    for (int i = 0; i < provinceArrayList.size(); i++) {
                                        if (response.body().getProvinceId() == provinceArrayList.get(i).getId()) {
                                            province.setText(provinceArrayList.get(i).getName());
                                            break;
                                        }
                                    }

                                    //set gia tri cho point
                                    Retrofit re = RetrofitClient.getClient();
                                    final APIService api = re.create(APIService.class);
                                    api.getPointFeedbackService(Token,response.body().getId())
                                            .enqueue(new Callback<PointServiceResponse>() {
                                                @Override
                                                public void onResponse(Call<PointServiceResponse> call, Response<PointServiceResponse> response) {
                                                    if(response.isSuccessful())
                                                    {
                                                        Log.e("size", String.valueOf(response.body().getPointStats().size()));
                                                        int countTurn =0;
                                                        float avg = 0F;
                                                        for(int z = 0 ;z < response.body().getPointStats().size();z++)
                                                        {
                                                            countTurn+=response.body().getPointStats().get(z).getTotal();

                                                            avg+= response.body().getPointStats().get(z).getTotal()
                                                                    *response.body().getPointStats().get(z).getPoint();
                                                        }
                                                        count.setText(String.valueOf(countTurn));
                                                        if(countTurn!=0)pointAvg.setText(String.valueOf(avg/countTurn)+"/5");
                                                        ratingBar1.setRating(avg/countTurn);
                                                        if(countTurn > 0)
                                                        {
                                                            p1.setProgress(100*response.body().getPointStats().get(0).getTotal()/countTurn);
                                                            p2.setProgress(100*response.body().getPointStats().get(1).getTotal()/countTurn);
                                                            p3.setProgress(100*response.body().getPointStats().get(2).getTotal()/countTurn);
                                                            p4.setProgress(100*response.body().getPointStats().get(3).getTotal()/countTurn);
                                                            p5.setProgress(100*response.body().getPointStats().get(4).getTotal()/countTurn);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PointServiceResponse> call, Throwable t) {
                                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    ///

                                    send.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (etFB.getText().toString().equals("")) {
                                                Toast.makeText(context, "No comment! Try again", Toast.LENGTH_SHORT).show();
                                            } else if (ratingBar.getRating() == 0) {
                                                Toast.makeText(context, "Please report point", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Retrofit retrofit3 = RetrofitClient.getClient();
                                                APIService apiService3 = retrofit3.create(APIService.class);

                                                apiService3.sendFeedback(Token, mListTour.get(finalIndex1).getServiceId(), etFB.getText().toString().trim(), (int) ratingBar.getRating())
                                                        .enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if (response.isSuccessful()) {
                                                                    etFB.setText("");
                                                                    ratingBar.setRating(0F);
                                                                    // Toast.makeText(ExploreActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                                                    apiService4.getListCommentService(Token, mListTour.get(finalIndex2).getServiceId(), 1, 500)
                                                                            .enqueue(new Callback<ListCommentServiceResponse>() {
                                                                                @Override
                                                                                public void onResponse(Call<ListCommentServiceResponse> call, Response<ListCommentServiceResponse> response) {
                                                                                    if (response.isSuccessful()) {

                                                                                        ArrayList<ListCommentServiceResponse.CommentUser> list = response.body().getCommentUsers();
                                                                                        Collections.reverse(list);
                                                                                        Log.e("size", String.valueOf(list.size()));
                                                                                        if (list != null) {
                                                                                            ListView listView = (ListView) dialog.findViewById(R.id.lvComment);
                                                                                            MyAdapterCommentService myAdapter = new
                                                                                                    MyAdapterCommentService((Activity)context, list);
                                                                                            listView.setAdapter(myAdapter);
                                                                                        }

                                                                                    } else {
                                                                                        try {
                                                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                            Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<ListCommentServiceResponse> call, Throwable t) {
                                                                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                } else {
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DetailServiceResponse> call, Throwable t) {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
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