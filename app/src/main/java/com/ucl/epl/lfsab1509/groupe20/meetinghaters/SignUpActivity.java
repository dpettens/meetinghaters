package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

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

    private MeetingApp appInstance = MeetingApp.getAppInstance();

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

        if(! validator()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.signup_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signup_message_dialog));
        progressDialog.show();

        //Remote db
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mail", this.email);
            jsonObject.put("password", this.password);
            jsonObject.put("firstname", this.firstName);
            jsonObject.put("surname", this.name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonRequestHelper request = new JsonRequestHelper(
                Request.Method.POST,
                appInstance.remoteDBHandler.apiUserURL(),
                jsonObject, //GET REQUEST so no JSONObject to pass
                "",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("First response: ", response.toString());
                        appInstance.myDBHandler.addUser(email);

                        JSONObject innerJsonObject = new JSONObject();
                        try {
                            innerJsonObject.put("mail", email);
                            innerJsonObject.put("password", password);
                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                        JsonRequestHelper innerRequest = new JsonRequestHelper(
                                Request.Method.POST,
                                appInstance.remoteDBHandler.apiAuth(),
                                innerJsonObject,
                                "",
                                "",
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.e("Second response: ", response.toString());
                                        try {
                                            appInstance.myDBHandler.setToken(response.getString("token"));
                                        } catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                        Intent i = new Intent(getApplicationContext(), MeetingListActivity.class);
                                        startActivity(i);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Second error: ", error.toString());
                                        progressDialog.hide();
                                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                                        } else if (error.networkResponse.statusCode == 500){
                                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }

                        );
                        innerRequest.setPriority(Request.Priority.HIGH);
                        appInstance.remoteDBHandler.add(innerRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("First error pre: ", "yop" /*new Integer(error.networkResponse.statusCode).toString()*/);
                        progressDialog.hide();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                        } else {
                            Log.e("First error post: ", String.valueOf(error.networkResponse.statusCode));
                            //*
                            switch (error.networkResponse.statusCode) {
                                case 409:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.db_user_exist_error), Toast.LENGTH_LONG).show();
                                    break;
                                case 500:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                    break;
                            }/**/
                        }
                    }
                }
        );

        request.setPriority(Request.Priority.HIGH);
        this.appInstance.remoteDBHandler.add(request);

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
        if (name.isEmpty() || name.length() < 2) {
            nameEditText.setError(getString(R.string.name_error));
            valid = false;
        } else {
            nameEditText.setError(null);
        }

        if (firstName.isEmpty() || firstName.length() < 2) {
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
