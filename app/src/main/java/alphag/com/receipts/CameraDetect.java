package alphag.com.receipts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.google.firebase.ml.vision.document.FirebaseVisionDocumentTextRecognizer;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CameraDetect extends AppCompatActivity {
    //Member Variables.
    private ImageView mImageView_Camera;
    private Button mButton_Snap;
    private Button mButton_Detect;
    private TextView mTextview_Text;
    //More Member Variables ( Function Available)
    private Bitmap imageBitmap;
    public String mCurrentPhotoPath;
    public boolean permissionGranted;
    //Static Member Variables.
    private static final int REQUEST_IMAGE_CAPTURE = 1 ;
    static final int REQUEST_TAKE_PHOTO = 1;
    public final static int REQUEST_CAMERA = 3;
    //Log Cats Variables
    public static String DTAG ="Files";
    public static String ITAG = "Permissions";
    public static String DImageTag = "Images";
    public static String FireBaseTag = "FireBase";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //On Create
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_detect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set member variables.
        mImageView_Camera = findViewById(R.id.camera_image);
        mButton_Snap = findViewById(R.id.camera_button_snapshot);
        mButton_Detect = findViewById(R.id.camera_button_detect);
        mTextview_Text = findViewById(R.id.camera_text);
        //---------------------
        //Check Permissions
        Log.i(ITAG, "Permissions State : " +permissionGranted + " On Create");
        if(!permissionGranted){
            Log.i(ITAG, "onCreate: Went Through here");
            if(checkPermissions()){
                permissionGranted = true;
            }
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
                Log.d(DTAG, "dispatchTakePictureIntent: " + photoFile.toString());
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.getStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.d(DTAG,"Passed Photo Path Not NULL");
                Uri photoURI = FileProvider.getUriForFile(this,
                        "alphag.com.receipts",
                        photoFile);
                Log.d(DTAG,"Created New URI");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                Log.d(DTAG,"Tooked a Picture.");
            }
            Log.d(DTAG,"Passed URI creator");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        setPic();
    }
    //This method will allows to retrieve the photo, to detect text recognition.
    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView_Camera.getWidth();
        int targetH = mImageView_Camera.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageBitmap = bitmap;
        mImageView_Camera.setImageBitmap(bitmap);
    }
    //This method will allow us to save the current photo to our gallery, using the most recent path.
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    /*
     * Handles Buttons actions
     */
    //This method would handle Button Snapshot.
    public void camera_button_snapshot(View view) {
        Log.i(ITAG, "Permissions State : " +permissionGranted + "Method");
        if(permissionGranted) {
            dispatchTakePictureIntent();
        }
        else{
            Intent intent = new Intent(this, HomeActivity.class);
            Snackbar.make(view, "You Must Grant permissions ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            startActivity(intent);
        }
    }
    //This method would handle Button Detect.
    public void camera_button_detect(View view) {
            detect_text();
    }
    /*
     * Handles image recognition
     */
    private void detect_text() {
        Log.d(FireBaseTag, " " + (imageBitmap == null) );
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
                Log.d(DTAG, "******* Failed (Are you connected to the wifi?) *********");
            }
        });
    }

    /*
     * Handles image processing to external uses
     */
//    FirebaseVisionText.TextBlock labelBlock;
//    FirebaseVisionText.TextBlock costBlock;
//    boolean foundLabelBlockFirstTime = false;
//    String[] keywords = {"total", "amount", "payment"};

//    private void process_text(FirebaseVisionText firebaseVisionText) {
//        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
//        if (blocks.size() == 0) {
//            showToast("No text found");
//            return;
//        }
//
//        // Find the block with the keywords...
//        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks() ){
//            String text = block.getText();
//            mTextview_Text.setTextSize(24);
//            mTextview_Text.append(text +  " \n --");
//
//            Log.d("TEXT: ", "\n");
//            Log.d("TEXT: ", "\n----------BLOCK----------\n");
//            Log.d("TEXT: ", "\n\n" + text);
//            Log.d("TEXT: ", "\n-------------------------\n");
//            Log.d("TEXT: ", "\n");
//
//
//            // on each line in each block...
//            //      Every line is returned as a String...
//            //          if a keyword is found in a line... SAVE BLOCK (label).
//            for (FirebaseVisionText.Line line : block.getLines()) {
//                for (String keyword : keywords) {
//                    if (line.getText().toLowerCase().contains(keyword)) {
//                        labelBlock = block;
////                        foundLabelBlockFirstTime = true;
//                    }
//                }
//            }
//        }
//        // can only use labelBlock if not null
//
//        if(labelBlock != null){
//            Log.d("TEXT:", "-----LabelBlock-----" + labelBlock.getText());
//        }
//
//    }


