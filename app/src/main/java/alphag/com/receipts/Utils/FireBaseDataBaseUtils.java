package alphag.com.receipts.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alphag.com.receipts.models.User;

public class FireBaseDataBaseUtils {
    //Log Cats
    private static final String TAG = "FireBaseDataBaseUtils";
    //FireBase Static Key values for DataBase Root References
    //User Static Keys
    final static String FIRST_NAME_KEY = "first_Name";
    final static String LAST_NAME_KEY = "last_Name";
    final static String PHONE_KEY = "phone";
    final static String EMAIL_KEY = "email";
    //User Receipt Static Keys
    final static String RECEIPT_NAME = "name";
    final static String RECEIPT_ADDRESS = "address";
    final static String RECEIPT_TOTAL = "total";
    final static String RECEIPT_DATE = "date";
    final static String RECEIPT_LON = "longitude";
    final static String RECEIPT_LAT = "longitude";
    final static String RECEIPT_IMAG = "imagescr";

    //FireBase Static Key Vakyes for DataBase Root References
    final static String USERS_KEY = "users";
    final static String RECEIPTS_KEY = "receipts";
    //FireBase Database Root of the whole Database
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //FireBase Parent References
    /**
     * This method will allow you to get the root of a reference.
     * @param parentRoot The Parent root of the root key.
     * @param rootKey The Key Value Json of that root.
     * @return The Root DatabaseReference given the key value.
     */
    public DatabaseReference get_Root_Reference(DatabaseReference parentRoot , String rootKey){
        if(parentRoot != null) {
            Log.d(TAG, "get_Root_Reference: : Parent of Key is : " + parentRoot.getKey() );
            return parentRoot.child(rootKey);
        }
        Log.d(TAG, "get_Root_Reference: Failed : parent root is null");
        return null;
    }

    /**
     * Easy access To users key
     * @return Returns Users Database Reference
     */
    public DatabaseReference get_Users_Root(){
        if (mRootRef != null ) {
            DatabaseReference usersRoot = get_Root_Reference(mRootRef, USERS_KEY);
            Log.d(TAG, "get_Users_Root: Passed : Users ID Was Found" + "users");
            return usersRoot;
        }
        Log.d(TAG, "get_Users_Root: Failed : To get Users Root - FireBase Root is Null");
        return null;
    }
    //FireBase User Root Reference
    /**
     * Returns a current user root Reference.
     * @param user
     * @return
     */
    public DatabaseReference get_User_Root(@NonNull FirebaseUser user){
        String userUID = user.getUid();
        if (mRootRef != null ) {
            DatabaseReference userRoot = get_Root_Reference(get_Users_Root(), userUID);
            Log.d(TAG, "get_User_Root: Passed : To get User Root");
            return userRoot;
        }
        Log.d(TAG, "get_User_Root: Failed : To get User Root - FireBase Root is Null");
        return null;
    }
    /**
     * Returns the Root Reference Current User Receipts
     * @param user The current user.
     * @return The Root Reference of the current user.
     */
    public DatabaseReference get_User_Receipts_Root(@NonNull FirebaseUser user){
        DatabaseReference userReceiptRoot = get_User_Root(user).child(RECEIPTS_KEY);
        Log.d(TAG, "get_User_Receipts_Root: Passed : To get User Receipt Root");
        return userReceiptRoot;
    }
    /**
     * @param user Current User
     * @param receiptUID The UID of one receipt
     * @return DataBase Root Reference of Current Receipt UID
     */
    public DatabaseReference get_User_Receipt_UID(@NonNull FirebaseUser user, String receiptUID){
        return get_Root_Reference(get_User_Receipts_Root(user),receiptUID);
    }


