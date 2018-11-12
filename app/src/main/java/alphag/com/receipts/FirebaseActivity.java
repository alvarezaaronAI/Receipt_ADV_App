package alphag.com.receipts;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseActivity";
    EditText mET_User_Id;
    EditText mET_Receipt_Id;
    //This returns a reference of the Json Tree
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userIdRef = mRootRef.child("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);

        mET_User_Id = (EditText) findViewById(R.id.et_user_id);
        mET_Receipt_Id = (EditText) findViewById(R.id.et_receipts_id);
    }

    public void addUser(String userId , String receiptId) {
        String userIdTemp = mET_User_Id.getText().toString();
        String receiptIdTemp = mET_Receipt_Id.getText().toString();

    }

    @Override
    protected void onStart() {
        super.onStart();

        userIdRef.addValueEventListener(new ValueEventListener() {
            //When ever the Users Id Database gets updated
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getValue(String.class);
                mET_User_Id.setText(userId);
            }
            //When ever theres an error
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
