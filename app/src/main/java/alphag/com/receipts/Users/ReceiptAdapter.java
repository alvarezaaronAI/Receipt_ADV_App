package alphag.com.receipts.Users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import alphag.com.receipts.R;
import alphag.com.receipts.models.Receipt;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private List<Receipt> mReceiptsList;
    private Context mContext;
    public ReceiptAdapter(List<Receipt> receiptsList) {
        this.mReceiptsList = receiptsList;
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.receipt_viewholder, null);
        ReceiptViewHolder holder = new ReceiptViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder receiptViewHolder, int i) {
        Receipt receipt = mReceiptsList.get(i);

        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPrice = df.format(receipt.getTotal());

        receiptViewHolder.textLocation.setText(receipt.getAddress());
        receiptViewHolder.textDate.setText(receipt.getDate());
        receiptViewHolder.textPrice.setText("$" + formattedPrice);

    }

    @Override
    public int getItemCount() {
        return mReceiptsList.size();
    }

    class ReceiptViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textLocation;
        TextView textDate;
        TextView textPrice;

        public ReceiptViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_receipt_image);
            textLocation = itemView.findViewById(R.id.tv_receipt_location);
            textDate = itemView.findViewById(R.id.tv_receipt_date);
            textPrice = itemView.findViewById(R.id.tv_receipt_price);


        }
    }
}
