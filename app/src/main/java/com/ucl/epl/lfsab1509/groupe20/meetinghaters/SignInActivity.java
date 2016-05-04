package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.ucl.epl.lfsab1509.groupe20.meetinghaters.DB.JsonRequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Based on the code of http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
 */
public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";

    private EditText emailEditText;
    private EditText passwordEditText;

    private String email;
    private String password;

    private MeetingApplication appInstance = MeetingApplication.getAppInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        emailEditText = (EditText) findViewById(R.id.text_email);
        passwordEditText = (EditText) findViewById(R.id.text_password);

        Button signInButton = (Button) findViewById(R.id.btn_signin);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        TextView signUpLink = (TextView)  findViewById(R.id.link_signup);
        signUpLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Tries to log the user in the database
     */
    private void signIn() {
        if (!validator()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.validate_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signin_message_dialog));
        progressDialog.show();

        // remote db
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mail", email);
            jsonObject.put("password", password);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonRequestHelper request = new JsonRequestHelper(
                Request.Method.POST,
                appInstance.remoteDBHandler.apiAuth(),
                jsonObject,
                "",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            appInstance.myDBHandler.addUser(email);
                            appInstance.myDBHandler.setToken(response.getString("token"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent i = new Intent(getApplicationContext(), MeetingListActivity.class);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                        } else if (error.networkResponse.statusCode == 500) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                        } else if (error.networkResponse.statusCode == 400) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.db_wring_password_error), Toast.LENGTH_LONG).show();
                        } else if (error.networkResponse.statusCode == 404) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.db_user_notfound_error), Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

        request.setPriority(Request.Priority.HIGH);
        appInstance.remoteDBHandler.add(request);
    }

    /*
     * Validate the information entered by the user in the form
     */
    private boolean validator() {
        boolean valid = true;

        /*
         * Retrieves the information of the input
         */
        email = emailEditText.getText().toString();
        password = passwordEditText.getText().toString();

        /*
         * Check if the email and password are correct following rules
         * if one input is invalid, set an error message for this input and return false
         * if the input is valid, remove any error message
         */
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
