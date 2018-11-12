package alphag.com.receipts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    //-Member Variables-
    Button mButton_Camera;
    Button mButton_Firebase;
    //----------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mButton_Camera = findViewById(R.id.camera_button);
        mButton_Firebase = findViewById(R.id.firebase_button);
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

    public void firebase_button(View view) {
        Intent intent = new Intent(HomeActivity.this, FirebaseActivity.class);
        //Start the Intent.
        startActivity(intent);
    }
}
