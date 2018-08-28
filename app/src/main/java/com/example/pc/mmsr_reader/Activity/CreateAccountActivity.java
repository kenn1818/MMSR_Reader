package com.example.pc.mmsr_reader.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.pc.mmsr_reader.DatabaseHandler;
import com.example.pc.mmsr_reader.R;
import com.example.pc.mmsr_reader.createAccountVerification;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by pc on 1/12/2018.
 */

public class CreateAccountActivity  extends AppCompatActivity {
    public static Activity createAccountActivity;
    DatabaseHandler myDb;
    Button btnSignUp;
    Calendar myCalendar;
    EditText etLoginID,etLoginPassword,etConfirmPassword,etUserName,etDob;
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        myDb = new DatabaseHandler(this);
        createAccountActivity = this;


        myCalendar = Calendar.getInstance();

        etDob= (EditText) findViewById(R.id.etDOB);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateAccountActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etDob.setText(sdf.format(myCalendar.getTime()));
    }
    public void SignUp(View view) {
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etLoginID = (EditText) findViewById(R.id.etLoginID);
        etLoginPassword = (EditText) findViewById(R.id.etLoginPassword);
        etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        if (TextUtils.isEmpty(etLoginID.getText().toString())) {
            etLoginID.setError("Please fill in your email to continue.");
        } else {
            if (isValidEmail(etLoginID.getText().toString())) {
                String password = etLoginPassword.getText().toString();
                if (TextUtils.isEmpty(etLoginPassword.getText().toString())) {
                    etLoginPassword.setError("Please fill in your password to continue.");
                } else {
                    if (!password.matches("[a-zA-Z0-9]{6,12}")) {
                        etLoginPassword.setError("The password must be within 6 to 12 characters,no symbol allowed");
                    } else {
                        if (TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
                            etConfirmPassword.setError("Please fill in confirm password to continue.");
                        } else {
                            if (etLoginPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                                createAccountVerification createaccountverification = new createAccountVerification(CreateAccountActivity.this, etLoginID.getText().toString(), etLoginPassword.getText().toString(),etUserName.getText().toString(),etDob.getText().toString());
                                createaccountverification.execute();
                            } else {
                                etLoginPassword.setError("password and confirm password must be same");
                                etConfirmPassword.setError("password and confirm password must be same");
                            }
                        }
                    }
                }
            } else {
                etLoginID.setError("Please enter a correct email format.");
            }
        }
        //  }


    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
