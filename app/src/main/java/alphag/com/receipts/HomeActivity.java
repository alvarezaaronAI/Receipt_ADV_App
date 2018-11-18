package alphag.com.receipts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    //-Member Variables-
    Button mButton_Camera;
    String TAG = "Camera button";
    //----------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "camera activate ");

        mButton_Camera =  findViewById(R.id.camera_button);
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
}
