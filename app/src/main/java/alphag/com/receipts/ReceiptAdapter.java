package alphag.com.receipts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private Context mCtx;
    private List<Receipts> receiptsList;

    public ReceiptAdapter(Context mCtx, List<Receipts> receiptsList) {
        this.mCtx = mCtx;
        this.receiptsList = receiptsList;
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.receipt_viewholder, null);
        ReceiptViewHolder holder = new ReceiptViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder receiptViewHolder, int i) {
        Receipts receipt = receiptsList.get(i);

        receiptViewHolder.textLocation.setText(receipt.getLocation());
        receiptViewHolder.textDate.setText(receipt.getDate());
        receiptViewHolder.textPrice.setText(receipt.getPrice());
        receiptViewHolder.imageView.setImageDrawable(mCtx.getResources().getDrawable(receipt.getImage()));

    }

    @Override
    public int getItemCount() {
        return receiptsList.size();
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
