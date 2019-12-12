package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.project.CreateTourActivity.*;

public class StopPointActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SearchView searchView;
    SupportMapFragment mapFragment;
    private String tag = "KQ";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLocation;
    private Button cancel;
    Dialog stopPointList;
    ImageButton imgBtnClose;
    ListView listView1;
    ArrayList<StopPointObject> stopPointObjectArrayList = new ArrayList<StopPointObject>();
    MyAdapter_StopPointList myAdapter_stopPointList;
    Button btnStopPointList;
    private Address address;
    public static  final String SRCLAT= "SRCLAT";
    public static final String SRCLONG="SRCLONG";
    public static final String ADDRESS="ADDRESS";
    public static  final String SRC= "SRC";
    public static  final String TEMP= "TEMP";
    private String token;
    private int num,temp=5;
    Dialog PopUpStopPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point);
        btnStopPointList  = (Button)findViewById(R.id.btn_stopPointList);
        searchView = (SearchView) findViewById(R.id.location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        cancel=(Button) findViewById(R.id.btnCancel);
        PopUpStopPoint = new Dialog(StopPointActivity.this);
        num= getIntent().getIntExtra(CreateTourActivity.SRC,0);

        token= getIntent().getStringExtra("token");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(StopPointActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                     address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    map.clear();
//                   Toast.makeText(StopPointActivity.this, (int) a,Toast. LENGTH_SHORT).show();
                    //    if(map!=null) {
                    map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(location).snippet(address.getAddressLine(0))

                            .icon(BitmapDescriptorFactory.defaultMarker(

                                    BitmapDescriptorFactory.HUE_GREEN)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }

                // }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mapFragment.getMapAsync(this);
      cancel.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(num==1 || num==2) {
                  Intent intent = new Intent(StopPointActivity.this, CreateTourActivity.class);
                  temp=4;
                  intent.putExtra(TEMP,temp);
                  intent.putExtra("token", token);
                  intent.putExtra(SRC, num);
                  startActivity(intent);
              }
              else if(num==3){
                  Toast.makeText(StopPointActivity.this, "Can't back last activity", Toast.LENGTH_SHORT).show();
              }
          }
       });
    }
    public void byExtras(String srclat,String srclong,String address){
        Intent intent= new Intent(StopPointActivity.this,CreateTourActivity.class);
        intent.putExtra("token",token);
        intent.putExtra(SRC,num);
        intent.putExtra(TEMP,temp);
        intent.putExtra(SRCLAT,srclat);
        intent.putExtra(SRCLONG,srclong);
        intent.putExtra(ADDRESS,address);
        startActivity(intent);
    }
    public void byExtras2(String srclat,String srclong,String address){
        Intent intent= new Intent(StopPointActivity.this,AddStopPoint.class);
        intent.putExtra("token",token);
        intent.putExtra(SRC,num);
        intent.putExtra(SRCLAT,srclat);
        intent.putExtra(SRCLONG,srclong);
        intent.putExtra(ADDRESS,address);
        startActivity(intent);
    }
    public void ShowUp(){
        Button close;
        PopUpStopPoint.setContentView(R.layout.activity_add_stop_point);

        PopUpStopPoint.show();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String latitude = String.valueOf(address.getLatitude());
                String longitude = String.valueOf(address.getLongitude());
                String addr= address.getAddressLine(0);
                if(num!=3) {

                    byExtras(latitude, longitude,addr);
                }
                else
                if (num == 3) {
                    // ShowUp();
                    byExtras2(latitude, longitude,addr);
                }
            }
        });

        btnStopPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPointList = new Dialog(StopPointActivity.this);
                stopPointList.setContentView(R.layout.stop_point_list);
                imgBtnClose = (ImageButton)stopPointList.findViewById(R.id.imgBtnClose);
                listView1 = (ListView) stopPointList.findViewById(R.id.lvStopPointList);
                myAdapter_stopPointList = new MyAdapter_StopPointList(StopPointActivity.this,stopPointObjectArrayList);
                listView1.setAdapter(myAdapter_stopPointList);
                stopPointList.show();

                imgBtnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopPointList.dismiss();
                    }
                });
            }
        });
    }

    private void addLocationPermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        if(ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        userLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        map.clear();
                        map.addMarker(new MarkerOptions().position(userLocation).title("I'm here"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                        map.animateCamera(CameraUpdateFactory.zoomTo(19.0F));
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
}
