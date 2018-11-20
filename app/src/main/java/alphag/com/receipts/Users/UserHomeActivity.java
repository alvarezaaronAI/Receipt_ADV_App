package alphag.com.receipts.Users;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.ArrayList;

import alphag.com.receipts.FireBaseMLK.CameraDetect;
import alphag.com.receipts.R;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class UserHomeActivity extends AppCompatActivity {
    private static final String TAG = "UserHomeActivity";

    Toolbar myToolBar;
    Spinner mySpinner;
    ImageButton myImageProfile;
    FloatingActionButton mFloatingActionBt;

    RecyclerView mReceiptsRV;
    ReceiptAdapter mReceiptsAdapter;


    //Firebase Database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("users");
    //FireBase Utils
    FireBaseDataBaseUtils mFireBaseDBUtils;

    ArrayList<Receipt> mfinalReceipts = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        myImageProfile = (ImageButton) findViewById(R.id.profileButton);
        mReceiptsRV = (RecyclerView) findViewById(R.id.rv_users_home_receipts);
        mFloatingActionBt = (FloatingActionButton) findViewById(R.id.fab_new_receipt);



        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UserHomeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);



        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUId =  currentUser.getUid();
        DatabaseReference userRootRef = mUsersRef.child(userUId);
        DatabaseReference userReceiptRootRef = userRootRef.child("receipts");


        userReceiptRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ArrayList<Receipt> receipts = JsonUtils.parseReceipts(dataSnapshot.getValue().toString());
//                Log.d(TAG, "onDataChange: " + receipts.get(0));
//                ArrayList<String> addresses = JsonUtils.getAddresses(receipts);
//
//                for(int i = 0 ; i < addresses.size(); i++){
//                    mAddressTv.append(addresses.get(i) + "\n");
//                }
                mfinalReceipts = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Receipt receiptTemp = new Receipt(
                            " " + snapshot.child("longitude").getValue(),
                            " " +snapshot.child("latitude").getValue(),
                            " " +snapshot.child("address").getValue(),
                            " " + snapshot.child("date").getValue(),
                            Double.valueOf(snapshot.child("total").getValue().toString()));
                    mfinalReceipts.add(receiptTemp);
                    Log.d(TAG, "onDataChange: " + receiptTemp.toString());

                    LinearLayoutManager layoutManager = new LinearLayoutManager(UserHomeActivity.this);
                    mReceiptsRV.setLayoutManager(layoutManager);
                    mReceiptsRV.setHasFixedSize(true);

                    Log.d(TAG, "onCreate: " + mfinalReceipts.get(0));
                    mReceiptsAdapter = new ReceiptAdapter(mfinalReceipts);
                    mReceiptsRV.setAdapter(mReceiptsAdapter);
                }
//                User user = dataSnapshot.getValue(User.class);
//                Log.d(TAG, "onDataChange: -----" + user.getEmail());

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
