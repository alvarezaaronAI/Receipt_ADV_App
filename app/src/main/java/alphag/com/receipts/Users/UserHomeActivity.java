package alphag.com.receipts.Users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import alphag.com.receipts.Spinner.BiWeeklyActivity;
import alphag.com.receipts.Spinner.MonthActivity;
import alphag.com.receipts.Spinner.TodayActivity;
import alphag.com.receipts.Spinner.WeekActivity;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class UserHomeActivity extends AppCompatActivity {
    private static final String TAG = "UserHomeActivity";

    Toolbar myToolBar;
    Spinner mySpinner;
    ImageButton myImageProfile;
    ImageButton myMapButton;
    FloatingActionButton mFloatingActionBt;

    RecyclerView mReceiptsRV;
    ReceiptAdapter mReceiptsAdapter;
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
        String userUId = currentUser.getUid();

        DatabaseReference userRootRef = mUsersRef.child(userUId);
        DatabaseReference userReceiptRootRef = userRootRef.child(FireBaseDataBaseUtils.getReceiptsKey());
        Log.d(TAG, "onCreate: " +userReceiptRootRef.getPath());
        userReceiptRootRef.addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(UserHomeActivity.this, "User Data was Appended", Toast.LENGTH_SHORT).show();
                LinearLayoutManager layoutManager = new LinearLayoutManager(UserHomeActivity.this);
                mReceiptsRV.setLayoutManager(layoutManager);
                mReceiptsRV.setHasFixedSize(true);

                Log.d(TAG, "onCreate: " + mfinalReceipts.get(0));
                mReceiptsAdapter = new ReceiptAdapter(mfinalReceipts);
                mReceiptsRV.setAdapter(mReceiptsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mFloatingActionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(UserHomeActivity.this, CameraDetect.class);
                startActivity(cameraIntent);
            }

        });

        myMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent = new Intent(UserHomeActivity.this, ReceiptsMapsActivity.class);
                startActivity(mapsIntent);
            }
        });
        //TODO : Fix Budget Conversion : Disabled for now.
        //appendToSpinner();
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

    private void appendToSpinner() {
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                view.setVisibility(View.GONE);
                String item = adapterView.getItemAtPosition(i).toString();
                switch (i) {
                    case 1:
                        Intent todayDay = new Intent(UserHomeActivity.this, TodayActivity.class);
                        startActivity(todayDay);
                        break;

                    case 2:
                        Intent sevenDay = new Intent(UserHomeActivity.this, WeekActivity.class);
                        startActivity(sevenDay);
                        break;

                    case 3:
                        Intent fourteenDay = new Intent(UserHomeActivity.this, BiWeeklyActivity.class);
                        startActivity(fourteenDay);
                        break;

                    case 4:
                        Intent thirtyDay = new Intent(UserHomeActivity.this, MonthActivity.class);
                        startActivity(thirtyDay);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}