package com.example.project;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class StopPointActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap map;
    SearchView searchView;
    SupportMapFragment mapFragment;
    private  String tag="KQ";
    EditText tes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point);
        searchView=(SearchView) findViewById(R.id.location);
        mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        tes=(EditText)findViewById(R.id.test);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location= searchView.getQuery().toString();
                List<Address> addressList=null;
                if(location !=null || !location.equals("")){
                    Geocoder geocoder= new Geocoder(StopPointActivity.this);
                    try {
                        addressList= geocoder.getFromLocationName(location,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);

                  LatLng latLng= new LatLng(+41.5020952,-81.6789717);

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
        });
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