/*
    Find $ or '.' along with 2 integers after it
 */

    FirebaseVisionText.TextBlock labelBlock;
    FirebaseVisionText.TextBlock costBlock;
//    String[] keywords = {"total", "amount", "payment"};

    private void process_text(FirebaseVisionText firebaseVisionText) {
        HashMap<Integer, Double> pricesHashMap = new HashMap<>();
        boolean containsDollarSign = false;
        double maxPrice = -1;

        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        if (blocks.size() == 0) {
            showToast("No text found");
            return;
        }
        for (FirebaseVisionText.TextBlock block : firebaseVisionText.getTextBlocks() ){
            //String text = block.getText();
//            mTextview_Text.setTextSize(24);
//            mTextview_Text.append(text +  " \n --");


            List<FirebaseVisionText.Line> lines = block.getLines();
            for (int i = 0; i < lines.size(); i++) {
                FirebaseVisionText.Line line = lines.get(i);
                //for (String keyword : keywords) {
                containsDollarSign = line.getText().toLowerCase().contains("$");
                //containsCents = line.getText().toLowerCase().contains(".");
                if (containsDollarSign) {
//                        labelBlock = block;
                    // Store line.gettext into a hash map and then use it later to
                    // return the max value and display on screen
//                    Log.d("TEXT:", "-----Price-----" + line.getText());

                    // Clean up line and then place into map

                    pricesHashMap.put(i, cleanUpStringPriceToDoublePrice(line.getText()));

                    maxPrice = getMaxPrice(pricesHashMap.values());
                    Log.d("TEXT:", "-----TRUE TOTAL-----" + maxPrice);
                }
                //}
            }


        }
        Log.d("TEXT:", "-----MAP-----" + pricesHashMap.values());
        mTextview_Text.setTextSize(24);
        mTextview_Text.append("$" + maxPrice);
    }

    private double cleanUpStringPriceToDoublePrice(String price){

        //Clean this up too "APPS -VEG SPRINGROLL UPSELL 1.00"
        String rawStringArray[] = price.split(" ");
        //Log.d("TEXT:", "-----MAP-----" + Arrays.toString(rawStringArray));

        // Iterate and choose element with $ or .

        if (price != null){
            for (String aRawStringArray : rawStringArray) {
                if ((aRawStringArray.contains("$") || aRawStringArray.contains(".")) && !aRawStringArray.contains("%")) {
                    price = aRawStringArray;
                }
            }

            String stringDouble = price.replace("$", "");
            Log.d("TEXT:", "-----MAP-----" + stringDouble);
            return Double.parseDouble(stringDouble);
        }
        else{
            return -1;
        }
    }

    private double getMaxPrice(Collection<Double> prices){
        double maxPrice = 0;
        for(double price: prices){
            if (price > maxPrice){
                maxPrice = price;
            }
        }
        return maxPrice;
    }



    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    /*
     * Handles High Quality Picture
     */
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
    /*
     * Handles Permissions Methods
     */
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
        int permissionsCheck = permissionCheckCamera + permissionCheckReadable + permissionCheckWritable;
        Log.i(ITAG, "checkPermissions: " + permissionsCheck);
        //Allow the user to request permissions on the spot, if he wants.
        if (permissionsCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CAMERA);
            return false;
        } else {
            return true;
        }
    }
    //end checkPermissions.

    //Method that checks if External Device is Writable.
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
    //end isExternalStorageWritable.

    //Method that handles permission response.
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
    //end onRequestPermissionsResult.

}
