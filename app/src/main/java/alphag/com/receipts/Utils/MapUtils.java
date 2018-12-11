package alphag.com.receipts.Utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alphag.com.receipts.R;

public class MapUtils extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
        }
    }

    private static final String TAG = "MapUtils";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    //Vars

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maputils);
        getLocationPermission();
    }

    private void getDeviceLocation(){
        Log.d(TAG, "InitMap: initializing map" );
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
       try {
           if(mLocationPermissionsGranted){
               Task location = mFusedLocationProviderClient.getLastLocation();
               location.addOnCompleteListener(new OnCompleteListener() {
                   @Override
                   public void onComplete(@NonNull Task task) {
                       if(task.isSuccessful()){
                           Log.d(TAG, "OnComplete: Found Location");
                           Location currentLocation = (Location)task.getResult();
                           moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                   DEFAULT_ZOOM, "My Location");

                       }
                       else{
                           Log.d(TAG, "OnCOmplete: Unable to get location");
                           Toast.makeText(MapUtils.this, "Cant find location", Toast.LENGTH_SHORT);
                       }
                   }
               });
           }

       }catch (SecurityException e){
           Log.e(TAG, "GetDeviceLocation: SecurityException " + e.getMessage());
       }
    }

    public void setMarkers(String location){
        Geocoder gc = new Geocoder(MapUtils.this);
        try {
            List<Address> fin =  gc.getFromLocationName(location, 3);

            Address fin1 = fin.get(0);
            String cal = fin1.getLocality();

            double lat = fin1.getLatitude();
            double log = fin1.getLongitude();

            LatLng latlng = new LatLng(lat,log);
            MarkerOptions options = new MarkerOptions().position(latlng).title("Spot");
            mMap.addMarker(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title){
        Log.d(TAG, "MoveCamera: Moving the camera to: lat:" + latlng.latitude + ", lng:" + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng,zoom) );

        MarkerOptions options = new MarkerOptions().position(latlng).title(title);

        mMap.addMarker(options);
        setMarkers("7122 Arbutus Ave Huntington park 90255");
    }

    public void initMap(){
        Log.d(TAG, "initMap: map is Initialized");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapUtils.this);

    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting locations permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                Log.d(TAG, "Permission: Permission Granted");
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //Initialize our map
                    initMap();
                }
            }
        }
    }


}