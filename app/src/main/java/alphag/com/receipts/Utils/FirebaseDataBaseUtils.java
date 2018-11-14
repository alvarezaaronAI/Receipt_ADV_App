package alphag.com.receipts.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import alphag.com.receipts.models.User;

public class FirebaseDataBaseUtils {
    //Log Cats
    private static final String TAG = "FirebaseDataBaseUtils";
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
    public void delete_User_DataBase(String userUiD, @NonNull FirebaseUser user){
        //Todo : Get Instance of Current User Through FirebaseAuthenticationUtils
    }
    /**
     * Easy access To users key
     * @return Returns Users Database Reference
     */
    public DatabaseReference get_Users_Root(){
        if (mRootRef != null ) {
            DatabaseReference musersRoot = get_Root_Reference(mRootRef, "users");
            Log.d(TAG, "get_Users_Root: Passed : Users ID Was Found" + "users");
            return musersRoot;
        }
        Log.d(TAG, "get_Users_Root: Failed : To get Users Root - FireBase Root is Null");
        return null;
    }


}
