package alphag.com.receipts.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alphag.com.receipts.models.User;

public class FireBaseDataBaseUtils {
    //Log Cats
    private static final String TAG = "FireBaseDataBaseUtils";
    //FireBase Static Key values for FireBase
    final static String FIRST_NAME_KEY = "first_Name";
    final static String LAST_NAME_KEY = "last_Name";
    final static String PHONE_KEY = "phone";
    final static String EMAIL_KEY = "email";
    //FireBase Database Root of the whole Database
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    //FireBase Methods For App

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
            DatabaseReference usersRoot = get_Root_Reference(mRootRef, "users");
            Log.d(TAG, "get_Users_Root: Passed : Users ID Was Found" + "users");
            return usersRoot;
        }
        Log.d(TAG, "get_Users_Root: Failed : To get Users Root - FireBase Root is Null");
        return null;
    }


    //Action Methods To Modify DataBase
    //Todo : Create Method : Add : User Data
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
    //Todo : Create Method : Delete : User Data
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
    //Todo : Create Method : Modifies User : First Name
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
    //Todo : Create Method : Modifies User : Last Name
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
    //Todo : Create Method : Adds/Modifies User : Phone Number
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
    //Todo : Create Method : Adds/Modifies User : Email
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


    //Users Data Retrival DataBase
    //Todo : Create Method : Retrieves : Email
    //Todo : Create Method : Retrieves : First Name
    //Todo : Create Method : Retrieves : Last Name
    //Todo : Create Method : Retrieves : Receipts (If any)
    //Todo : Create Method : Retrieves : Specific Receipt (Given a Receipts Id)

    //Action Methods User Authentication
    //Todo : Create Method : Delete User
    //Todo : Create Method : Re-authenticate User
    //Todo : Create Method : Send Password Reset Email
    //Todo : Create Method : Set A Users new password
    //Todo : Create Method : Send Users Email Verification
    //Todo : Create Method : Set A Users new Email
    //Todo : Create Method : Get User Current Sign in

    //Modify/Add Receipts (Specific Receipt)
    //TOdo : Create Method : Modifies/Adds Receipt : Address
    //TOdo : Create Method : Modifies/Adds Receipt : Date
    //TOdo : Create Method : Modifies/Adds Receipt : Longitude
    //TOdo : Create Method : Modifies/Adds Receipt : Latitude
    //TOdo : Create Method : Modifies/Adds Receipt : Total
    //TOdo : Create Method : Modifies/Adds Receipt : Image Scr

}