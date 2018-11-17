package alphag.com.receipts.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthenticationUtils {
    private FirebaseAuth mAuth;
    //Todo : Add Constructor that takes in UsersUiD.

    public FirebaseAuthenticationUtils() {
        this.mAuth = mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseAuthenticationUtils(FirebaseAuth mAuthUser) {
        FirebaseUser firebaseUser = mAuthUser.getCurrentUser();

        this.mAuth = mAuth = FirebaseAuth.getInstance();
    }


    //Getters


    public FirebaseAuth getmAuth() {
        return mAuth;
    }
}
