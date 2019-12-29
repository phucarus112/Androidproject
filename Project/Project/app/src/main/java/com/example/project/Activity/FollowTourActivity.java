package com.example.project.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.InfoTourResponse;
import com.example.project.APIConnect.MemberObject;
import com.example.project.APIConnect.MyMember;
import com.example.project.APIConnect.PosMemberResponse;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.UserInfoResponse;
import com.example.project.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// classes needed to initialize map
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.util.Log;

// classes needed to launch navigation UI
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;

import org.json.JSONException;
import org.json.JSONObject;

public class FollowTourActivity extends AppCompatActivity implements OnMapReadyCallback {

     GoogleMap map;
    int tourId;
    int userIdHost;
    int userId;
    ArrayList<MyMember> memberObjectArrayList = new ArrayList<>();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLocation;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_tour);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        tourId = Integer.parseInt(getIntent().getStringExtra("tourId"));

        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getUsetInfo(getIntent().getStringExtra("token"))
                .enqueue(new Callback<UserInfoResponse>() {
                    @Override
                    public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                        if(response.isSuccessful())
                        {
                            userId = response.body().getId();
                        }
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(FollowTourActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserInfoResponse> call, Throwable throwable) {
                        Toast.makeText(FollowTourActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });

        getHost();

        btn = (Button) findViewById(R.id.btnSendNoti);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(FollowTourActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_noti);
                TextView text = (TextView)dialog.findViewById(R.id.text);
                text.setPaintFlags(text.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                TextView road = (TextView)dialog.findViewById(R.id.road);
                road.setPaintFlags(road.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
                dialog.show();
                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog1 = new Dialog(FollowTourActivity.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialog_text_noti);
                        final EditText etText = (EditText) dialog1.findViewById(R.id.etText);
                        Button btnText = (Button) dialog1.findViewById(R.id.btnText);
                        btnText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(etText.getText().toString().trim().equals(""))
                                {
                                    Toast.makeText(FollowTourActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //api
                                    Retrofit retrofit = RetrofitClient.getClient();
                                    APIService apiService = retrofit.create(APIService.class);
                                    apiService.sendTextNoti(getIntent().getStringExtra("token"),tourId,userId,
                                            etText.getText().toString().trim())
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if(response.isSuccessful())
                                                    {
                                                        Toast.makeText(FollowTourActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                                        dialog1.cancel();
                                                        dialog.cancel();
                                                    }
                                                    else
                                                    {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                            Toast.makeText(FollowTourActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(FollowTourActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                        dialog1.show();
                    }
                });
                road.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog1 = new Dialog(FollowTourActivity.this);
                        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog1.setContentView(R.layout.dialog_speed);
                        final EditText etRoad = (EditText) dialog1.findViewById(R.id.etRoad);
                        Button btnRoad = (Button) dialog1.findViewById(R.id.btnRoad);
                        final CheckBox id40 = (CheckBox)dialog1.findViewById(R.id.id40);
                        final CheckBox id60 = (CheckBox)dialog1.findViewById(R.id.id60);
                        final CheckBox id80 = (CheckBox)dialog1.findViewById(R.id.id80);
                        btnRoad.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(etRoad.toString().trim().equals("") )
                                {
                                    Toast.makeText(FollowTourActivity.this, "Please enter text", Toast.LENGTH_SHORT).show();
                                }
                                else if((id40.isChecked()&& id60.isChecked() && id80.isChecked())
                                    || (id40.isChecked() && id60.isChecked()) || (id60.isChecked() && id80.isChecked())
                                || (id80.isChecked()&& id40.isChecked()))
                                {
                                    Toast.makeText(FollowTourActivity.this, "Only one speed type is chosen", Toast.LENGTH_SHORT).show();
                                }
                                else if(id40.isChecked()==false && id60.isChecked()==false && id80.isChecked()==false)
                                {
                                    Toast.makeText(FollowTourActivity.this, "You must choose one speed type", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //api
                                    int speed;
                                    if(id40.isChecked()) speed=  40;
                                    else if(id60.isChecked())speed = 60;
                                    else speed =80;

                                    Retrofit retrofit = RetrofitClient.getClient();
                                    APIService apiService = retrofit.create(APIService.class);
                                    apiService.sendSpeedNoti(getIntent().getStringExtra("token"),userLocation.latitude,
                                            userLocation.longitude,tourId,userId,3,speed,
                                            etRoad.getText().toString().trim())
                                            .enqueue(new Callback<ResponseBody>() {
                                                @Override
                                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                    if(response.isSuccessful())
                                                    {
                                                        Toast.makeText(FollowTourActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                                        dialog1.cancel();
                                                        dialog.cancel();
                                                    }
                                                    else
                                                    {
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                                            Toast.makeText(FollowTourActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                    Toast.makeText(FollowTourActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            }
                        });
                        dialog1.show();
                    }
                });
            }
        });
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFollow);
        supportMapFragment.getMapAsync(this);
    }

    private void getHost() {
        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getInfoTour(getIntent().getStringExtra("token"),tourId)
                .enqueue(new Callback<InfoTourResponse>() {
                    @Override
                    public void onResponse(Call<InfoTourResponse> call, Response<InfoTourResponse> response) {
                        if(response.isSuccessful())
                        {
                            memberObjectArrayList= (ArrayList<MyMember>) response.body().getMembers();
                           // Toast.makeText(FollowTourActivity.this, String.valueOf(userIdHost), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(FollowTourActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<InfoTourResponse> call, Throwable t) {
                        Toast.makeText(FollowTourActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getPositionMember(double lat,double longtitude) {
        //get member position
        Retrofit retrofit1 = RetrofitClient.getClient();
        APIService apiService1 = retrofit1.create(APIService.class);
        apiService1.getPosMember(getIntent().getStringExtra("token"),userId,tourId,lat,longtitude)
                .enqueue(new Callback<ArrayList<PosMemberResponse>>() {
                    @Override
                    public void onResponse(Call<ArrayList<PosMemberResponse>> call, Response<ArrayList<PosMemberResponse>> response) {
                        if (response.isSuccessful())
                        {
                            //Toast.makeText(FollowTourActivity.this, String.valueOf(response.body().size()), Toast.LENGTH_SHORT).show();
                                for(int i=0;i<response.body().size();i++) {
                                    LatLng latLng = new LatLng(response.body().get(i).getLat(), response.body().get(i).getLongtitude());
                                    for(int j=0;j<memberObjectArrayList.size();j++)
                                    {
                                        if(response.body().get(i).getId() == memberObjectArrayList.get(j).getId())
                                        {
                                            map.addMarker(new MarkerOptions().position(latLng).title(memberObjectArrayList.get(j)
                                                    .getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                                        }
                                    }
                            }
                        }
                        else
                        {
                            try {
                                JSONObject jsonObject = new JSONObject(response.errorBody().string());
                                Toast.makeText(FollowTourActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<PosMemberResponse>> call, Throwable t) {
                        Toast.makeText(FollowTourActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                final Handler handler = new Handler();

                final Runnable r = new Runnable() {
                    public void run() {
                        userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        map.clear();
                        getPositionMember(userLocation.latitude, userLocation.longitude);
                        map.animateCamera(CameraUpdateFactory.newLatLng(userLocation));
                        handler.post(this);
                        handler.postDelayed(this, 10000);
                    }
                };
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

        addLocationPermission();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
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
                            map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 16.0f));
                            getPositionMember(userLocation.latitude,userLocation.longitude);
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
}
