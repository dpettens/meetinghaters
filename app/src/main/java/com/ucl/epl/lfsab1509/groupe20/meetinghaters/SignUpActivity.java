package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Based on the code of http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private EditText nameEditText;
    private EditText firstNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private String name;
    private String firstName;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = (EditText) findViewById(R.id.text_name);
        firstNameEditText = (EditText) findViewById(R.id.text_first_name);
        emailEditText = (EditText) findViewById(R.id.text_email);
        passwordEditText = (EditText) findViewById(R.id.text_password);

        Button signUpButton = (Button) findViewById(R.id.btn_signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Store the new user in the database
     */
    private void signUp() {

        if(!validator()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signup_message_dialog));
        progressDialog.show();

        
    }

    /*
     * Validate the information entered by the user in the form
     */
    private boolean validator() {
        boolean valid = true;

        /*
         * Retrieves the information of the input
         */
        name = nameEditText.getText().toString();
        firstName = firstNameEditText.getText().toString();
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        /*
         * Check if the name, firstName, email and password are correct following rules
         * if one input is invalid, set an error message for this input and return false
         * if the input is valid, remove any error message
         */
        if (name.isEmpty() || name.length() < 3) {
            nameEditText.setError(getString(R.string.name_error));
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (firstName.isEmpty() || firstName.length() < 3) {
            firstNameEditText.setError(getString(R.string.first_name_error));
            valid = false;
        } else {
            firstNameEditText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.email_error));
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 20) {
            passwordEditText.setError(getString(R.string.password_error));
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }
}
