package com.ygaps.travelapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ygaps.travelapp.APIConnect.APIService;
import com.ygaps.travelapp.APIConnect.InfoTourResponse;
import com.ygaps.travelapp.APIConnect.ResponseBody;
import com.ygaps.travelapp.APIConnect.RetrofitClient;
import com.ygaps.travelapp.APIConnect.SearchUserResponse;
import com.ygaps.travelapp.APIConnect.StopPointObjectEdit;
import com.ygaps.travelapp.APIConnect.TourHistory;
import com.ygaps.travelapp.APIConnect.UpdateTourResponse;
import com.ygaps.travelapp.Activity.Detail_Review;
import com.ygaps.travelapp.Activity.EditStopPoint;
import com.ygaps.travelapp.Activity.FollowTourActivity;
import com.ygaps.travelapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAdapterHistory extends RecyclerView.Adapter<MyAdapterHistory.ViewHolder> implements Filterable {

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView lich;
        public TextView nguoi1;
        public TextView nguoi2;
        public TextView gia;
        public ImageView btnEdit,btnDelete,btnFollow,btnInvite;
        LinearLayout ll;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.imageChinh_history);
            name=(TextView)itemView.findViewById(R.id.tvName_history);
            lich=(TextView)itemView.findViewById(R.id.tvLich_history);
            nguoi1=(TextView)itemView.findViewById(R.id.tvNguoi1_history);
            nguoi2=(TextView)itemView.findViewById(R.id.tvNguoi2_history);
            gia=(TextView)itemView.findViewById(R.id.tvGia_history);
            btnEdit = (ImageView) itemView.findViewById(R.id.btnEditTour);
            btnDelete = (ImageView) itemView.findViewById(R.id.btnDeleteTour);
            btnFollow = (ImageView)itemView.findViewById(R.id.btnFollowTour);
            btnInvite = (ImageView)itemView.findViewById(R.id.btnInviteMember);
            ll = (LinearLayout) itemView.findViewById(R.id.special);
        }
    }

    private List<TourHistory> mListTour;
    private List<TourHistory> mListTourFull;
    private Context context;
    public MyAdapterHistory(List<TourHistory> contacts, Context context1) {
        this.mListTour = contacts;
        mListTourFull=new ArrayList<>(contacts);
        this.context = context1;
    };

    @Override
    public MyAdapterHistory.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.itemtour_history, parent, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        //get token tu shared preference
        SharedPreferences sharedPreferences = context.getSharedPreferences("tokenUser", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        return viewHolder;
    }

    long milisecondStart, milisecondEnd;
    DatePickerDialog.OnDateSetListener mDateSetListenr,mDateSetListenr2;
    String token;
    String tempName;
    int tempAdults,tempChilds,tempMinCost,tempMaxCost;
    String srcDateEdit="",desDateEdit="";

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final int idTourClickedPosition = mListTour.get(position).getId();
        final boolean kq = mListTour.get(position).getPrivate();

        TourHistory X = mListTour.get(position);
            final int status  = X.getStatus();

            TextView Gia = viewHolder.gia;
            Gia.setText(X.getMinCost() + " - " + X.getMaxCost());
            TextView nguoi3 = viewHolder.nguoi1;
            nguoi3.setText(String.valueOf(X.getAdults()));
            TextView nguoi2 = viewHolder.nguoi2;
            nguoi2.setText(String.valueOf(X.getChilds()));
            TextView lich = viewHolder.lich;
            Log.e("diem di",X.getStartDate());
            Log.e("diem den",X.getEndDate());

        long src =Long.parseLong(X.getStartDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date(src);

        long des =Long.parseLong(X.getEndDate());
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = new Date(des);


        lich.setText(sdf.format(date)+"-"+ sdf1.format(date1));

            TextView name = viewHolder.name;
            name.setText(X.getName());
            ImageView anh = viewHolder.image;
            anh.setImageURI((Uri) X.getAvatar());
            final ImageView edit = viewHolder.btnEdit;
            final ImageView delete = viewHolder.btnDelete;
            final ImageView follow=  viewHolder.btnFollow;
            final ImageView invite = viewHolder.btnInvite;

            LinearLayout linearLayout = viewHolder.ll;
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Detail_Review.class);
                    intent.putExtra("token", token);
                    intent.putExtra("tourId",mListTour.get(position).getId());
                    intent.putExtra("userId",((Activity)context).getIntent().getStringArrayExtra("userId"));
                    ((Activity) context).startActivity(intent);
                }
            });

        ImageView btnEdit = viewHolder.btnEdit;
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_edit_tour);

                final EditText etFullNameEdit = (EditText) dialog.findViewById(R.id.etFullNameEdit);
                final EditText etStartDayEdit = (EditText) dialog.findViewById(R.id.etStartDayEdit);
                final EditText etEndDayEdit = (EditText) dialog.findViewById(R.id.etEndDayEdit);
                final EditText etAdultEdit = (EditText) dialog.findViewById(R.id.etAdultEdit);
                final EditText etChildrenEdit = (EditText) dialog.findViewById(R.id.etChildrenEdit);
                final EditText etMincostEdit = (EditText) dialog.findViewById(R.id.etMinCostEdit);
                final EditText etMaxCostEdit = (EditText) dialog.findViewById(R.id.etMaxCostEdit);
                final TextView tvEditSP = (TextView) dialog.findViewById(R.id.tvEditSP);
                tvEditSP.setPaintFlags(tvEditSP.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                ImageView IMcalendar = (ImageView) dialog.findViewById(R.id.IMcalendar);
                ImageView IMcalendar2 = (ImageView) dialog.findViewById(R.id.IMcalendar2);
                Button btnUpdateTour = (Button) dialog.findViewById(R.id.btnUpdateTour);
              //  ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
                dialog.show();

                etFullNameEdit.setText(mListTour.get(position).getName());
                etAdultEdit.setText(String.valueOf(mListTour.get(position).getAdults()));
                etChildrenEdit.setText(String.valueOf(mListTour.get(position).getChilds()));
                etMincostEdit.setText(mListTour.get(position).getMinCost());
                etMaxCostEdit.setText(mListTour.get(position).getMaxCost());

                milisecondStart = Long.parseLong(mListTour.get(position).getStartDate());
                milisecondEnd = Long.parseLong(mListTour.get(position).getEndDate());

                long src =Long.parseLong(mListTour.get(position).getStartDate());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date(src);

                long des =Long.parseLong(mListTour.get(position).getEndDate());
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
                Date date1 = new Date(des);
                sdf1.format(date1);

                etStartDayEdit.setText(sdf.format(date));
                etEndDayEdit.setText(sdf1.format(date1));

                IMcalendar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        milisecondStart = cal.getTimeInMillis();
                        DatePickerDialog dialog = new DatePickerDialog(
                                context, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                IMcalendar2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);
                        milisecondEnd = cal.getTimeInMillis();
                        DatePickerDialog dialog = new DatePickerDialog(
                                context, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr2, year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                    }
                });

                mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        etStartDayEdit.setText(date);
                        srcDateEdit+=year;
                        if(month<10) srcDateEdit+="0"+String.valueOf(month);
                        else srcDateEdit+=month;
                        if(dayOfMonth<10)srcDateEdit+="0"+dayOfMonth;
                        else srcDateEdit+=dayOfMonth;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            Date dat = sdf.parse(srcDateEdit);
                            milisecondStart = dat.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };

                mDateSetListenr2 = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        etEndDayEdit.setText(date);
                        desDateEdit+=year;
                        if(month<10) desDateEdit+="0"+String.valueOf(month);
                        else desDateEdit+=month;
                        if(dayOfMonth<10)desDateEdit+="0"+dayOfMonth;
                        else desDateEdit+=dayOfMonth;

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        try {
                            Date dat = sdf.parse(desDateEdit);
                            milisecondEnd = dat.getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                };

                tvEditSP.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //lay thong tin tour
                        Retrofit retrofit = RetrofitClient.getClient();
                        final APIService apiService = retrofit.create(APIService.class);
                        apiService.getInfoTour(token,mListTour.get(position).getId())
                                .enqueue(new Callback<InfoTourResponse>() {
                                    @Override
                                    public void onResponse(Call<InfoTourResponse> call, Response<InfoTourResponse> response) {
                                        if (response.isSuccessful()){
                                            final Dialog dialog1 = new Dialog(context);
                                            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                            dialog1.setContentView(R.layout.stop_point_list_edit);
                                            final int sohieuTour = response.body().getId();
                                            final ArrayList<StopPointObjectEdit> spObj = (ArrayList<StopPointObjectEdit>)response.body().getStopPoints();

                                            ListView listView1 = (ListView) dialog1.findViewById(R.id.lvStopPointListEdit);
                                            MyAdapter_StopPointListEdit myAdapter_stopPointList = new
                                                    MyAdapter_StopPointListEdit((Activity)context,spObj);
                                            listView1.setAdapter(myAdapter_stopPointList);
                                            listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    Intent intent= new Intent(context, EditStopPoint.class);
                                                    intent.putExtra("token",token);

                                                    Log.e("so hireu  tour", String.valueOf(sohieuTour));

                                                    intent.putExtra("tourIdSP",String.valueOf(sohieuTour));
                                                    intent.putExtra("latSP",String.valueOf(spObj.get(position).getLat()));
                                                    intent.putExtra("longSP",String.valueOf(spObj.get(position).getLongtitude()));
                                                    intent.putExtra("idSP",String.valueOf(spObj.get(position).getId()));
                                                    intent.putExtra("nameSP",spObj.get(position).getName());
                                                    intent.putExtra("addressSP",spObj.get(position).getAddress());
                                                    intent.putExtra("serviceId",String.valueOf(spObj.get(position).getServiceId()));
                                                    intent.putExtra("serviceSP",String.valueOf(spObj.get(position).getServiceTypeId()));
                                                    intent.putExtra("provinceSP",String.valueOf(spObj.get(position).getProvinceId()));
                                                    intent.putExtra("minCostSP",String.valueOf(spObj.get(position).getMinCost()));
                                                    intent.putExtra("maxCostSP",String.valueOf(spObj.get(position).getMaxCost()));
                                                    intent.putExtra("arrivalSP",String.valueOf(spObj.get(position).getArrivalAt()));
                                                    intent.putExtra("leaveSP",String.valueOf(spObj.get(position).getLeaveAt()));
                                                    intent.putExtra("index",String.valueOf(position));
                                                    ((Activity) context).startActivity(intent);
                                                    dialog1.cancel();

                                                }
                                            });

                                            listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                @Override
                                                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setMessage("Are you sure to remove stop point?")
                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(final DialogInterface dialog, int which) {
                                                                    Retrofit retrofit2 = RetrofitClient.getClient();
                                                                    final APIService apiService2 = retrofit2.create(APIService.class);
                                                                    apiService2.removeStopPoint(token,String.valueOf(spObj.get(position).getId()))
                                                                            .enqueue(new Callback<ResponseBody>() {
                                                                                @Override
                                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                    if(response.isSuccessful())
                                                                                    {
                                                                                        dialog1.cancel();
                                                                                        Toast.makeText(context, "Delete stop point successfully", Toast.LENGTH_SHORT).show();
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
                                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            }).create().show();
                                                    return false;
                                                }
                                            });

                                            dialog1.show();
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
                                    public void onFailure(Call<InfoTourResponse> call, Throwable t) {
                                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                btnUpdateTour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tempName = etFullNameEdit.getText().toString();
                        tempAdults = Integer.parseInt(etAdultEdit.getText().toString());
                        tempChilds = Integer.parseInt(etChildrenEdit.getText().toString());
                        tempMinCost = Integer.parseInt(etMincostEdit.getText().toString());
                        tempMaxCost = Integer.parseInt(etMaxCostEdit.getText().toString());

                        Retrofit retrofit = RetrofitClient.getClient();
                        APIService apiService = retrofit.create(APIService.class);
                        if (etFullNameEdit.getText().toString().equals("")) {
                            Toast.makeText(context, "Invalid Name", Toast.LENGTH_SHORT).show();
                        } else {
                            apiService.updateTour(token, mListTour.get(position).getId(),
                                    etFullNameEdit.getText().toString(), milisecondStart,milisecondEnd,
                                    Integer.parseInt(etAdultEdit.getText().toString()),
                                    Integer.parseInt(etChildrenEdit.getText().toString()),
                                    Integer.parseInt(etMincostEdit.getText().toString()),
                                    Integer.parseInt(etMaxCostEdit.getText().toString()),
                                    mListTour.get(position).getStatus())
                                    .enqueue(new Callback<UpdateTourResponse>() {
                                        @Override
                                        public void onResponse(Call<UpdateTourResponse> call, Response<UpdateTourResponse> response) {
                                            if (response.isSuccessful()) {
                                                Toast.makeText(context, "Edit Successfully", Toast.LENGTH_SHORT).show();
                                               dialog.cancel();
                                                ((Activity) context).finish();
                                                ((Activity) context).startActivity(((Activity) context).getIntent());
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
                                        public void onFailure(Call<UpdateTourResponse> call, Throwable t) {
                                            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });

        ImageView btnDelete = viewHolder.btnDelete;
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to delete this tour?")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                Retrofit retrofit = RetrofitClient.getClient();
                                APIService apiService = retrofit.create(APIService.class);
                                apiService.updateTour(token, mListTour.get(position).getId(),
                                        tempName, milisecondStart, milisecondEnd,
                                        tempAdults,tempChilds,tempMinCost,tempMaxCost,-1)
                                        .enqueue(new Callback<UpdateTourResponse>() {
                                            @Override
                                            public void onResponse(Call<UpdateTourResponse> call, Response<UpdateTourResponse> response) {
                                                if(response.isSuccessful())
                                                {
                                                    Toast.makeText(context, "Delete Successfully", Toast.LENGTH_SHORT).show();
                                                    ((Activity) context).finish();
                                                    ((Activity) context).startActivity(((Activity) context).getIntent());
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
                                            public void onFailure(Call<UpdateTourResponse> call, Throwable t) {
                                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).create().show();
            }
        });

        ImageView btnInvite = viewHolder.btnInvite;
        btnInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog mydialog = new Dialog(context,android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mydialog.setContentView(R.layout.dialog_invite_member);
                final SearchView searchView = (SearchView) mydialog.findViewById(R.id.etInputEmailMember);
                final ListView  listResults = (ListView) mydialog.findViewById(R.id.lvRS);
                mydialog.show();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        String query = searchView.getQuery().toString();
                        Retrofit retrofit = RetrofitClient.getClient();
                        final APIService apiService = retrofit.create(APIService.class);
                        apiService.getListUsersSearch(query,1,1000)
                                .enqueue(new Callback<SearchUserResponse>() {
                                    @Override
                                    public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                                        if(response.isSuccessful()) {

                                            final ArrayList<SearchUserResponse.SearchUserObject> list = response.body().getUserObjects();
                                            Adapter_SearchView adapter_searchView = new Adapter_SearchView(((Activity) context), list);
                                            listResults.setAdapter(adapter_searchView);
                                            listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                    builder.setMessage("Do you want to invite"+list.get(position).getFullName()+" to join in tour?")
                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    //moi ban be
                                                                    Retrofit retrofit2 = RetrofitClient.getClient();
                                                                    APIService apiService2 = retrofit2.create(APIService.class);
                                                                    Log.e("clicked",String.valueOf(idTourClickedPosition));
                                                                    Log.e("id",String.valueOf(list.get(position).getId()));
                                                                    apiService2.inviteMember(token, String.valueOf(idTourClickedPosition), String.valueOf(list.get(position).getId()),kq)
                                                                            .enqueue(new Callback<ResponseBody>() {
                                                                                @Override
                                                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                    if(response.isSuccessful())
                                                                                    {
                                                                                        Toast.makeText(context, "Add member successfully", Toast.LENGTH_SHORT).show();
                                                                                        mydialog.cancel();
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
                                                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                }
                                                            })
                                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            }).create().show();
                                                }
                                            });
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
                                    public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        return false;
                    }
                });
            }
        });

        ImageView imgFollow = viewHolder.btnFollow;
        imgFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FollowTourActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("tourId",String.valueOf(mListTour.get(position).getId()));
                ((Activity) context).startActivity(intent);
            }
        });
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
            List<TourHistory> filterList=new ArrayList<>();

            if(constraint==null || constraint.length()==0)
            {
                filterList.addAll(mListTourFull);
            }
            else
            {
                String pattern =constraint.toString().toLowerCase().trim();
                for(TourHistory item : mListTourFull)
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