    //Action Methods To Modify DataBase
    /**
     * This method would automatically add new a user onto the Firebase Database.
     * @param user This is the Firebase User Unique ID created from Firebase Authentication.
     * @param userObj This is the User Object, with all its info.
     */
    public void add_New_User_DataBase (@NonNull FirebaseUser user, @NonNull User userObj){
        String userUiTemp = user.getUid();
        DatabaseReference usersRootTemp = get_Users_Root();
        if (usersRootTemp != null) {
            usersRootTemp.child(userUiTemp)
                    .setValue(userObj)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "onComplete: Passed : Add User to Database");
                            } else {
                                Log.d(TAG, "onComplete: Failed : Add User To Database");
                            }
                        }
                    });
        }
    }
    /**
     * This method would delete a user from the FireBase Database, but NOT! from authentication.
     * @param user This is the user which in we want to delete from the FireBase Database/
     */
    public void delete_User_DataBase(@NonNull FirebaseUser user){
        String userUiTemp = user.getUid();
        DatabaseReference usersRootTemp = get_Users_Root();
        if (usersRootTemp != null) {
            //Todo : Make sure you confirm before deleting
            usersRootTemp.child(userUiTemp).removeValue();
        }


    }

    
    //Users Modifications
    /**
     * Method that will modify users first name their database
     * @param user Current User
     * @param newFirstName New First Name of Current User
     */
    public void modified_User_First_Name(@NonNull FirebaseUser user, String newFirstName){
        String userUiTemp = user.getUid();
        DatabaseReference usersRootTemp = get_Users_Root();
        if (usersRootTemp != null) {
            //Todo : Make sure you confirm before deleting
            usersRootTemp.child(userUiTemp).child(FIRST_NAME_KEY).setValue(newFirstName);
        }

    }
    /**
     * Method that will modify users last name on their database
     * @param user Current User
     * @param newLastName New Last Name of Current User
     */
    public void modified_User_Last_Name(@NonNull FirebaseUser user, String newLastName) {
        String userUiTemp = user.getUid();
        DatabaseReference usersRootTemp = get_Users_Root();
        if (usersRootTemp != null) {
            //Todo : Make sure you confirm before deleting
            usersRootTemp.child(userUiTemp).child(LAST_NAME_KEY).setValue(newLastName);
        }
    }
    /**
     * Method that will modify users phone number on their database
     * @param user Current User
     * @param newPhoneNumber New Phone Number of Current User
     */
    public void modified_User_Phone(@NonNull FirebaseUser user, String newPhoneNumber) {
        String userUiTemp = user.getUid();
        DatabaseReference usersRootTemp = get_Users_Root();
        if (usersRootTemp != null) {
            //Todo : Make sure you confirm before deleting
            usersRootTemp.child(userUiTemp).child(PHONE_KEY).setValue(newPhoneNumber);
        }
    }
    /**
     * Method that will modify users email on their database
     * @param user Current User
     * @param newEmail New Email of Current User
     */
    public void modified_User_Email(@NonNull FirebaseUser user, String newEmail) {
        String userUiTemp = user.getUid();
        DatabaseReference usersRootTemp = get_Users_Root();
        if (usersRootTemp != null) {
            //Todo : Make sure you confirm before deleting
            usersRootTemp.child(userUiTemp).child(EMAIL_KEY).setValue(newEmail);
        }
    }

    //Modify/Add Receipts (Specific Receipt)
    /**
     * Makes Modification for a Receipt Address
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newReceiptAddress New Address
     */
    public void modified_User_Receipt_Address(@NonNull FirebaseUser user, String receiptUID, String newReceiptAddress){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_ADDRESS).setValue(newReceiptAddress);
        }
    }
    /**
     * Makes Modification for a Receipt Date
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newDate New Date
     */
    public void modified_User_Receipt_Date(@NonNull FirebaseUser user, String receiptUID, String newDate){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_DATE).setValue(newDate);
        }
    }
    /**
     * Makes Modification for a Receipt Longitude
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newLongitude New Longitude
     */
    public void modified_User_Receipt_Longitude(@NonNull FirebaseUser user, String receiptUID, String newLongitude){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_LON).setValue(newLongitude);
        }
    }
    /**
     * Makes Modification for a Receipt Latitude
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newLatitude New Latitude
     */
    public void modified_User_Receipt_Latitude(@NonNull FirebaseUser user, String receiptUID, String newLatitude){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_LAT).setValue(newLatitude);
        }
    }
    /**
     * Makes Modification for a Receipt Total
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newTotal New Total
     */
    public void modified_User_Receipt_Total(@NonNull FirebaseUser user, String receiptUID, String newTotal){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_TOTAL).setValue(newTotal);
        }
    }
    /**
     * Makes Modification for a Receipt Image Source
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newImgScr New Image Source
     */
    public void modified_User_Receipt_ImageSrc(@NonNull FirebaseUser user, String receiptUID, String newImgScr){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_IMAG).setValue(newImgScr);
        }
    }
    /**
     * Makes Modification for a Receipt Name
     * @param user Current User
     * @param receiptUID Current Receipt Unique ID
     * @param newReceiptName New Receipt Name
     */
    public void modified_User_Receipt_Name(@NonNull FirebaseUser user, String receiptUID, String newReceiptName){
        DatabaseReference userReceiptRoot = get_User_Receipt_UID(user, receiptUID);
        if (userReceiptRoot != null) {
            //Todo : Make sure you confirm before deleting
            userReceiptRoot.child(RECEIPT_IMAG).setValue(newReceiptName);
        }
    }

    //Action Methods User Authentication
    //Todo : Create Method : Delete User
    //Todo : Create Method : Re-authenticate User
    //Todo : Create Method : Send Password Reset Email
    //Todo : Create Method : Set A Users new password
    //Todo : Create Method : Send Users Email Verification
    //Todo : Create Method : Set A Users new Email
    //Todo : Create Method : Get User Current Sign in

}
