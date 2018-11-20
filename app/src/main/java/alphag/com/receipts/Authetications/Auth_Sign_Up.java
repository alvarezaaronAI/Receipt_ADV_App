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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import alphag.com.receipts.R;
import alphag.com.receipts.UserHome;
import alphag.com.receipts.Users.UserHomeActivity;
import alphag.com.receipts.Utils.FireBaseDataBaseUtils;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth__sign__up);
        //Views Instances
        mFirstName = (EditText) findViewById(R.id.et_first_name_sign_up);
        mLastName = (EditText) findViewById(R.id.et_last_name_sign_up);
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
    }
    //This should make a new account and put in the database
    public void create_New_Account_Handler(View view) {
        // changed email from final to not final String
        final String firstNameTemp = mFirstName.getText().toString().trim();
        final String lastNameTemp = mLastName.getText().toString().trim();
        String emailTemp = mEmail.getText().toString().trim();
        String passWordTemp = mPassWord.getText().toString().trim();
        String passWordMatchTemp = mPassWordMatch.getText().toString().trim();
        //Todo : Authenticate First, Last , Email , PassWord, and Password Match Cases
        validate(firstNameTemp, lastNameTemp, emailTemp, passWordTemp, passWordMatchTemp);

    }

    public void validate(String firstNameTemp, String lastNameTemp, String emailTemp,
                         String passWordTemp, String passWordMatchTemp){
        try{
            final String validFirstName = validateName(firstNameTemp);
            final String validLastName = validateName(lastNameTemp);
            final String validEmail = validateEmail(emailTemp);
            final String validPassword = validatePassword(passWordTemp, passWordMatchTemp);

            mAuth.createUserWithEmailAndPassword(validEmail,validPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Receipt receiptTemp = new Receipt(
                                "1234",
                                "5678",
                                "MyReceiptAddress",
                                "11/12/2018",
                                13.45);

                        ArrayList<Receipt> receiptsTemp = new ArrayList<>();
                        receiptsTemp.add(receiptTemp);
                        //Creating User data
                        User userTemp = new User(validFirstName,validLastName,validEmail,receiptsTemp);
                        //Getting current User
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        //Adding user to Database
                        new FireBaseDataBaseUtils().add_New_User_DataBase(user,userTemp);
                        Intent intent = new Intent(Auth_Sign_Up.this, UserHomeActivity.class);
                        //Start the Intent.
                        startActivity(intent);
                        //Sign in User if its Successful

                        Log.d(TAG, "onComplete: Success : To Create a new user.");
                    }
                    else{
                        Log.d(TAG, "onComplete: Failed : To create a new user.");
                        Toast.makeText(Auth_Sign_Up.this, "Failed to Create Special Token", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e){
            emailTemp = null;
            passWordTemp = null;
            Log.d(TAG, "create_New_Account_Handler: INSIDE CATCH --> " + e);
        }
    }

    public String validateName(String name){
        if(name != null || name != ""){
            for(int i = 0 ; i < name.length() ; i++){
                if(Character.isLetter(i)){
                    name = name.substring(0, 1).toUpperCase() +
                            name.substring(1, name.length());
                    Toast.makeText(this, "NAME: " + name, Toast.LENGTH_SHORT).show();

                }else{
                    return null;
                }
            }
        }
        return null;
    }

    public String validateEmail(String email){
        if (email != null && email.contains("@") && email.contains(".")){
            return email;
        }
        Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        return null;
    }


    public final int MIN_PASS_LENGTH = 6;
    public String validatePassword(String password, String reenteredPassword){
        if(password.length() >= MIN_PASS_LENGTH && password.equals(reenteredPassword)){
            return password;
        }
        Log.d(TAG, "validatePassword: CHECK PASSWORD");
        Toast.makeText(this, "Check Password", Toast.LENGTH_SHORT).show();
        return null;
    }

}
