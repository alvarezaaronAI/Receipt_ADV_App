package alphag.com.receipts.Users;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import alphag.com.receipts.R;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.models.Receipt;

public class ReceiptDetailedActivity extends AppCompatActivity {
    //Log Cat
    private static final String TAG = "ReceiptDetailedActivity";

    //Member Variables
    TextView mReceiptTitle;
    TextView mReceiptTotal;
    TextView mReceiptDate;
    TextView mReceiptAddress;
    ImageButton mImageButton;
    String mReceiptUID;
    //Firebase Database
    private DatabaseReference mRootRef;
    private DatabaseReference mUsersRef;
    private DatabaseReference mCurrentUserRef;
    private DatabaseReference mReceiptsRef;
    private DatabaseReference mCurrentReceiptRef;
    //Athentication
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    //Storage
    FirebaseStorage mStorageRef;
    //Image
    private byte[] imageByte = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_detailed);

        //Setting Items Val
        mReceiptTitle = (TextView) findViewById(R.id.tv_receipt_detail_title);
        mReceiptTotal = (TextView) findViewById(R.id.tv_receipt_detail_price);
        mReceiptDate = (TextView) findViewById(R.id.tv_receipt_detail_date);
        mReceiptAddress = (TextView) findViewById(R.id.tv_receipt_detail_address);
        mImageButton = (ImageButton) findViewById(R.id.iv_receipt_detailed_image);
        //Authentication
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        //Accessing Firebase Database
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mUsersRef = mRootRef.child(FireBaseDataBaseUtils.getUsersKey());
        mCurrentUserRef = mUsersRef.child(mCurrentUser.getUid());
        mReceiptsRef = mCurrentUserRef.child(FireBaseDataBaseUtils.getReceiptsKey());

        //Accessing Storage Database
        mStorageRef = FirebaseStorage.getInstance();
        StorageReference storageRefReference = mStorageRef.getReference();

        Intent previousIntent = getIntent();
        if (previousIntent.hasExtra(ReceiptAdapter.RECEIPT_UID)) {
            mReceiptUID = previousIntent.getStringExtra(ReceiptAdapter.RECEIPT_UID);
            mCurrentReceiptRef = mReceiptsRef.child(mReceiptUID);
            Log.d(TAG, "onCreate: " + mReceiptsRef.child(mReceiptUID));
            //Receipt Image

            //Receipt Content
            mCurrentReceiptRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mReceiptTitle.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptName()).getValue());
                    mReceiptTotal.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptTotal()).getValue());
                    mReceiptDate.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptDate()).getValue());
                    mReceiptAddress.setText("" + dataSnapshot.child(FireBaseDataBaseUtils.getReceiptAddress()).getValue());
                    Toast.makeText(ReceiptDetailedActivity.this, "Appended Receipt Data", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: Couldnt Append Receipt Data");
                    Toast.makeText(ReceiptDetailedActivity.this, "Failed to Append Receipt Data", Toast.LENGTH_SHORT).show();
                }
            });
            StorageReference mUsersStorageRef = storageRefReference.child(FireBaseDataBaseUtils.getStorageUsers());
            StorageReference mUserStorageRef = mUsersStorageRef.child(mCurrentUser.getUid());
            StorageReference mUSerReceiptsRef = mUserStorageRef.child(FireBaseDataBaseUtils.getStrorageUsersReceipts());
            StorageReference mUserReceiptRef = mUSerReceiptsRef.child(mReceiptUID);


            final long TEN_MEGABYTE = 10 * (1024 * 1024);
            mUserReceiptRef.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    imageByte = bytes;
                    Log.d(TAG, "onSuccess: Image Byte Downloaded");

                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
                    Bitmap rotatedImage = rotateBitmap(bitmap,90);
                    Bitmap fixedImage = resizedBitmap(rotatedImage,500, 750);
                    Toast.makeText(ReceiptDetailedActivity.this, "Appended Image Receipt", Toast.LENGTH_SHORT).show();
                    mImageButton.setImageBitmap(fixedImage);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    exception.printStackTrace();
                    Log.d(TAG, "onFailure: Did not Download Anything");
                }
            });


        } else {
            Toast.makeText(this, "No Receipt Located", Toast.LENGTH_SHORT).show();
        }
    }


    public void receipt_confirmation_handler(View view) {

    }

    public void receipt_detailed_handler(View view) {

    }
    private Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();

        matrix.postRotate(degrees);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(original, width, height, true);

        Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }

    public Bitmap resizedBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bitmap, 0, 0, width, height, matrix, false);
        bitmap.recycle();
        return resizedBitmap;
    }
}
