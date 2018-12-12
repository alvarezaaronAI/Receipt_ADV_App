package alphag.com.receipts.FireBaseMLK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import alphag.com.receipts.R;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
import alphag.com.receipts.Utils.ParseUtils;
import alphag.com.receipts.models.Receipt;

public class CameraDetect extends AppCompatActivity {
    //Log Cat
    private static final String TAG = "CameraDetectActivity";
    //Member Variables.
    private ImageButton mIamgeButton;
    private EditText mEditName;
    private EditText mEditDate;
    private EditText mEditTotal;
    private EditText mEditAddress;
    private ProgressBar mProgress;
    private Button mConfirmButton;

    //FireBase Authentications
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;

    //FireBase Storage
    private FirebaseStorage mStorage;
    private StorageReference mStorageRef;

    //Unique UUID For Receipt
    private String mReceiptUID;

    //More Member Variables ( Function Available)
    private Bitmap imageBitmap;
    public String mCurrentPhotoPath;
    public boolean permissionGranted;
    //Request Codes
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    public final static int REQUEST_CAMERA = 3;

    public static Set<Double> pricesHashSet = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //On Create
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detect);

        //Set member variables.
        mIamgeButton = (ImageButton) findViewById(R.id.ib_receipt_confirmation_image);
        mEditName = (EditText) findViewById(R.id.et_receipt_confirmation_name);
        mEditDate = (EditText) findViewById(R.id.et_receipt_confirmation_date);
        mEditTotal = (EditText) findViewById(R.id.et_receipt_confirmation_total);
        mEditAddress = (EditText) findViewById(R.id.et_receipt_confirmation_address);
        mConfirmButton = (Button) findViewById(R.id.bt_receipt_confirmation_button);
        mProgress = (ProgressBar) findViewById(R.id.pb_receipt_confirmation_progressbar);
        //---------------------
        //Setting FireBase Utils
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        mStorageRef = mStorage.getReference();
        //---------------------
        //Making a unique val for the image receipt For firebase
        mReceiptUID = UUID.randomUUID().toString();
        //---------------------
        checkPermissions();
        Log.d(TAG, "onCreate: permissions granted state ----------------: " + permissionGranted);
        if (permissionGranted) {
            camera_button_snapshot();
        } else {
            onStart();
        }

    }

    /*
     * This methods will allow us to take a picture.
     * --------------------
     * Notes: Idea - Camera - BitMap Object - Firebase - Detection -Extract
     * */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.getStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "alphag.com.receipts",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setPic();
    }

    //This method will allows to retrieve the photo, to detect text recognition.
    private void setPic() {
        //Setting Pictures to new Bitmap Location
        imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        Log.d(TAG, "setPic: Picture Current Photo Path : " + mCurrentPhotoPath);
        Toast.makeText(this, "Converted to BitMap", Toast.LENGTH_SHORT).show();
        //After it takes a picture, this should detect the text to upload.
        if (imageBitmap != null) {
            mIamgeButton.setImageBitmap(imageBitmap);
            mProgress.setVisibility(View.VISIBLE);
            mIamgeButton.setEnabled(false);
            mEditName.setEnabled(false);
            mEditAddress.setEnabled(false);
            mEditDate.setEnabled(false);
            mEditTotal.setEnabled(false);
            mConfirmButton.setEnabled(false);
            //fireBase_Storage_Upload_Receipt_Image();
        } else {
        }
        camera_button_detect();

    }

    //This method will allow us to save the current photo to our gallery, using the most recent path.
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    //This method would handle Button Snapshot.
    public void camera_button_snapshot() {
        Log.i(TAG, "Permissions State : " + permissionGranted + "Method");
        if (permissionGranted) {
            dispatchTakePictureIntent();
        } else {
            checkPermissions();
        }
    }

    //This method would handle Button Detect.
    public void camera_button_detect() {
        detect_text();
    }

    private void detect_text() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        /**
         FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
         .getOnDeviceTextRecognizer();
         **/
        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getCloudTextRecognizer();
        detector.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                process_text(firebaseVisionText);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "******* Failed (Are you connected to the wifi?) *********");
            }
        });
    }

    private void process_text(FirebaseVisionText firebaseVisionText) {
        // Reset elements
        pricesHashSet.clear();
        double maxPrice = -1;
        double cleanedNumber;
        String readErrorMessage = "Please Take Photo Again";
        String address = null;
        String date = null;

        // Base case for reading empty block
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            return;
        }

        // Read block by block
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks() ){
            List<FirebaseVisionText.Line> lines = block.getLines();

            // Look for address in block
            if(address == null) {
                address = ParseUtils.getAddressFromReceipt(block.getText());
                //Log.d("ADDRESS", "Address: " + address);
            }
            if(date == null){
                date = ParseUtils.getDateFromReceipt(block.getText());
            }
            // Read each line in current block
            for (int i = 0; i < lines.size(); i++) {
                FirebaseVisionText.Line line = lines.get(i);
                if(date == null){
                    date = ParseUtils.getDateFromReceipt(line.getText());
                }
                // Attempt to read line and add number to HashSet (MUST have try, catch)
                try{
                    ParseUtils.cleanUpStringPriceToDoublePrice(line.getText(), pricesHashSet);
                }catch(Exception e){
                    Log.d("TEXT:", "-----SKIPPING...-----" + "ERROR IN READING...");
                    cleanedNumber = -1;
                }
            }
        }

        // From HashSet --> Return max price from List
        maxPrice = ParseUtils.getMaxPrice(pricesHashSet);
        //-----------------Finished Reading------------------

        // Set Texts
        Log.d(TAG, "process_text: Getting Values from Firebase Authentication: \n"
                + date + "\n"
                + maxPrice + "\n"
                + address + "\n");
        //Make Sure Users cant edit while trying to figure out the values

        mEditName.setText("Receipt Name");
        mEditDate.setText(date);
        mEditTotal.setText("" + maxPrice);
        mEditAddress.setText(address);

        mProgress.setVisibility(View.GONE);
        mIamgeButton.setEnabled(true);
        mEditName.setEnabled(true);
        mEditAddress.setEnabled(true);
        mEditDate.setEnabled(true);
        mEditTotal.setEnabled(true);
        mConfirmButton.setEnabled(true);

    }

    private void fireBase_Database_Upload_Receipt(Receipt receiptToUpload) {
        //Get the Root Ref of Current User, Receipts
        DatabaseReference usersRootRef = mRootRef.child(FireBaseDataBaseUtils.getUsersKey());
        DatabaseReference currentUserRootRef = usersRootRef.child(mCurrentUser.getUid());
        DatabaseReference currentUserReceiptsRootRef = currentUserRootRef.child(FireBaseDataBaseUtils.getReceiptsKey());
        //Currently Under Users Receipts DataBase, Store them in the DataBase of Current User
        currentUserReceiptsRootRef
                .child(receiptToUpload.getReceiptUId())
                .setValue(receiptToUpload)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: Success : Added new Receipt To Current User " + mCurrentUser.getUid().toString());
                    }
                });
    }

    private void fireBase_Storage_Upload_Receipt_Image() {

        //Setting Up Storage before anything
        StorageReference mUsers = mStorageRef.child("" + FireBaseDataBaseUtils.getStorageUsers());
        StorageReference mUser = mUsers.child("" + mCurrentUser.getUid());
        StorageReference mUserReceipts = mUser.child("" + FireBaseDataBaseUtils.getStrorageUsersReceipts());
        StorageReference mUserReceipt = mUserReceipts.child("" + mReceiptUID);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        final UploadTask uploadTask = mUserReceipt.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: Path a Failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: Path Generated Success");
                Log.d(TAG, "onSuccess: Upload Byte : " +  taskSnapshot.getBytesTransferred());
                Log.d(TAG, "onSuccess: Upload Session: " + taskSnapshot.getUploadSessionUri());
            }
        });
        Log.d(TAG, "fireBase_Storage_Upload_Receipt_Image: Upload Task Snapshot : " + uploadTask.getSnapshot().getUploadSessionUri());
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    //Method that creates a path.
    private File createImageFile() throws IOException {
        // Create an temp image File to store.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "Receipt_Date_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Checks and grants permission to use camera, if and only if is not yet accepted.
    public boolean checkPermissions() {
        //Checking for Write External Storage Permission.
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "This app will only work with usable external storage.", Toast.LENGTH_LONG).show();
            return false;
        }
        //Checking for all permissions manifest State.
        int permissionCheckCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionCheckWritable = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheckReadable = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        //TODO Write Code to check permissions for Geo Location.
        // Permissions Check Int val will result 0 if all permissions was granted, other wise < 0 if 1 or many permissions were denied.
        //TODO Edit a better way to check all permissions at once without needed to add.
        Log.d(TAG, "checkPermissions: PERMISSION CAMERA : " + permissionCheckCamera + " PERMISSION WRITETable : " + permissionCheckWritable + "PERMISSIONS CHECK READBALE :  " + permissionCheckReadable);
        int permissionsCheck = permissionCheckCamera + permissionCheckReadable + permissionCheckWritable;
        //Allow the user to request permissions on the spot, if he wants.
        if (permissionsCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
            return false;
        } else {
            permissionGranted = true;
            return true;
        }
    }

    //Method that checks if External Device is Writable.
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }


    //Method that handles permission response.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult:  Camera Request " + requestCode);
        //TODO Write code to check permissions result overall.
        if (requestCode == REQUEST_CAMERA) {
            //Receive permission result camera permission.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Camera Permissions has been granted, preview can be displayed.

                //TODO Show Camera preview.
                //Write Code here...

                Toast.makeText(this, "Camera is now Open.", Toast.LENGTH_SHORT).show();
                permissionGranted = true;
            } else {
                //Else all other permissions was denied. permission was denied.
                Toast.makeText(this, "Camera permissions was denied.", Toast.LENGTH_LONG).show();
            }
        } else {
            //show permission result.
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //Activity Events

    @Override
    protected void onStart() {
        super.onStart();
        //If permissions were not granted we want to make sure it goes back one stack.
        if (!permissionGranted) {
            this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void receipt_confirmation_handle(View view) {
        //Uploading Image onto Storage FireBase
        fireBase_Storage_Upload_Receipt_Image();
        //if edits are not null then proceed to upload receipt online.
        //else toast to check some value.
        if (checkEditNulls()) {
            //Making a Temporary Receipt
            Receipt receiptToUpload = new Receipt(
                    mReceiptUID,
                    mEditName.getText().toString(),
                    "12345",
                    "67890",
                    mEditAddress.getText().toString(),
                    mEditDate.getText().toString(),
                    generateReceiptStoragePath(),
                    Double.parseDouble(mEditTotal.getText().toString()));

            Log.d(TAG, "process_text: \n Receipt : " + receiptToUpload.receiptUId +
                    " \n Receipt Name : " + receiptToUpload.getName() +
                    " \n Receipt longitude : " + receiptToUpload.getLongitude() +
                    " \n Receipt latitude : " + receiptToUpload.getLatitude() +
                    " \n Receipt address : " + receiptToUpload.getAddress() +
                    " \n Receipt date : " + receiptToUpload.getDate() +
                    " \n Receipt snapshotURI : " + receiptToUpload.getSnapshotUri() +
                    " \n Receipt price : " + receiptToUpload.getTotal()
            );
            //Uploading Receipt On DataBase FireBase
            fireBase_Database_Upload_Receipt(receiptToUpload);
            finish();
        }
    }

    private String generateReceiptStoragePath(){
        String defaultPath = "gs://receipts-alphag.appspot.com/users/"+ mCurrentUser.getUid() + "/receipts/"+ mReceiptUID;
        return defaultPath;
    }

    private boolean checkEditNulls() {
        if (
                (mEditName.getText().toString() != null)
                        || (mEditDate.getText().toString() != null)
                        || (mEditTotal.getText().toString() != null)
                        || (mEditAddress.getText().toString() != null)
                ) {
            return true;

        }
        return false;

    }

    public void receipt_confirmation_image_handle(View view) {
        Toast.makeText(this, "The Image was clicked.", Toast.LENGTH_SHORT).show();
    }
}
