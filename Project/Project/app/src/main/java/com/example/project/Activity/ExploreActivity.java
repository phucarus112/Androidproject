package com.example.project.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.DetailServiceResponse;
import com.example.project.APIConnect.InfoTourResponse;
import com.example.project.APIConnect.ListCommentServiceResponse;
import com.example.project.APIConnect.LocationStopPoint;
import com.example.project.APIConnect.PointServiceObject;
import com.example.project.APIConnect.PointServiceResponse;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.SuggestDescObjectResponse;
import com.example.project.APIConnect.SuggestDescRequest;
import com.example.project.APIConnect.SuggestDescResponse;
import com.example.project.Adapter.MyAdapterCommentService;
import com.example.project.Adapter.MyAdapter_StopPointList;
import com.example.project.Data.Province;
import com.example.project.Data.ServiceId_StopPoint;
import com.example.project.Data.ServiceType;
import com.example.project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class ExploreActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    SearchView searchView;
    Button cancel, send;
    private Address address;
    String Token;
    ArrayList<Float> point = new ArrayList<>();
    ArrayList<SuggestDescObjectResponse> suggestResult = new ArrayList<>();
    ArrayList<Province> provinceArrayList = new ArrayList<>();
    ArrayList<ServiceType> serviceTypeArrayList = new ArrayList<>();
    EditText name, addr, minCost, maxCost, service, province;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        searchView = (SearchView) findViewById(R.id.locationExplore);
        cancel = (Button) findViewById(R.id.btnCancelExplore);
        Token = getIntent().getStringExtra("token");
        getSuggestStopPoint();
        setUpProvince();
        setUpServiceTypeId();

        final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_explore);
        bottomNavigationView.setSelectedItemId(R.id.navigation_Explore);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_ListTour:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent = new Intent(ExploreActivity.this, MainActivity.class);
                        intent.putExtra("token", Token);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_History:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent2 = new Intent(ExploreActivity.this, HistoryActivity.class);
                        intent2.putExtra("token", Token);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_Explore:
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_Notifications:
                        //Toast.makeText(com.example.project.Activity.MainActivity.this, "notifications", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_Setting:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent3 = new Intent(ExploreActivity.this, ActivitySetting.class);
                        intent3.putExtra("token", Token);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                Log.e("query",location);
                for(int i=0;i<suggestResult.size();i++)
                {
                    if(location.toLowerCase().equals(suggestResult.get(i).getName().toLowerCase()))
                    {
                        LatLng latLng = new LatLng(suggestResult.get(i).getLat(),suggestResult.get(i).getLongtitude());
                        map.addMarker(new MarkerOptions().position(new LatLng(suggestResult.get(i).getLat(),suggestResult.get(i).getLongtitude())).title(location)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                        return false;
                    }
                }
                Toast.makeText(ExploreActivity.this, "Not find location", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ExploreActivity.this);
                builder.setMessage("Are you sure to exit?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create().show();
            }
        });

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapExplore);
        supportMapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(Marker marker) {

                int index = 0;
                final boolean[] success = {false};
                for (int i = 0; i < suggestResult.size(); i++) {
                    if (suggestResult.get(i).getLat() == marker.getPosition().latitude &&
                            suggestResult.get(i).getLongtitude() == marker.getPosition().longitude) {
                        index = i;
                        break;
                    }
                }
                Retrofit retrofit2 = RetrofitClient.getClient();
                APIService apiService2 = retrofit2.create(APIService.class);
                final int finalIndex = index;
                Log.e("index", String.valueOf(index) + "/" + suggestResult.get(index).getId());
                final int finalIndex1 = index;
                final int finalIndex2 = index;
                apiService2.detailService(Token, suggestResult.get(index).getId())
                        .enqueue(new Callback<DetailServiceResponse>() {
                            @Override
                            public void onResponse(Call<DetailServiceResponse> call, final Response<DetailServiceResponse> response) {
                                if (response.isSuccessful()) {
                                    final Dialog dialog = new Dialog(ExploreActivity.this);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.detail_service_explore);
                                    name = (EditText) dialog.findViewById(R.id.etStopPointExplore);
                                    addr = (EditText) dialog.findViewById(R.id.etAddressExplore);
                                    minCost = (EditText) dialog.findViewById(R.id.etMinCostExplore);
                                    maxCost = (EditText) dialog.findViewById(R.id.etMaxCostExplore);
                                    service = (EditText) dialog.findViewById(R.id.spinnerServiceTypeExplore);
                                    province = (EditText) dialog.findViewById(R.id.spinnerCityExplore);
                                    send = (Button) dialog.findViewById(R.id.btnFeedbackExplore);
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
                                    apiService4.getListCommentService(Token, suggestResult.get(finalIndex2).getId(), 1, 500)
                                            .enqueue(new Callback<ListCommentServiceResponse>() {
                                                @Override
                                                public void onResponse(Call<ListCommentServiceResponse> call, Response<ListCommentServiceResponse> response) {
                                                    if (response.isSuccessful()) {
                                                        ArrayList<ListCommentServiceResponse.CommentUser> list = response.body().getCommentUsers();
                                                        Log.e("size", String.valueOf(list.size()));
                                                        if (list != null) {
                                                            ListView listView = (ListView) dialog.findViewById(R.id.lvComment);
                                                            MyAdapterCommentService myAdapter = new
                                                                    MyAdapterCommentService(ExploreActivity.this, list);
                                                            listView.setAdapter(myAdapter);
                                                        }

                                                    } else {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                            Toast.makeText(ExploreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ListCommentServiceResponse> call, Throwable t) {
                                                    Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
                                                            Toast.makeText(ExploreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PointServiceResponse> call, Throwable t) {
                                                    Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    ///

                                    send.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (etFB.getText().toString().equals("")) {
                                                Toast.makeText(ExploreActivity.this, "No comment! Try again", Toast.LENGTH_SHORT).show();
                                            } else if (ratingBar.getRating() == 0) {
                                                Toast.makeText(ExploreActivity.this, "Please report point", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Retrofit retrofit3 = RetrofitClient.getClient();
                                                APIService apiService3 = retrofit3.create(APIService.class);

                                                apiService3.sendFeedback(Token, suggestResult.get(finalIndex1).getId(), etFB.getText().toString().trim(), (int) ratingBar.getRating())
                                                        .enqueue(new Callback<ResponseBody>() {
                                                            @Override
                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                if (response.isSuccessful()) {
                                                                    etFB.setText("");
                                                                    ratingBar.setRating(0F);
                                                                    // Toast.makeText(ExploreActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                                                    apiService4.getListCommentService(Token, suggestResult.get(finalIndex2).getId(), 1, 500)
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
                                                                                                    MyAdapterCommentService(ExploreActivity.this, list);
                                                                                            listView.setAdapter(myAdapter);
                                                                                        }

                                                                                    } else {
                                                                                        try {
                                                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                            Toast.makeText(ExploreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                        } catch (JSONException e) {
                                                                                            e.printStackTrace();
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<ListCommentServiceResponse> call, Throwable t) {
                                                                                    Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                } else {
                                                                    try {
                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                        Toast.makeText(ExploreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                } else {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                        Toast.makeText(ExploreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<DetailServiceResponse> call, Throwable t) {
                                Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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

    private void getSuggestStopPoint() {

        ArrayList<LocationStopPoint> locationStopPoints = new ArrayList<>();
        locationStopPoints.add(new LocationStopPoint(23.980056, 85.577677));
        locationStopPoints.add(new LocationStopPoint(23.588665, 163.065945));

        ArrayList<LocationStopPoint> locationStopPoints2 = new ArrayList<>();
        locationStopPoints2.add(new LocationStopPoint(-12.609835, 163.707522));
        locationStopPoints2.add(new LocationStopPoint(-13.928084,  75.526301));

        ArrayList<SuggestDescRequest.CoordList> coordLists = new ArrayList<>();
        coordLists.add(new SuggestDescRequest.CoordList(locationStopPoints));
        coordLists.add(new SuggestDescRequest.CoordList(locationStopPoints2));

        Retrofit retrofit = RetrofitClient.getClient();
        final APIService apiService = retrofit.create(APIService.class);
        apiService.getSuggestDesc(Token, new SuggestDescRequest(false, coordLists))
                .enqueue(new Callback<SuggestDescResponse>() {
                    @Override
                    public void onResponse(Call<SuggestDescResponse> call, Response<SuggestDescResponse> response) {
                        if (response.isSuccessful()) {
                            suggestResult = response.body().getList();
                            if (suggestResult != null) {
                                for (int i = 0; i < suggestResult.size(); i++) {
                                    LatLng newLocation = new LatLng(suggestResult.get(i).getLat(), suggestResult.get(i).getLongtitude());
                                    map.addMarker(new MarkerOptions().position(newLocation).title(suggestResult.get(i).getAddress()))
                                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12.0f));
                                }
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(ExploreActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuggestDescResponse> call, Throwable t) {
                        Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });



    }

}
