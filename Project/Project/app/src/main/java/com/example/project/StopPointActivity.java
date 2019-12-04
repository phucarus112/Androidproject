package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class StopPointActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SearchView searchView;
    SupportMapFragment mapFragment;
    private String tag = "KQ";
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLocation;
    Dialog stopPointList;
    ImageButton imgBtnClose;
    ListView listView1;
    ArrayList<StopPointObject> stopPointObjectArrayList = new ArrayList<StopPointObject>();
    MyAdapter_StopPointList myAdapter_stopPointList;
    Button btnStopPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point);
        btnStopPointList  = (Button)findViewById(R.id.btn_stopPointList);
        searchView = (SearchView) findViewById(R.id.location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(+41.5020952, -81.6789717);

//                   Toast.makeText(StopPointActivity.this, (int) a,Toast. LENGTH_SHORT).show();
                    //    if(map!=null) {
                    map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(location).snippet("San Jose, CR")

                            .icon(BitmapDescriptorFactory.defaultMarker(

                                    BitmapDescriptorFactory.HUE_GREEN)));
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
                }

                // }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });*/
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                map.clear();
                map.addMarker(new MarkerOptions().position(userLocation).title("I'm here"));
                map.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
                map.animateCamera(CameraUpdateFactory.zoomTo(19.0F));
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

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                        //click vào map ở 1 điểm bky
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
