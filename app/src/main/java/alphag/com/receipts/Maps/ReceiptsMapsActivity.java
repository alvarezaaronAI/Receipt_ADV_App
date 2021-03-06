package alphag.com.receipts.Maps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alphag.com.receipts.R;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class ReceiptsMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MapUtils";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private static final int MAXRECEIPTUPLOAD = 10;
    //Vars
    public final static int REQUEST_RECEIPT_MAPS = 5;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean permissionGranted;
    //Firebase Database
    DatabaseReference mRootRef;
    DatabaseReference mUsersRef;
    DatabaseReference mUserRef;
    DatabaseReference mUserReceiptRef;
    //Authentication User
    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    String mCurrentUserUID;
    //ArrayList
    ArrayList<Receipt> mfinalReceipts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipts_maps2);
        checkPermissions();
        Log.d(TAG, "onCreate: checkPermissions : " + permissionGranted);
        if (permissionGranted) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            //Get Current User
            mAuth = FirebaseAuth.getInstance();
            mCurrentUser = mAuth.getCurrentUser();
            mCurrentUserUID = mCurrentUser.getUid();
            //Getting Current User Receipt
            mRootRef = FirebaseDatabase.getInstance().getReference();
            mUsersRef = mRootRef.child(FireBaseDataBaseUtils.getUsersKey());
            mUserRef = mUsersRef.child(mCurrentUserUID);
            mUserReceiptRef = mUserRef.child(FireBaseDataBaseUtils.getReceiptsKey());
            Log.d(TAG, "onCreate: Receipt REf Path : " + mUserReceiptRef.getPath() );
            mUserReceiptRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mfinalReceipts = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Receipt receiptTemp = new Receipt(
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptUid()).getValue(),
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptName()).getValue(),
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptLon()).getValue(),
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptLat()).getValue(),
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptAddress()).getValue(),
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptDate()).getValue(),
                                "" + snapshot.child(FireBaseDataBaseUtils.getReceiptImag()).getValue(),
                                Double.valueOf(snapshot.child(FireBaseDataBaseUtils.getReceiptTotal()).getValue().toString()));
                        mfinalReceipts.add(receiptTemp);
                    }
                    Toast.makeText(ReceiptsMapsActivity.this, "Receipt Appended Data", Toast.LENGTH_SHORT).show();
                    addMarkers(MAXRECEIPTUPLOAD);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(ReceiptsMapsActivity.this, "Failed To Append Receipt Data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            onStart();
        }

    }

    private void testMarkers() {
        setMarkers("5151 State University Dr, Los Angeles, CA 90032", " My Cal State La Los Angeles");
    }

    private void addMarkers(int maxNumReceipts) {
        Log.d(TAG, "addMarkers: List Size" + mfinalReceipts.size());
        for (int numReceipts = 0; numReceipts < maxNumReceipts; numReceipts++) {
            Receipt tempReceipt = mfinalReceipts.get(numReceipts);
            setMarkers(tempReceipt.getAddress(), tempReceipt.getName()  +" " + tempReceipt.getDate() +" $" + tempReceipt.getTotal());
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng calstateLa = new LatLng(34.0667847, -118.1692549);
        mMap.addMarker(new MarkerOptions().position(calstateLa).title("Cal State Los Angeles"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(calstateLa));
        moveCamera(calstateLa, DEFAULT_ZOOM, "String");

    }

    public void setMarkers(String location, String markerName) {
        Geocoder gc = new Geocoder(ReceiptsMapsActivity.this);
        try {
            List<Address> fin = gc.getFromLocationName(location, 3);

            Address fin1 = fin.get(0);
            String cal = fin1.getLocality();

            double lat = fin1.getLatitude();
            double log = fin1.getLongitude();

            LatLng latlng = new LatLng(lat, log);
            MarkerOptions options = new MarkerOptions().position(latlng).title(markerName);
            mMap.addMarker(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getDeviceLocation() {
        Log.d(TAG, "InitMap: initializing map");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (permissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "OnComplete: Found Location");
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM, "My Location");

                        } else {
                            Log.d(TAG, "OnCOmplete: Unable to get location");

                        }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "GetDeviceLocation: SecurityException " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latlng, float zoom, String title) {
        Log.d(TAG, "MoveCamera: Moving the camera to: lat:" + latlng.latitude + ", lng:" + latlng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom));

        MarkerOptions options = new MarkerOptions().position(latlng).title(title);

        mMap.addMarker(options);
    }

    public boolean checkPermissions() {
        //Checking for Write External Storage Permission.
        //Checking for all permissions manifest State.
        int permissionCheckAccessFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckAccessCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //TODO Write Code to check permissions for Geo Location.
        // Permissions Check Int val will result 0 if all permissions was granted, other wise < 0 if 1 or many permissions were denied.
        //TODO Edit a better way to check all permissions at once without needed to add.
        Log.d(TAG, "checkPermissions: PERMISSION Map : " + permissionCheckAccessFineLocation + " PERMISSION Access fine location: " + permissionCheckAccessCoarseLocation);
        int permissionsCheck = permissionCheckAccessFineLocation + permissionCheckAccessCoarseLocation;
        //Allow the user to request permissions on the spot, if he wants.
        if (permissionsCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_RECEIPT_MAPS);
            return false;
        } else {
            permissionGranted = true;
            return true;
        }
    }

    //Method that handles permission response.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:  Map Request " + requestCode);
        //TODO Write code to check permissions result overall.
        if (requestCode == REQUEST_RECEIPT_MAPS) {
            //Receive permission result camera permission.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Camera Permissions has been granted, preview can be displayed.

                //TODO Show Camera preview.
                //Write Code here...

                Toast.makeText(this, "Maps is now Accessible.", Toast.LENGTH_SHORT).show();
                permissionGranted = true;
            } else {
                //Else all other permissions was denied. permission was denied.
                Toast.makeText(this, "Maps permissions was denied.", Toast.LENGTH_LONG).show();
            }
        } else {
            //show permission result.
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!permissionGranted) {
            finish();
        }
    }
}
