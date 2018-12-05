package alphag.com.receipts.Users;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import alphag.com.receipts.R;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class ReceiptDetailedActivity extends AppCompatActivity {
    //Log Cat
    private static final String TAG = "ReceiptDetailedActivity";
    //Member Variables
    TextView mReceiptTitle;
    TextView mReceiptTotal;
    TextView mReceiptDate;
    TextView mReceiptAddress;
    String mReceiptUID;
    //Firebase Database
    private DatabaseReference mRootRef;
    private DatabaseReference mUsersRef;
    private DatabaseReference mCurrentUserRef;
    private DatabaseReference mReceiptsRef;
    private DatabaseReference mCurrentReceiptRef;
    //Athentication
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    //Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detailed);

        //Setting Items Val
        mReceiptTitle = (TextView) findViewById(R.id.tv_receipt_detail_title);
        mReceiptTotal = (TextView) findViewById(R.id.tv_receipt_detail_price);
        mReceiptDate = (TextView) findViewById(R.id.tv_receipt_detail_date);
        mReceiptAddress = (TextView) findViewById(R.id.tv_receipt_detail_address);
        //Authentication
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        //Accessing Firebase Database
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mRootRef.child(FireBaseDataBaseUtils.getUsersKey());
        mCurrentUserRef = mUsersRef.child(mCurrentUser.getUid());
        mReceiptsRef = mCurrentUserRef.child(FireBaseDataBaseUtils.getReceiptsKey());

        Intent previousIntent = getIntent();
        if (previousIntent.hasExtra(ReceiptAdapter.RECEIPT_UID)) {
            mReceiptUID = previousIntent.getStringExtra(ReceiptAdapter.RECEIPT_UID);
            mCurrentReceiptRef = mReceiptsRef.child(mReceiptUID);
            Log.d(TAG, "onCreate: " + mReceiptsRef.child(mReceiptUID));
            //Receipt Image

            //Receipt Content
            mCurrentReceiptRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mReceiptTitle.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptName()).getValue());
                    mReceiptTotal.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptTotal()).getValue());
                    mReceiptDate.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptDate()).getValue());
                    mReceiptAddress.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptAddress()).getValue());
                    Toast.makeText(ReceiptDetailedActivity.this, "Appended Receipt Data", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: Couldnt Append Receipt Data");
                    Toast.makeText(ReceiptDetailedActivity.this, "Failed to Append Receipt Data", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(this, "No Receipt Located", Toast.LENGTH_SHORT).show();
        }
    }



    public void receipt_confirmation_handler(View view) {
    }

    public void receipt_detailed_handler(View view) {

    }
}
