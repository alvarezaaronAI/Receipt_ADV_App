package alphag.com.receipts.Users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import alphag.com.receipts.R;
import alphag.com.receipts.UserHome;
import alphag.com.receipts.models.Receipt;

public class UserHomeActivity extends AppCompatActivity {

    Toolbar myToolBar;
    Spinner mySpinner;
    ImageButton myImageProfile;
    ImageButton myImageMap;

    RecyclerView mReceiptsRV;
    ReceiptAdapter mReceiptsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userhome);

        myToolBar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);
        myImageProfile = (ImageButton) findViewById(R.id.profileButton);
        mReceiptsRV = (RecyclerView) findViewById(R.id.rv_users_home_receipts);


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(UserHomeActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.days));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        ArrayList<Receipt> receiptsTemp = new ArrayList<>();
        Receipt receipt1 = new Receipt("123","345","213 Some Address","1/24/1996",13.45);
        Receipt receipt2 = new Receipt("123","345","213 Some Address","7/24/2018",13.45);
        Receipt receipt3 = new Receipt("123","345","213 Some Address","12/24/2018",13.45);
        receiptsTemp.add(receipt1);
        receiptsTemp.add(receipt2);
        receiptsTemp.add(receipt3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mReceiptsRV.setLayoutManager(layoutManager);
        mReceiptsRV.setHasFixedSize(true);

        mReceiptsAdapter = new ReceiptAdapter(receiptsTemp);
        mReceiptsRV.setAdapter(mReceiptsAdapter);


    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
