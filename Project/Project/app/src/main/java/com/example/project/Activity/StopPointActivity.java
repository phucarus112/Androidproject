package com.example.project.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.AddStopPointRequest;
import com.example.project.APIConnect.DetailServiceResponse;
import com.example.project.APIConnect.InfoTourResponse;
import com.example.project.APIConnect.ListCommentServiceResponse;
import com.example.project.APIConnect.LocationStopPoint;
import com.example.project.APIConnect.PointServiceResponse;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.StopPointObject;
import com.example.project.APIConnect.StopPointObjectEdit;
import com.example.project.APIConnect.SuggestDescObjectResponse;
import com.example.project.APIConnect.SuggestDescRequest;
import com.example.project.APIConnect.SuggestDescResponse;
import com.example.project.Adapter.MyAdapterCommentService;
import com.example.project.Adapter.MyAdapter_StopPointList;
import com.example.project.Adapter.MyAdapter_StopPointListEdit;
import com.example.project.Data.Province;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mapbox.mapboxsdk.style.expressions.Expression;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StopPointActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SearchView searchView;
    private String tag = "KQ";
    Dialog stopPointList;
    ImageButton imgBtnClose;
    ListView listView1;
    ArrayList<StopPointObject> stopPointObjectArrayList = new ArrayList<StopPointObject>();
    ArrayList<Province> provinceArrayList = new ArrayList<>();
    ArrayList<ServiceType> serviceTypeArrayList = new ArrayList<>();
    MyAdapter_StopPointList myAdapter_stopPointList;
    Button btnStopPointList;
    private Address address;
    public static final String SRCLAT = "SRCLAT";
    public static final String SRCLONG = "SRCLONG";
    public static final String ADDRESS = "ADDRESS";
    public static final String SRC = "SRC";
    public static final String TEMP = "TEMP";
    private String token;
    private String tourId;
    private int num, temp = 5;
    Dialog PopUpStopPoint;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLocation;
    ArrayList<SuggestDescObjectResponse> suggestResult;
    SharedPreferences sharedPreferences, sharedPreferences2;
    SharedPreferences.Editor editor;
    String tourStatus;
    Button cancel, addListSP, send;
    EditText name, addr, minCost, maxCost, service, province;
    private String dateArrival = "", dateLeave = "", timeArrival = "", timeLeave = "";
    private ImageView imDateArr, imDateLea;
    private TextView setDateArr, setDateLea, setArrTime, setLeaTime;
    private DatePickerDialog.OnDateSetListener mDateSetListenr, mDateSetListenr2;
    private long milisecondArr, milisecondLea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point);
        setUpProvince();
        setUpServiceTypeId();
        btnStopPointList = (Button) findViewById(R.id.btn_stopPointList);
        searchView = (SearchView) findViewById(R.id.location);
        cancel = (Button) findViewById(R.id.btnCancel);
        PopUpStopPoint = new Dialog(StopPointActivity.this);
        num = getIntent().getIntExtra(CreateTourActivity.SRC, 0);
        token = getIntent().getStringExtra("token");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP", MODE_PRIVATE);
                if (sharedPreferences4.getString("tim sau khi tao", "").equals("yes")) //tim kiem sau khi tao tour
                {
                    for (int i = 0; i < suggestResult.size(); i++) {
                        if (location.toLowerCase().equals(suggestResult.get(i).getName().toLowerCase())) {
                            LatLng latLng = new LatLng(suggestResult.get(i).getLat(), suggestResult.get(i).getLongtitude());
                            map.addMarker(new MarkerOptions().position(new LatLng(suggestResult.get(i).getLat(), suggestResult.get(i).getLongtitude())).title(location)
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                            return false;
                        }
                    }
                    Toast.makeText(StopPointActivity.this, "Not find location", Toast.LENGTH_SHORT).show();
                } else {
                    List<Address> addressList = null;
                    if (location != null || !location.equals("")) {
                        Geocoder geocoder = new Geocoder(StopPointActivity.this);
                        try {
                            addressList = geocoder.getFromLocationName(location, 1);
                            address = addressList.get(0);
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            map.clear();
//                   Toast.makeText(StopPointActivity.this, (int) a,Toast. LENGTH_SHORT).show();
                            //    if(map!=null) {
                            map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(location).snippet(address.getAddressLine(0))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                            //neu tim ra
                            final String latitude = String.valueOf(address.getLatitude());
                            final String longitude = String.valueOf(address.getLongitude());
                            final String addr = address.getAddressLine(0);
                        } catch (IOException e) {
                            Toast.makeText(StopPointActivity.this, "Not find location", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(StopPointActivity.this);
                builder.setMessage("If you choose YES, this tour won't be saved. Do you want it?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences = getSharedPreferences("saveSP", MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = preferences.edit();
                                editor1.putString("tim sau khi tao", "no");
                                editor1.putString("hoan thanh", "ok");
                                String token = preferences.getString("tourCancel", "");
                                String[] tokenList = token.split("/");
                                Log.e("size cancel", String.valueOf(tokenList.length));
                                editor1.putString("tourCancel", token + tourId + "/");
                                Log.e("day", token + tourId + "/");
                                editor1.commit();

                                //Intent intent = new Intent(StopPointActivity.this, MainActivity.class);
                                //intent.putExtra("token",token);
                                //startActivity(intent);
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

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences = getSharedPreferences("tourSP", MODE_PRIVATE);
        tourStatus = sharedPreferences.getString("isCompleted", "");
        if (tourStatus.equals("yes")) {
            tourId = getIntent().getStringExtra("tourId");
            Toast.makeText(this, tourId, Toast.LENGTH_SHORT).show();
            getSuggestStopPoint();
        }

        sharedPreferences2 = getSharedPreferences("saveSP", MODE_PRIVATE);
        if (sharedPreferences2.getString("have", "").equals("yes")) //neu da tao diem dung thanh cong
        {
            StopPointObject stopPointObject = new StopPointObject(
                    sharedPreferences2.getString("name", ""),
                    sharedPreferences2.getString("address", ""),
                    Integer.parseInt(sharedPreferences2.getString("province", "")),
                    Double.parseDouble(sharedPreferences2.getString("lat", "")),
                    Double.parseDouble(sharedPreferences2.getString("long", "")),
                    Long.parseLong(sharedPreferences2.getString("arrival", "")),
                    Long.parseLong(sharedPreferences2.getString("leave", "")),
                    Integer.parseInt(sharedPreferences2.getString("service", "")),
                    Float.parseFloat(sharedPreferences2.getString("minCost", "")),
                    Float.parseFloat(sharedPreferences2.getString("maxCost", "")),
                    null, null, null);
            //them diem dung nay vao tour lien
            Retrofit add = RetrofitClient.getClient();
            APIService apiAdd = add.create(APIService.class);
            final ArrayList<StopPointObject> spObj = new ArrayList<>();
            spObj.add(stopPointObject);
            apiAdd.addStopPoint(token, new AddStopPointRequest(tourId, spObj))
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(StopPointActivity.this, "Stop point is added to tour successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                    Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
            ///

            SharedPreferences.Editor edit = sharedPreferences2.edit();
            edit.putString("have", "no");
            edit.commit();

            stopPointObjectArrayList.add(stopPointObject);
        }
    }

    private void getSuggestStopPoint() {

        ArrayList<LocationStopPoint> locationStopPoints = new ArrayList<>();
        locationStopPoints.add(new LocationStopPoint(23.980056, 85.577677));
        locationStopPoints.add(new LocationStopPoint(23.588665, 163.065945));

        ArrayList<LocationStopPoint> locationStopPoints2 = new ArrayList<>();
        locationStopPoints2.add(new LocationStopPoint(-12.609835, 163.707522));
        locationStopPoints2.add(new LocationStopPoint(-13.928084, 75.526301));

        ArrayList<SuggestDescRequest.CoordList> coordLists = new ArrayList<>();
        coordLists.add(new SuggestDescRequest.CoordList(locationStopPoints));
        coordLists.add(new SuggestDescRequest.CoordList(locationStopPoints2));

        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getSuggestDesc(token, new SuggestDescRequest(false, coordLists))
                .enqueue(new Callback<SuggestDescResponse>() {
                    @Override
                    public void onResponse(Call<SuggestDescResponse> call, Response<SuggestDescResponse> response) {
                        if (response.isSuccessful()) {
                            suggestResult = response.body().getList();
                            if (suggestResult != null) {
                                Log.e("size", String.valueOf(suggestResult.size()));
                                for (int i = 0; i < suggestResult.size(); i++) {
                                    LatLng newLocation = new LatLng(suggestResult.get(i).getLat(), suggestResult.get(i).getLongtitude());
                                    map.addMarker(new MarkerOptions().position(newLocation).title(suggestResult.get(i).getAddress()))
                                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                }
                            }
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SuggestDescResponse> call, Throwable t) {
                        Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void byExtras(String srclat, String srclong, String address) {
        Intent intent = new Intent(StopPointActivity.this, CreateTourActivity.class);
        intent.putExtra("token", token);
        intent.putExtra(SRC, num);
        intent.putExtra(TEMP, temp);
        intent.putExtra(SRCLAT, srclat);
        intent.putExtra(SRCLONG, srclong);
        intent.putExtra(ADDRESS, address);
        startActivity(intent);
        finish();
    }

    public void byExtras2(String srclat, String srclong, String address) {
        Intent intent = new Intent(StopPointActivity.this, AddStopPoint.class);
        intent.putExtra("token", token);
        intent.putExtra(SRC, num);
        intent.putExtra(SRCLAT, srclat);
        intent.putExtra(SRCLONG, srclong);
        intent.putExtra(ADDRESS, address);
        startActivity(intent);
        SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP", MODE_PRIVATE);
        if (sharedPreferences4.getString("hoan thanh", "").equals("ok")) {
            finish();
        }
    }

    public void ShowUp() {
        Button close;
        PopUpStopPoint.setContentView(R.layout.activity_add_stop_point);
        PopUpStopPoint.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //addLocationPermission();

        map.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
            @Override
            public void onInfoWindowLongClick(final Marker marker) {

                SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP", MODE_PRIVATE);
                if (sharedPreferences4.getString("tim sau khi tao", "").equals("yes")) //tim kiem sau khi tao tour
                {

                    //hien thong bao
                    final AlertDialog.Builder alert = new AlertDialog.Builder(StopPointActivity.this);
                    alert.setMessage("Do you want to add this stop point to your tour?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    final Dialog mydialog = new Dialog(StopPointActivity.this);
                                    mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    mydialog.setContentView(R.layout.dialog_add_time);
                                    Button btn;
                                    imDateArr = (ImageView) mydialog.findViewById(R.id.IMcalendarArrAddSP);
                                    imDateLea = (ImageView) mydialog.findViewById(R.id.IMcalendarLeaveAddSP);
                                    setDateArr = (TextView) mydialog.findViewById(R.id.tvDateArriveAddSP);
                                    setDateLea = (TextView) mydialog.findViewById(R.id.tvDateLeaveAddSP);
                                    setArrTime = (TextView) mydialog.findViewById(R.id.tvTimeArriveAddSP);
                                    setLeaTime = (TextView) mydialog.findViewById(R.id.tvTimeLeaveAddSP);
                                    btn = (Button) mydialog.findViewById(R.id.btnAddSPAddSP);
                                    mydialog.show();
                                    setArrTime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Calendar calendar = Calendar.getInstance();
                                            int gio = calendar.get(Calendar.HOUR_OF_DAY);
                                            int phut = calendar.get(Calendar.MINUTE);

                                            TimePickerDialog timePickerDialog = new TimePickerDialog(StopPointActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    setArrTime.setText(hourOfDay + " : " + minute);
                                                    if (hourOfDay < 10)
                                                        timeArrival += "0" + String.valueOf(hourOfDay);
                                                    else timeArrival = String.valueOf(hourOfDay);
                                                    if (minute < 10)
                                                        timeArrival += "0" + String.valueOf(minute);
                                                    else timeArrival += String.valueOf(minute);
                                                    timeArrival += "00";
                                                }
                                            }, gio, phut, true);
                                            timePickerDialog.show();
                                        }
                                    });
                                    setLeaTime.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            final Calendar calendar = Calendar.getInstance();
                                            int gio = calendar.get(Calendar.HOUR_OF_DAY);
                                            int phut = calendar.get(Calendar.MINUTE);

                                            TimePickerDialog timePickerDialog = new TimePickerDialog(StopPointActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                                    setLeaTime.setText(hourOfDay + " : " + minute);
                                                    if (hourOfDay < 10)
                                                        timeLeave = "0" + String.valueOf(hourOfDay);
                                                    else timeLeave = String.valueOf(hourOfDay);
                                                    if (minute < 10)
                                                        timeLeave += "0" + String.valueOf(minute);
                                                    else timeLeave += String.valueOf(minute);
                                                    timeLeave += "00";
                                                }
                                            }, gio, phut, true);
                                            timePickerDialog.show();
                                        }
                                    });
                                    imDateArr.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Calendar cal = Calendar.getInstance();
                                            int year = cal.get(Calendar.YEAR);
                                            int month = cal.get(Calendar.MONTH);
                                            int day = cal.get(Calendar.DAY_OF_MONTH);
                                            milisecondArr = cal.getTimeInMillis();
                                            DatePickerDialog dialog = new DatePickerDialog(
                                                    StopPointActivity.this,
                                                    android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr, year, month, day);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.show();
                                        }
                                    });
                                    imDateLea.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Calendar cal = Calendar.getInstance();
                                            int year = cal.get(Calendar.YEAR);
                                            int month = cal.get(Calendar.MONTH);
                                            int day = cal.get(Calendar.DAY_OF_MONTH);
                                            milisecondLea = cal.getTimeInMillis();
                                            DatePickerDialog dialog = new DatePickerDialog(
                                                    StopPointActivity.this,
                                                    android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListenr2, year, month, day);
                                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                            dialog.show();
                                        }
                                    });
                                    mDateSetListenr = new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            month = month + 1;
                                            String date = dayOfMonth + "/" + month + "/" + year;
                                            setDateArr.setText(date);

                                            dateArrival += year;
                                            if (month < 10)
                                                dateArrival += "0" + String.valueOf(month);
                                            else dateArrival += String.valueOf(month);
                                            if (dayOfMonth < 10)
                                                dateArrival += "0" + String.valueOf(dayOfMonth);
                                            else dateArrival += String.valueOf(dayOfMonth);
                                        }
                                    };
                                    mDateSetListenr2 = new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                            month = month + 1;
                                            String date = dayOfMonth + "/" + month + "/" + year;
                                            setDateLea.setText(date);

                                            dateLeave += year;
                                            if (month < 10)
                                                dateLeave += "0" + String.valueOf(month);
                                            else dateLeave += String.valueOf(month);
                                            if (dayOfMonth < 10)
                                                dateLeave += "0" + String.valueOf(dayOfMonth);
                                            else dateLeave += String.valueOf(dayOfMonth);
                                        }
                                    };
                                    btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mydialog.cancel();
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
                                            apiService2.detailService(token, suggestResult.get(index).getId())
                                                    .enqueue(new Callback<DetailServiceResponse>() {
                                                        @Override
                                                        public void onResponse(Call<DetailServiceResponse> call, Response<DetailServiceResponse> response) {
                                                            if (response.isSuccessful()) {
                                                                final Dialog dialog = new Dialog(StopPointActivity.this);
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
                                                                final TextView pointAvg = (TextView) dialog.findViewById(R.id.pointFB);
                                                                final TextView count = (TextView) dialog.findViewById(R.id.tongDanhGiaService);
                                                                final RatingBar ratingBar1 = (RatingBar) dialog.findViewById(R.id.ratingService);
                                                                final ProgressBar p1 = (ProgressBar) dialog.findViewById(R.id.progressBar1Service);
                                                                final ProgressBar p2 = (ProgressBar) dialog.findViewById(R.id.progressBar2Service);
                                                                final ProgressBar p3 = (ProgressBar) dialog.findViewById(R.id.progressBar3Service);
                                                                final ProgressBar p4 = (ProgressBar) dialog.findViewById(R.id.progressBar4Service);
                                                                final ProgressBar p5 = (ProgressBar) dialog.findViewById(R.id.progressBar5Service);

                                                                //lay ds comment cua service
                                                                Retrofit retrofit4 = RetrofitClient.getClient();
                                                                final APIService apiService4 = retrofit4.create(APIService.class);
                                                                apiService4.getListCommentService(token, suggestResult.get(finalIndex2).getId(), 1, 500)
                                                                        .enqueue(new Callback<ListCommentServiceResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<ListCommentServiceResponse> call, Response<ListCommentServiceResponse> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    ArrayList<ListCommentServiceResponse.CommentUser> list = response.body().getCommentUsers();
                                                                                    Log.e("size", String.valueOf(list.size()));
                                                                                    if (list != null) {
                                                                                        ListView listView = (ListView) dialog.findViewById(R.id.lvComment);
                                                                                        MyAdapterCommentService myAdapter = new
                                                                                                MyAdapterCommentService(StopPointActivity.this, list);
                                                                                        listView.setAdapter(myAdapter);
                                                                                    }

                                                                                } else {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                        Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<ListCommentServiceResponse> call, Throwable t) {
                                                                                Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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

                                                                ///
                                                                StopPointObject stopPointObject = new StopPointObject(name.getText().toString().trim(),
                                                                        addr.getText().toString().trim(), response.body().getProvinceId(),
                                                                        response.body().getLat(), response.body().getLongtitude(), Long.parseLong(dateArrival + timeArrival),
                                                                        Long.parseLong(dateLeave + timeLeave), response.body().getServiceTypeId(),
                                                                        Float.parseFloat(minCost.getText().toString().trim()), Float.parseFloat(maxCost.getText().toString().trim()),
                                                                        null, null, String.valueOf(response.body().getId()));
                                                                stopPointObjectArrayList.add(stopPointObject);
                                                                //them diem dung nay vao tour lien
                                                                Retrofit add = RetrofitClient.getClient();
                                                                APIService apiAdd = add.create(APIService.class);
                                                                final ArrayList<StopPointObject> spObj = new ArrayList<>();
                                                                spObj.add(stopPointObject);
                                                                apiAdd.addStopPoint(token, new AddStopPointRequest(tourId, spObj))
                                                                        .enqueue(new Callback<ResponseBody>() {
                                                                            @Override
                                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Toast.makeText(StopPointActivity.this, "Stop point is added to tour successfully", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                        Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                ///

                                                                //set gia tri cho point
                                                                Retrofit re = RetrofitClient.getClient();
                                                                final APIService api = re.create(APIService.class);
                                                                api.getPointFeedbackService(token, response.body().getId())
                                                                        .enqueue(new Callback<PointServiceResponse>() {
                                                                            @Override
                                                                            public void onResponse(Call<PointServiceResponse> call, Response<PointServiceResponse> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    Log.e("size", String.valueOf(response.body().getPointStats().size()));
                                                                                    int countTurn = 0;
                                                                                    float avg = 0F;
                                                                                    for (int z = 0; z < response.body().getPointStats().size(); z++) {
                                                                                        countTurn += response.body().getPointStats().get(z).getTotal();

                                                                                        avg += response.body().getPointStats().get(z).getTotal()
                                                                                                * response.body().getPointStats().get(z).getPoint();
                                                                                    }
                                                                                    count.setText(String.valueOf(countTurn));
                                                                                    if(countTurn!=0) pointAvg.setText(String.valueOf(avg / countTurn) + "/5");
                                                                                    ratingBar1.setRating(avg / countTurn);
                                                                                    if (countTurn > 0) {
                                                                                        p1.setProgress(100 * response.body().getPointStats().get(0).getTotal() / countTurn);
                                                                                        p2.setProgress(100 * response.body().getPointStats().get(1).getTotal() / countTurn);
                                                                                        p3.setProgress(100 * response.body().getPointStats().get(2).getTotal() / countTurn);
                                                                                        p4.setProgress(100 * response.body().getPointStats().get(3).getTotal() / countTurn);
                                                                                        p5.setProgress(100 * response.body().getPointStats().get(4).getTotal() / countTurn);
                                                                                    }
                                                                                } else {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                        Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<PointServiceResponse> call, Throwable t) {
                                                                                Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                ///

                                                                send.setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        if (etFB.getText().toString().equals("")) {
                                                                            Toast.makeText(StopPointActivity.this, "No comment! Try again", Toast.LENGTH_SHORT).show();
                                                                        } else if (ratingBar.getRating() == 0) {
                                                                            Toast.makeText(StopPointActivity.this, "Please report point", Toast.LENGTH_SHORT).show();
                                                                        } else {
                                                                            Retrofit retrofit3 = RetrofitClient.getClient();
                                                                            APIService apiService3 = retrofit3.create(APIService.class);
                                                                            apiService3.sendFeedback(token, suggestResult.get(finalIndex1).getId(), etFB.getText().toString().trim(), (int) ratingBar.getRating())
                                                                                    .enqueue(new Callback<ResponseBody>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                            if (response.isSuccessful()) {
                                                                                                // Toast.makeText(ExploreActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                                                                                apiService4.getListCommentService(token, suggestResult.get(finalIndex2).getId(), 1, 500)
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
                                                                                                                                MyAdapterCommentService(StopPointActivity.this, list);
                                                                                                                        listView.setAdapter(myAdapter);
                                                                                                                    }

                                                                                                                } else {
                                                                                                                    try {
                                                                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                                                        Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                                                    } catch (JSONException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                }
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call<ListCommentServiceResponse> call, Throwable t) {
                                                                                                                Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        });
                                                                                            } else {
                                                                                                try {
                                                                                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                                    Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                } catch (IOException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                            Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                });
                                                            } else {
                                                                try {
                                                                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                    Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<DetailServiceResponse> call, Throwable t) {
                                                            Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(StopPointActivity.this);
                    builder.setMessage("Are you ok with address ' " + marker.getTitle() + " ' ?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    byExtras(String.valueOf(marker.getPosition().latitude),
                                            String.valueOf(marker.getPosition().longitude), address.getAddressLine(0));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                }
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (tourStatus.equals("yes")) //neu da tao xong tour moi vao duoc
                {
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(StopPointActivity.this, Locale.getDefault());
                    try {
                        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0);
                        byExtras2(String.valueOf(latLng.latitude), String.valueOf(latLng.longitude), address);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });



        btnStopPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP", MODE_PRIVATE);
                if (sharedPreferences4.getString("tim sau khi tao", "").equals("yes"))
                {
                    //lay ds cac diem dung
                    Retrofit re = RetrofitClient.getClient();
                    final APIService api = re.create(APIService.class);
                    api.getInfoTour(token, Integer.parseInt(tourId))
                            .enqueue(new Callback<InfoTourResponse>() {
                                @Override
                                public void onResponse(Call<InfoTourResponse> call, Response<InfoTourResponse> response) {
                                    if (response.isSuccessful()) {
                                        final Dialog dialog1 = new Dialog(StopPointActivity.this);
                                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog1.setContentView(R.layout.stop_point_list_edit);
                                        final ArrayList<StopPointObjectEdit> spObj = (ArrayList<StopPointObjectEdit>) response.body().getStopPoints();
                                        ListView listView1 = (ListView) dialog1.findViewById(R.id.lvStopPointListEdit);
                                        MyAdapter_StopPointListEdit myAdapter_stopPointList = new
                                                MyAdapter_StopPointListEdit(StopPointActivity.this, spObj);
                                        listView1.setAdapter(myAdapter_stopPointList);
                                        final Button exit = (Button) dialog1.findViewById(R.id.tvExitPrevious);
                                        exit.setFocusable(true);
                                        exit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(StopPointActivity.this);
                                                builder.setMessage("Do you want to return previous screen?")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                                exit.setText("");
                                                                finish();
                                                            }
                                                        })
                                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                            public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                                            }
                                                        });
                                                final AlertDialog alert = builder.create();
                                                alert.show();
                                            }
                                        });

                                        //set su kien khi nhan vao
                                        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Intent intent = new Intent(StopPointActivity.this, EditStopPoint.class);
                                                intent.putExtra("token", token);

                                                intent.putExtra("tourIdSP", String.valueOf(tourId));
                                                intent.putExtra("latSP", String.valueOf(spObj.get(position).getLat()));
                                                intent.putExtra("longSP", String.valueOf(spObj.get(position).getLongtitude()));
                                                intent.putExtra("idSP", String.valueOf(spObj.get(position).getId()));
                                                intent.putExtra("nameSP", spObj.get(position).getName());
                                                intent.putExtra("addressSP", spObj.get(position).getAddress());
                                                intent.putExtra("serviceId", String.valueOf(spObj.get(position).getServiceId()));
                                                intent.putExtra("serviceSP", String.valueOf(spObj.get(position).getServiceTypeId()));
                                                intent.putExtra("provinceSP", String.valueOf(spObj.get(position).getProvinceId()));
                                                intent.putExtra("minCostSP", String.valueOf(spObj.get(position).getMinCost()));
                                                intent.putExtra("maxCostSP", String.valueOf(spObj.get(position).getMaxCost()));
                                                intent.putExtra("arrivalSP", String.valueOf(spObj.get(position).getArrivalAt()));
                                                intent.putExtra("leaveSP", String.valueOf(spObj.get(position).getLeaveAt()));
                                                intent.putExtra("index", String.valueOf(position));
                                                startActivity(intent);
                                                dialog1.cancel();
                                            }
                                        });

                                        //set su kien khi long click
                                        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                            @Override
                                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                                                final AlertDialog.Builder builder = new AlertDialog.Builder(StopPointActivity.this);
                                                builder.setMessage("Are you sure to remove stop point?")
                                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(final DialogInterface dialog, int which) {
                                                                Retrofit retrofit2 = RetrofitClient.getClient();
                                                                final APIService apiService2 = retrofit2.create(APIService.class);
                                                                apiService2.removeStopPoint(token, String.valueOf(spObj.get(position).getId()))
                                                                        .enqueue(new Callback<ResponseBody>() {
                                                                            @Override
                                                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                                                if (response.isSuccessful()) {
                                                                                    dialog1.cancel();
                                                                                    Toast.makeText(StopPointActivity.this, "Delete stop point successfully", Toast.LENGTH_SHORT).show();
                                                                                } else {
                                                                                    try {
                                                                                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                                                        Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                                    } catch (JSONException e) {
                                                                                        e.printStackTrace();
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                                                Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(StopPointActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<InfoTourResponse> call, Throwable t) {
                                    Toast.makeText(StopPointActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else Toast.makeText(StopPointActivity.this, "Cannot open", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            buildAlertMessageNoGps();
                        } else {
                            if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(),
                                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                            map.clear();
                            map.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
                            map.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                            map.animateCamera(CameraUpdateFactory.zoomTo(17.0F));
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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
}