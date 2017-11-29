package com.example.pc.mmsr_reader.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.mmsr_reader.Class.Contributor;
import com.example.pc.mmsr_reader.Class.Reader;
import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.R;
import com.example.pc.mmsr_reader.createAccountVerificationGoogle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by pc on 10/29/2017.
 */

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public static Activity loginActivity;
    DatabaseHandler myDb;
    private Reader reader;
    private GoogleApiClient googleAPiClient;
    private static final int REQ_CODE = 9001;
    private SignInButton googleSignIn;
    //private EditText etLoginID, etLoginPassword;
    //private TextView tvLoginID, tvPassword, tv1, tvHere, tv2;
    //private Button btnSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        loginActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myDb = new DatabaseHandler(this);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleAPiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions).build();
        googleSignIn = (SignInButton) findViewById(R.id.btnGoogleSignIn);
        googleSignIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleAPiClient);
                                                startActivityForResult(intent, REQ_CODE);
                                            }
                                        }

        );
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void savePersonalData(String loginMethod) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LoginStatus", "LogIn");
        editor.putString("loginMethod", loginMethod);
        editor.commit();
    }

    private void handleResult(GoogleSignInResult result) {
        Log.e("result", result + "");
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            String name = account.getDisplayName();
            String email = account.getEmail();
            createAccountVerificationGoogle createAccountverificationgoogle = new createAccountVerificationGoogle(LoginActivity.this,"", email, "", name);
            createAccountverificationgoogle.execute();
            savePersonalData("google");
        } else {
            Toast.makeText(this, "Login failed,try another method", Toast.LENGTH_LONG).show();
        }
    }
}
