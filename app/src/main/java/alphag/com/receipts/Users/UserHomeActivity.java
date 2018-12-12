package alphag.com.receipts.Users;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import alphag.com.receipts.FireBaseMLK.CameraDetect;
import alphag.com.receipts.Maps.ReceiptsMapsActivity;
import alphag.com.receipts.R;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class UserHomeActivity extends AppCompatActivity{
    private static final String TAG = "UserHomeActivity";

    Toolbar myToolBar;
    Spinner mySpinner;
    ImageButton myImageProfile;
    ImageButton myMapButton;
    FloatingActionButton mFloatingActionBt;

    Toast mToast;

    RecyclerView mReceiptsRV;
    ReceiptAdapter mReceiptsAdapter;

    public boolean permissionGranted;
    public final static int REQUEST_MAPS = 4;
    //Firebase Database
    DatabaseReference mRootRef;
    DatabaseReference mUsersRef;

    ArrayList<Receipt> mfinalReceipts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mRootRef.child(FireBaseDataBaseUtils.getUsersKey());

        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        myImageProfile = (ImageButton) findViewById(R.id.profileButton);
        mReceiptsRV = (RecyclerView) findViewById(R.id.rv_users_home_receipts);
        mFloatingActionBt = (FloatingActionButton) findViewById(R.id.fab_new_receipt);
        myMapButton = (ImageButton) findViewById(R.id.mapButton);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UserHomeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUId =  currentUser.getUid();
        DatabaseReference userRootRef = mUsersRef.child(userUId);
        DatabaseReference userReceiptRootRef = userRootRef.child(FireBaseDataBaseUtils.getReceiptsKey());


        userReceiptRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mfinalReceipts = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
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

                    Log.d(TAG, "onDataChange: " + receiptTemp.toString());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(UserHomeActivity.this);
                    mReceiptsRV.setLayoutManager(layoutManager);
                    mReceiptsRV.setHasFixedSize(true);

                    Log.d(TAG, "onCreate: " + mfinalReceipts.get(0));
                    mReceiptsAdapter = new ReceiptAdapter(mfinalReceipts);
                    mReceiptsRV.setAdapter(mReceiptsAdapter);
                }
                Toast.makeText(UserHomeActivity.this, "User Data was Appended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mFloatingActionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(UserHomeActivity.this,CameraDetect.class);
                startActivity(cameraIntent);
            }

        });

            myMapButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(UserHomeActivity.this, "here mofo", Toast.LENGTH_SHORT).show();
                    if(permissionGranted) {
                        Intent mapsIntent = new Intent(UserHomeActivity.this, ReceiptsMapsActivity.class);
                        startActivity(mapsIntent);
                    }
                    else{
                       checkPermissions();
                    }
                }
            });

    }

    public boolean checkPermissions() {
        //Checking for Write External Storage Permission.
        //Checking for all permissions manifest State.
        int permissionCheckAccessFineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionCheckAccessCoarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //TODO Write Code to check permissions for Geo Location.
        // Permissions Check Int val will result 0 if all permissions was granted, other wise < 0 if 1 or many permissions were denied.
        //TODO Edit a better way to check all permissions at once without needed to add.
        Log.d(TAG, "checkPermissions: PERMISSION Map : " + permissionCheckAccessFineLocation + " PERMISSION Access fine location: " + permissionCheckAccessCoarseLocation );
        int permissionsCheck = permissionCheckAccessFineLocation + permissionCheckAccessCoarseLocation;
        //Allow the user to request permissions on the spot, if he wants.
        if (permissionsCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                            },
                    REQUEST_MAPS);
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
        if (requestCode == REQUEST_MAPS) {
            //Receive permission result camera permission.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Camera Permissions has been granted, preview can be displayed.

                //TODO Show Camera preview.
                //Write Code here...

                Toast.makeText(this, "Maps is now Open.", Toast.LENGTH_SHORT).show();
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

    }

    @Override
    protected void onPause() {
        super.onPause();
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


}
