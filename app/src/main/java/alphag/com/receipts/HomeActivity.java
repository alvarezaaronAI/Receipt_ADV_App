package alphag.com.receipts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import alphag.com.receipts.Authetications.Auth_Sign_In;
import alphag.com.receipts.Authetications.Auth_Sign_Up;
import alphag.com.receipts.FireBaseMLK.CameraDetect;
import alphag.com.receipts.Spinner.TodayActivity;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;

public class HomeActivity extends AppCompatActivity {
    //Log Cat
    private static final String TAG = "HomeActivity";
    //-Member Variables-
    Button mButton_Camera;
    Button mtoday;
    //----------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mButton_Camera = findViewById(R.id.camera_button);
        mtoday = findViewById(R.id.today);

        mtoday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, TodayActivity.class);
                startActivity(intent);
            }
        });

    }
    /*
        This method goes to the next Activity, CAMERADETECT.
     */
    public void camera_button(View view) {
        //When button is clicked, goes through all this.
        //Make a new Intent
        Intent intent = new Intent(HomeActivity.this, CameraDetect.class);
        //Start the Intent.
        startActivity(intent);
    }

    public void sign_Up_Home_Handler(View view) {
        Intent intent = new Intent(HomeActivity.this, Auth_Sign_Up.class);
        //Start the Intent.
        startActivity(intent);
    }

    public void sign_In_Home_Handler(View view) {
        Intent intent = new Intent(HomeActivity.this, Auth_Sign_In.class);
        //Start the Intent.
        startActivity(intent);
    }

    public void firebase_database_delete_user_Handler(View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getUid() != null ) {
            Log.d(TAG, "firebase_database_delete_user_Handler: Current user is : " + (mAuth.getUid()));
            FirebaseUser userTemp = mAuth.getCurrentUser();
            new FireBaseDataBaseUtils().delete_User_DataBase(userTemp);
            Log.d(TAG, "firebase_database_delete_user_Handler: Current User is : " + (mAuth.getUid()));
        }
    }


}
