package alphag.com.receipts.Spinner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alphag.com.receipts.R;
import alphag.com.receipts.Users.ReceiptAdapter;
import alphag.com.receipts.Users.Receipts;

public class TodayActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ReceiptAdapter adapter;

    List<Receipts> receiptsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);


        receiptsList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        receiptsList.add(
                new Receipts(
                        1, "Panda Express", "10/10/2018", "$12.07",
                        R.drawable.ic_baseline_receipt_24px));

        receiptsList.add(
                new Receipts(
                        2, "Panda Express", "10/10/2018", "$12.07",
                        R.drawable.ic_baseline_receipt_24px));

        receiptsList.add(
                new Receipts(
                        3, "Panda Express", "10/10/2018", "$12.07",
                        R.drawable.ic_baseline_receipt_24px));

        receiptsList.add(
                new Receipts(
                        4, "Panda Express", "10/10/2018", "$12.07",
                        R.drawable.ic_baseline_receipt_24px));

        adapter = new ReceiptAdapter(this, receiptsList);
        recyclerView.setAdapter(adapter);
    }

}
