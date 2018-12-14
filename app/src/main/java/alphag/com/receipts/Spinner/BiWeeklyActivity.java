package alphag.com.receipts.Spinner;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

import alphag.com.receipts.R;
import alphag.com.receipts.Users.ReceiptAdapter;
import alphag.com.receipts.Utils.DateUtils;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class BiWeeklyActivity extends AppCompatActivity {
    private static final String TAG = "BiWeeklyActivity";
    TextView mTotal;

    RecyclerView mReceiptsRV;
    ReceiptAdapter mReceiptsAdapter;
    //Firebase Database
    DatabaseReference mRootRef;
    DatabaseReference mUsersRef;

    ArrayList<Receipt> mfinalReceipts = new ArrayList<>();
    private double totalReceipt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bi_weekly);

        mTotal = (TextView) findViewById(R.id.tv_bi_weekly_total);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mRootRef.child(FireBaseDataBaseUtils.getUsersKey());

        mReceiptsRV = (RecyclerView) findViewById(R.id.rv_bi_weekly_activity);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userUId = currentUser.getUid();
        DatabaseReference userRootRef = mUsersRef.child(userUId);
        DatabaseReference userReceiptRootRef = userRootRef.child(FireBaseDataBaseUtils.getReceiptsKey());

        userReceiptRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Receipt> tempFinalReceipts = new ArrayList<>();
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
                    tempFinalReceipts.add(receiptTemp);
                }
                mfinalReceipts = DateUtils.filter(tempFinalReceipts,14);

                for (Receipt receipt: mfinalReceipts) {
                    totalReceipt += receipt.getTotal();
                }
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                mTotal.append("" + numberFormat.format(totalReceipt));

                Toast.makeText(BiWeeklyActivity.this, "User Data was Appended", Toast.LENGTH_SHORT).show();

                LinearLayoutManager layoutManager = new LinearLayoutManager(BiWeeklyActivity.this);
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
    }
}
