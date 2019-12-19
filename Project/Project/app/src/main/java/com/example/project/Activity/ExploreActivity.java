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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.DetailServiceResponse;
import com.example.project.APIConnect.InfoTourResponse;
import com.example.project.APIConnect.LocationStopPoint;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.SuggestDescObjectResponse;
import com.example.project.APIConnect.SuggestDescRequest;
import com.example.project.APIConnect.SuggestDescResponse;
import com.example.project.Data.ServiceId_StopPoint;
import com.example.project.Data.ServiceType;
import com.example.project.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ExploreActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    SearchView searchView;
    Button cancel;
    private Address address;
    String Token;
    ArrayList<SuggestDescObjectResponse> suggestResult;
    ArrayList<ServiceType> serviceTypeArrayList= new ArrayList<>();
    ArrayList<ServiceId_StopPoint> serviceId_stopPointArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        searchView = (SearchView) findViewById(R.id.locationExplore);
        cancel=(Button) findViewById(R.id.btnCancelExplore);
        Token = getIntent().getStringExtra("token");
        getSuggestStopPoint();
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
                        intent.putExtra("token",Token);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_History:
                        Token = getIntent().getStringExtra("token");
                        overridePendingTransition(0, 0);
                        Intent intent2 = new Intent(ExploreActivity.this,HistoryActivity.class);
                        intent2.putExtra("token",Token);
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
                        // Toast.makeText(com.example.project.Activity.MainActivity.this, "setting", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(ExploreActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                        address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                        map.clear();
                        map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(location).snippet(address.getAddressLine(0))
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                        //neu tim ra
                        final String latitude = String.valueOf(address.getLatitude());
                        final String longitude = String.valueOf(address.getLongitude());
                        final String addr= address.getAddressLine(0);
                    } catch (IOException e) {
                        Toast.makeText(ExploreActivity.this, "Not find location", Toast.LENGTH_SHORT).show();
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
        supportMapFragment.getMapAsync( this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                Dialog dialog = new Dialog(ExploreActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.detail_service_explore);

                dialog.show();

            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void setUpServiceTypeId() {

        serviceTypeArrayList.add(new ServiceType(1,"Restaurant"));
        serviceTypeArrayList.add(new ServiceType(2,"Hotel"));
        serviceTypeArrayList.add(new ServiceType(3,"Rest Station"));
        serviceTypeArrayList.add(new ServiceType(4,"Other"));

    }

    private void getSuggestStopPoint() {

        ArrayList<LocationStopPoint> locationStopPoints = new ArrayList<>();
        locationStopPoints.add(new LocationStopPoint(23.391185,105.323524));
        locationStopPoints.add(new LocationStopPoint(8.30,104.40));

        ArrayList<SuggestDescRequest.CoordList> coordLists = new ArrayList<>();
        coordLists.add(new SuggestDescRequest.CoordList(locationStopPoints));

        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getSuggestDesc(Token,new SuggestDescRequest(false,coordLists))
                .enqueue(new Callback<SuggestDescResponse>() {
                    @Override
                    public void onResponse(Call<SuggestDescResponse> call, Response<SuggestDescResponse> response) {
                        if(response.isSuccessful())
                        {
                            suggestResult = response.body().getList();
                            if(suggestResult != null)
                            {
                                Log.e("size", String.valueOf(suggestResult.size()));

                                for(int i=0;i<suggestResult.size();i++)
                                {


                                    LatLng newLocation = new LatLng(suggestResult.get(i).getLat(), suggestResult.get(i).getLongtitude());
                                    map.addMarker(new MarkerOptions().position(newLocation).title(suggestResult.get(i).getAddress()))
                                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 8.0f));
                                }
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
                    public void onFailure(Call<SuggestDescResponse> call, Throwable t) {
                        Toast.makeText(ExploreActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
