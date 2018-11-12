package alphag.com.receipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseActivity";
    TextView mTv_User_Id;
    TextView mTv_Receipt_Id;
    //This returns a reference of the Json Tree
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUserIdRef = mRootRef.child("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mTv_User_Id = (TextView) findViewById(R.id.tv_user_id);
        mTv_Receipt_Id = (TextView) findViewById(R.id.tv_receipts_id);
    }

    public void addUser(String userId , String receiptId) {
        String userIdTemp = mTv_User_Id.getText().toString();
        String receiptIdTemp = mTv_Receipt_Id.getText().toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Test user Id = 1234567890
        DatabaseReference mReceipts = mUserIdRef.child("rec");
    }
}
