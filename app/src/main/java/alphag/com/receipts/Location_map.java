package alphag.com.receipts;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Location_map extends AppCompatActivity {
    private static final String TAG = "Location_map";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location );
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK:  Checking google services version");

        int avaliable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Location_map.this);
        if(avaliable == ConnectionResult.SUCCESS){
            //Everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK:  Google play servieces is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(avaliable)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK:  an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Location_map.this, avaliable, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this,"We can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}