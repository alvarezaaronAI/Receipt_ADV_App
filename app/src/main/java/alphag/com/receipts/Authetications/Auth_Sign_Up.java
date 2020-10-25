package alphag.com.receipts.Authetications;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.ArrayList;
import java.util.UUID;
import alphag.com.receipts.R;
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
    ProgressBar mProgressBar;
    Button mCreateAccountBt;

    //FireBase Authentication
    private FirebaseAuth mAuth;

    private byte[] mReceiptByteData;

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
        mProgressBar = (ProgressBar) findViewById(R.id.pb_auth_sign_up);
        mProgressBar.setVisibility(View.GONE);
        mCreateAccountBt = (Button) findViewById(R.id.bt_auth_sign_up_create_account);

        mFirstName.addTextChangedListener(loginTextWatcher);
        mLastName.addTextChangedListener(loginTextWatcher);
        mEmail.addTextChangedListener(loginTextWatcher);
        mPassWord.addTextChangedListener(loginTextWatcher);
        mPassWordMatch.addTextChangedListener(loginTextWatcher);

        //Set Button as disabled as default as soon as app is started
        mCreateAccountBt.setEnabled(false);

        //Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance();

        //mDefaultReciept = mStorage.getReference().child("https://firebasestorage.googleapis.com/v0/b/receipts-alphag.appspot.com/o/defaults%2Freceipts%2Fdefault_1.png?alt=media&token=daf6501a-5db1-4126-b202-f3bcbb800d79");

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth != null) {
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
        //mCreateAccountBt.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);

        //Downloading Default Receipt File

//        final long ONE_MEGABYTE = 1024 * 1024;
//        Log.d(TAG, "create_New_Account_Handler: Path : " + mDefaultReciept.getPath());
//        Log.d(TAG, "onCreate: START HERE -------------------");
//        mDefaultReciept.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "gs://receipts-alphag.appspot.com/defaults/receipts/default_1.png" is returns, use this as needed
//                Log.d(TAG, "onSuccess: is Byte Null" +bytes.length);
//                mReceiptByteData = bytes;
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Log.d(TAG, "onFailure: Nothing was downloaded.");
//            }
//        });
//        Log.d(TAG, "onCreate: END HERE -------------------");

        validate(firstNameTemp, lastNameTemp, emailTemp, passWordTemp, passWordMatchTemp);
    }

    public void validate(String firstNameTemp, String lastNameTemp, String emailTemp,
                         String passWordTemp, String passWordMatchTemp) {
        try {
            final String validFirstName = validateName(firstNameTemp);
            final String validLastName = validateName(lastNameTemp);
            final String validEmail = validateEmail(emailTemp);
            final String validPassword = validatePassword(passWordTemp, passWordMatchTemp);

            //Setting UP Users Account.
            mAuth.createUserWithEmailAndPassword(validEmail, validPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String receiptUID = UUID.randomUUID().toString();
                        Receipt defaultReceipt = new Receipt(
                                receiptUID,
                                "Pollo Loco",
                                "12345",
                                "67890",
                                "123 Default Address Ave, Los Angeles CA 90022",
                                "12/24/1996",
                                "gs://receipts-alphag.appspot.com/defaults/receipts/default_1.png",
                                0.00);

                        ArrayList<Receipt> defaultReceiptsUser = new ArrayList<>();
                        defaultReceiptsUser.add(defaultReceipt);
                        //Creating User data
                        User userTemp = new User(validFirstName, validLastName, validEmail, defaultReceiptsUser);
                        //Getting current User
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                        //Setting Up User DataBase
                        new FireBaseDataBaseUtils().add_New_User_DataBase(user, userTemp);


                        Intent intent = new Intent(Auth_Sign_Up.this, Auth_Sign_In.class);
                        //Start the Intent.
                        startActivity(intent);
                        //Sign in User if its Successful

                        Log.d(TAG, "onComplete: Success : To Create a new user.");
                    } else {
                        Log.d(TAG, "onComplete: Failed : To create a new user.");
                        Toast.makeText(Auth_Sign_Up.this, "Failed to Create Special Token", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            emailTemp = null;
            passWordTemp = null;
            //mCreateAccountBt.setEnabled(true);
            mProgressBar.setVisibility(View.GONE);
            Log.d(TAG, "create_New_Account_Handler: INSIDE CATCH --> " + e);
        }
    }

    public String validateName(String name) {
        if (name != null || name != "") {
            for (int i = 0; i < name.length(); i++) {
                if (Character.isLetter(i)) {
                    name = name.substring(0, 1).toUpperCase() +
                            name.substring(1, name.length());
                    Toast.makeText(this, "NAME: " + name, Toast.LENGTH_SHORT).show();

                } else {
                    return name;
                }
            }
        }
        return null;
    }

    public String validateEmail(String email) {
        if (email != null && email.contains("@") && email.contains(".")) {
            return email;
        }
        Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        return null;
    }


    public final int MIN_PASS_LENGTH = 6;

    public String validatePassword(String password, String reenteredPassword) {
        if (password.length() >= MIN_PASS_LENGTH && password.equals(reenteredPassword)) {
            return password;
        }
        Log.d(TAG, "validatePassword: CHECK PASSWORD");
        Toast.makeText(this, "Check Password", Toast.LENGTH_SHORT).show();
        return null;
    }

    //Text Edit Listener for handling toggling Sign Up button
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String firstName = mFirstName.getText().toString();
            String lastName = mLastName.getText().toString();
            String email = mEmail.getText().toString().trim();
            String password = mPassWord.getText().toString().trim();
            String matchPassword = mPassWordMatch.getText().toString().trim();
            mCreateAccountBt.setEnabled(!firstName.isEmpty() &&
                                        !lastName.isEmpty() &&
                                        !email.isEmpty() &&
                                        !password.isEmpty() &&
                                        !matchPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

}
