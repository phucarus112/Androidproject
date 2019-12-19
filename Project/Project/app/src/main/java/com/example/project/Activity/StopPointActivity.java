package com.example.project.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.project.APIConnect.APIService;
import com.example.project.APIConnect.AddStopPointRequest;
import com.example.project.APIConnect.LocationStopPoint;
import com.example.project.APIConnect.ResponseBody;
import com.example.project.APIConnect.RetrofitClient;
import com.example.project.APIConnect.StopPointObject;
import com.example.project.APIConnect.SuggestDescObjectResponse;
import com.example.project.APIConnect.SuggestDescRequest;
import com.example.project.APIConnect.SuggestDescResponse;
import com.example.project.Adapter.MyAdapter_StopPointList;
import com.example.project.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    MyAdapter_StopPointList myAdapter_stopPointList;
    Button btnStopPointList;
    private Address address;
    public static  final String SRCLAT= "SRCLAT";
    public static final String SRCLONG="SRCLONG";
    public static final String ADDRESS="ADDRESS";
    public static  final String SRC= "SRC";
    public static  final String TEMP= "TEMP";
    private String token;
    private String tourId;
    private int num,temp=5;
    Dialog PopUpStopPoint;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng userLocation;
    ArrayList<SuggestDescObjectResponse> suggestResult;
    SharedPreferences sharedPreferences,sharedPreferences2;
    SharedPreferences.Editor editor;
    String tourStatus;
    Button cancel, addListSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_point);
        btnStopPointList  = (Button)findViewById(R.id.btn_stopPointList);
        searchView = (SearchView) findViewById(R.id.location);
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
                        Toast.makeText(StopPointActivity.this, "Not find location", Toast.LENGTH_SHORT).show();
                    }
                    address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(),address.getLongitude());
                    map.clear();
