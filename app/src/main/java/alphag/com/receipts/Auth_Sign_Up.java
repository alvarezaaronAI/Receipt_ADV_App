package alphag.com.receipts;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import alphag.com.receipts.models.Receipt;
import alphag.com.receipts.models.User;

public class Auth_Sign_Up extends AppCompatActivity {
    //Log Cat
    private static final String TAG = "Auth_Sign_Up";
    //Member Variables
    EditText mFirstName;
    EditText mLastName;
    EditText mEmail;
    EditText mPassWord;
    EditText mPassWordMatch;
    //FireBase Authentication
    private FirebaseAuth mAuth;
    //FireBase DataBase reference
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference mUsersRef = mRootRef.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth__sign__up);
        //Views Instances
        mFirstName = (EditText) findViewById(R.id.et_last_name_sign_up);
        mLastName = (EditText) findViewById(R.id.et_first_name_sign_up);
        mEmail = (EditText) findViewById(R.id.et_email_sign_up);
        mPassWord = (EditText) findViewById(R.id.et_password_sign_up);
        mPassWordMatch = (EditText) findViewById(R.id.et_password_match_sign_up);
        //Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth != null){
            //Handle User Already Sign in

        }
        else {

        }
    }
    //This should make a new account and put in the database
    public void create_New_Account_Handler(View view) {
        final String firstNameTemp = mFirstName.getText().toString().trim();
        final String lastNameTemp = mLastName.getText().toString().trim();
        final String emailTemp = mEmail.getText().toString().trim();
        String passWordTemp = mPassWord.getText().toString().trim();
        String passWordMatchTemp = mPassWordMatch.getText().toString().trim();
        //Todo : Authenticate First, Last , Email , PassWord, and Password Match Cases
            //Write method called field_Authenticate_Users
        //Assuming All fields are correct
        mAuth.createUserWithEmailAndPassword(emailTemp,passWordTemp).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Receipt receiptTemp = new Receipt("1234","5678","My new Address", "11/12/2018",null,13.45);
                    ArrayList<Receipt> receiptsTemp = new ArrayList<>();
                    receiptsTemp.add(receiptTemp);
                    User userTemp = new User(firstNameTemp,lastNameTemp,emailTemp,receiptsTemp);
                    //Store User onto a firebase database;
                    mUsersRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userTemp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                //display toast if needed
                            }else{
                                //failed
                            }
                        }
                    });
                }
                else{
                    //Failed and we can display a toast.
                }
            }
        });
    }
    //If they are already signed in, then proceed to the app.

    //Todo : Make a method that checks for errors in name;

}
