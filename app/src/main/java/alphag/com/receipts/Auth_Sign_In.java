package alphag.com.receipts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Auth_Sign_In extends AppCompatActivity {
    //Member Variables
    EditText mEmailSignIn;
    EditText mPassWordIn;
    //Firebase Authentication
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth__sign__in);
        //Reference
        mEmailSignIn = (EditText) findViewById(R.id.et_email_sign_in);
        mPassWordIn = (EditText) findViewById(R.id.et_password_sign_in);

    }
    //Method that will Handle Sign up A User
    public void sign_In_Handler(View view) {
        
    }
    //Method that will handle Sign Up
    public void sign_Up_Handler(View view) {

    }
}
