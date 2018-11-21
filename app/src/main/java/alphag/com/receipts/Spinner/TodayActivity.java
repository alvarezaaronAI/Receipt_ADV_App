package alphag.com.receipts.Spinner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Date;
import java.util.List;

import alphag.com.receipts.R;
import alphag.com.receipts.Users.ReceiptAdapter;
import alphag.com.receipts.Users.UserHomeActivity;
import alphag.com.receipts.Utils.DateUtils;
import alphag.com.receipts.models.Receipt;

public class TodayActivity extends AppCompatActivity {
    private static final String TAG = "TodayActivity";
    Toolbar myToolBar;
    Spinner mySpinner;
    ImageButton myImageProfile;

    RecyclerView mTodayReceiptsRV;
    ReceiptAdapter mTodayReceiptsAdap;

    //Firebase Database
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("users");

    ArrayList<Receipt> mfinalReceipts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        myImageProfile = (ImageButton) findViewById(R.id.profileButton);
        mTodayReceiptsRV = (RecyclerView) findViewById(R.id.rv_users_home_receipts);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(TodayActivity.this,
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

                mfinalReceipts = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Receipt receiptTemp = new Receipt(
                            "" + snapshot.child("longitude").getValue(),
                            "" +snapshot.child("latitude").getValue(),
                            "" +snapshot.child("address").getValue(),
                            "" + snapshot.child("date").getValue(),
                            Double.valueOf(snapshot.child("total").getValue().toString()));

                    Date receiptDate = DateUtils.parseDate(receiptTemp.getDate());
                    if(DateUtils.filter(receiptTemp, 1)){
                        mfinalReceipts.add(receiptTemp);
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(TodayActivity.this);
                    mTodayReceiptsRV.setLayoutManager(layoutManager);
                    mTodayReceiptsRV.setHasFixedSize(true);

                    Log.d(TAG, "onCreate: " + mfinalReceipts.get(0));
                    mTodayReceiptsAdap = new ReceiptAdapter(mfinalReceipts);
                    mTodayReceiptsRV.setAdapter(mTodayReceiptsAdap);
                }

                Toast.makeText(TodayActivity.this, "User Data was Appended", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}