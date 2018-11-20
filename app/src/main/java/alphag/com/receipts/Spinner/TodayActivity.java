package alphag.com.receipts.Spinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alphag.com.receipts.R;
import alphag.com.receipts.Users.ReceiptAdapter;
import alphag.com.receipts.models.Receipt;

public class TodayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ReceiptAdapter adapter;

    List<Receipt> receiptsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        receiptsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        receiptsList.add(new Receipt("123", "456", "10/10/2018", "12/24/1996",13.40));
        receiptsList.add(new Receipt("123", "456", "10/10/2018", "12/24/1996",13.40));
        receiptsList.add(new Receipt("123", "456", "10/10/2018", "12/24/1996",13.40));


        adapter = new ReceiptAdapter(receiptsList);
        recyclerView.setAdapter(adapter);
    }

}
