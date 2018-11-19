package alphag.com.receipts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class UserHomeActivity extends AppCompatActivity {

    Toolbar myToolBar;
    Spinner mySpinner;
    ImageButton myImageProfile;
    ImageButton myImageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        myImageProfile = (ImageButton) findViewById(R.id.profileButton);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UserHomeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

    }



}