//                   Toast.makeText(StopPointActivity.this, (int) a,Toast. LENGTH_SHORT).show();
                    //    if(map!=null) {
                    map.addMarker(new MarkerOptions().position(new LatLng(address.getLatitude(), address.getLongitude())).title(location).snippet(address.getAddressLine(0))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));

                    //neu tim ra
                    final String latitude = String.valueOf(address.getLatitude());
                    final String longitude = String.valueOf(address.getLongitude());
                    final String addr= address.getAddressLine(0);

                    SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP",MODE_PRIVATE);
                    if(sharedPreferences4.getString("tim sau khi tao","").equals("yes")) //tim kiem sau khi tao tour
                    {
                        byExtras2(latitude,longitude);
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StopPointActivity.this);
                        builder.setMessage("Are you ok with address ' "+ addr+" ' ?" + latitude+longitude)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        byExtras(latitude, longitude,addr);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
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
                        builder.setMessage("Are you sure to cancel tour?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences preferences = getSharedPreferences("saveSP",MODE_PRIVATE);
                                SharedPreferences.Editor editor1 = preferences.edit();
                                editor1.putString("tim sau khi tao","no");
                                editor1.putString("hoan thanh","ok");
                                String token = preferences.getString("tourCancel","");
                                String[] tokenList = token.split("/");
                                Log.e("size cancel", String.valueOf(tokenList.length));
                                editor1.putString("tourCancel",token+tourId+"/");
                                Log.e("day",token+tourId+"/");
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
        sharedPreferences= getSharedPreferences("tourSP",MODE_PRIVATE);
        tourStatus = sharedPreferences.getString("isCompleted","");
        if(tourStatus.equals("yes"))
        {
            tourId = getIntent().getStringExtra("tourId");
            getSuggestStopPoint();
        }

        sharedPreferences2 = getSharedPreferences("saveSP",MODE_PRIVATE);
        if(sharedPreferences2.getString("have","").equals("yes")) //neu da tao diem dung thanh cong
        {
            StopPointObject stopPointObject = new StopPointObject(
                    sharedPreferences2.getString("name",""),
                   sharedPreferences2.getString("address",""),
                   Integer.parseInt(sharedPreferences2.getString("province","")),
                    Double.parseDouble(sharedPreferences2.getString("lat","")),
                    Double.parseDouble(sharedPreferences2.getString("long","")),
                    Long.parseLong(sharedPreferences2.getString("arrival","")),
                    Long.parseLong(sharedPreferences2.getString("leave","")),
                    Integer.parseInt(sharedPreferences2.getString("service","")),
                    Float.parseFloat(sharedPreferences2.getString("minCost","")),
                    Float.parseFloat(sharedPreferences2.getString("maxCost","")),
                    null);
            SharedPreferences.Editor edit = sharedPreferences2.edit();
            edit.putString("have","no");
            edit.commit();

            stopPointObjectArrayList.add(stopPointObject);
        }
    }

    private void getSuggestStopPoint() {

        ArrayList<LocationStopPoint> locationStopPoints = new ArrayList<>();
        locationStopPoints.add(new LocationStopPoint(23.391185,105.323524));
        locationStopPoints.add(new LocationStopPoint(8.30,104.40));

        ArrayList<SuggestDescRequest.CoordList> coordLists = new ArrayList<>();
        coordLists.add(new SuggestDescRequest.CoordList(locationStopPoints));

        Retrofit retrofit = RetrofitClient.getClient();
        APIService apiService = retrofit.create(APIService.class);
        apiService.getSuggestDesc(token,new SuggestDescRequest(false,coordLists))
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
                                    LatLng newLocation = new LatLng(suggestResult.get(i).getLat(),suggestResult.get(i).getLongtitude());
                                    map.addMarker(new MarkerOptions().position(newLocation).title(suggestResult.get(i).getAddress()))
                                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                }
                            }
                        }
                        else
                        {
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

    public void byExtras(String srclat,String srclong,String address){
        Intent intent= new Intent(StopPointActivity.this,CreateTourActivity.class);
        intent.putExtra("token",token);
        intent.putExtra(SRC,num);
        intent.putExtra(TEMP,temp);
        intent.putExtra(SRCLAT,srclat);
        intent.putExtra(SRCLONG,srclong);
        intent.putExtra(ADDRESS,address);
        startActivity(intent);
        finish();
    }

    public void byExtras2(String srclat,String srclong){
        Intent intent= new Intent(StopPointActivity.this, AddStopPoint.class);
        intent.putExtra("token",token);
        intent.putExtra(SRC,num);
        intent.putExtra(SRCLAT,srclat);
        intent.putExtra(SRCLONG,srclong);
        startActivity(intent);
        SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP",MODE_PRIVATE);
        if(sharedPreferences4.getString("hoan thanh","").equals("ok"))
        {
            finish();
        }
    }

    public void ShowUp(){
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

        addLocationPermission();

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                if (tourStatus.equals("yes")) //neu da tao xong tour moi vao duoc
                {
                    /*String latitude = String.valueOf(address.getLatitude());
                    Log.e("lat:",latitude);
                    String longitude = String.valueOf(address.getLongitude());
                    Log.e("long:",longitude);
                    String addr = address.getAddressLine(0);*/
                    byExtras2(String.valueOf(latLng.latitude),String.valueOf(latLng.longitude));
                }
            }
        });

        btnStopPointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPointList = new Dialog(StopPointActivity.this);
                stopPointList.requestWindowFeature(Window.FEATURE_NO_TITLE);
                stopPointList.setContentView(R.layout.stop_point_list);
                listView1 = (ListView) stopPointList.findViewById(R.id.lvStopPointList);
                myAdapter_stopPointList = new MyAdapter_StopPointList(StopPointActivity.this,stopPointObjectArrayList);
                listView1.setAdapter(myAdapter_stopPointList);
                addListSP = (Button) stopPointList.findViewById(R.id.btnAddStopPoints);
                stopPointList.show();

               listView1.setOnLongClickListener(new View.OnLongClickListener() {
                   @Override
                   public boolean onLongClick(View v) {

                       return false;
                   }
               });

               addListSP.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if(stopPointObjectArrayList.size() > 0)
                       {
                           Retrofit retrofit = RetrofitClient.getClient();
                           APIService apiService = retrofit.create(APIService.class);
                           apiService.addStopPoint(token,new AddStopPointRequest(tourId,stopPointObjectArrayList))
                                   .enqueue(new Callback<ResponseBody>() {
                                       @Override
                                       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                           if(response.isSuccessful())
                                           {
                                               SharedPreferences sharedPreferences4 = getSharedPreferences("saveSP",MODE_PRIVATE);
                                               SharedPreferences.Editor editor4 = sharedPreferences4.edit();
                                               editor4.putString("hoan thanh","ok");
                                               editor4.commit();

                                               SharedPreferences preferences = getSharedPreferences("saveSP",MODE_PRIVATE);
                                               SharedPreferences.Editor editor1 = preferences.edit();
                                               editor1.putString("tim sau khi tao","no");
                                               editor1.commit();

                                               Toast.makeText(StopPointActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
                                               stopPointList.cancel();
                                               //startActivity(new Intent(StopPointActivity.this,MainActivity.class));
                                               finish();
                                           }
                                           else
                                           {
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
                       else Toast.makeText(StopPointActivity.this, "You must be choose stop point", Toast.LENGTH_SHORT).show();

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

                        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            buildAlertMessageNoGps();
                        }
                        else
                        {
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