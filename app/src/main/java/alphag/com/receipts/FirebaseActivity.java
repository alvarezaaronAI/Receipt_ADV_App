package alphag.com.receipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class FirebaseActivity extends AppCompatActivity {
    private static final String TAG = "FirebaseActivity";
    EditText mET_User_Id;
    EditText mET_Receipt_Id;
    
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

}
