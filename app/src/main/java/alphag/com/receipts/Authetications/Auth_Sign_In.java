package alphag.com.receipts.Authetications;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import alphag.com.receipts.R;
import alphag.com.receipts.Users.UserHomeActivity;

public class Auth_Sign_In extends AppCompatActivity {
    //Log Cat 
    private static final String TAG = "Auth_Sign_In";
    //Member Variables
    EditText mEmailSignIn;
    EditText mPassWordIn;

    //Firebase Authentication
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth__sign__in);
        //Reference
        mEmailSignIn = (EditText) findViewById(R.id.et_email_sign_in);
        mPassWordIn = (EditText) findViewById(R.id.et_password_sign_in);
        //Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "onCreate: " + mAuth.getUid());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth.getUid() != null ){
            //Handle User Already Sign In
            Toast.makeText(Auth_Sign_In.this, "User is already logged in.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Auth_Sign_In.this, UserHomeActivity.class);
            //Start the Intent.
            startActivity(intent);
        }
        else {
            Toast.makeText(Auth_Sign_In.this, "User is Not Logged in",
                    Toast.LENGTH_SHORT).show();
        }
    }

    //Method that will Handle Sign up A User
    public void sign_In_Handler(View view) {
        String emailTemp = mEmailSignIn.getText().toString();
        String passsWordTemp = mPassWordIn.getText().toString();

        mAuth.signInWithEmailAndPassword(emailTemp, passsWordTemp)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(Auth_Sign_In.this, UserHomeActivity.class);
                            //Start the Intent.
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(Auth_Sign_In.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
    //Method that will handle Sign Up
    public void sign_Up_Handler(View view) {
        Intent intent = new Intent(Auth_Sign_In.this, Auth_Sign_Up.class);
        //Start the Intent.
        startActivity(intent);
    }
}
