package com.example.pc.mmsr_reader.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.pc.mmsr_reader.Class.Reader;
import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.LoginVerification;
import com.example.pc.mmsr_reader.R;

/**
 * Created by pc on 10/29/2017.
 */

public class LoginActivity extends AppCompatActivity {
    public static Activity loginActivity;
    DatabaseHandler myDb;
    private Reader reader;
    private EditText etLoginID, etLoginPassword;
    //private TextView tvLoginID, tvPassword, tv1, tvHere, tv2;
    private Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loginActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDb = new DatabaseHandler(this);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v){
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));

            }
        });
    }

    public void SignIn(View view) {
        if (TextUtils.isEmpty(etLoginID.getText().toString()) || TextUtils.isEmpty(etLoginPassword.getText().toString())) {
            if (TextUtils.isEmpty(etLoginID.getText().toString())) {
                etLoginID.setError("Please fill in your email to continue.");
            }
            if (TextUtils.isEmpty(etLoginPassword.getText().toString())) {
                etLoginPassword.setError("Please fill in your password to continue.");
            }
        } else {
            LoginVerification loginverification = new LoginVerification(getApplicationContext(), etLoginID.getText().toString(), etLoginPassword.getText().toString());
            loginverification.execute();
        }

    }

}
