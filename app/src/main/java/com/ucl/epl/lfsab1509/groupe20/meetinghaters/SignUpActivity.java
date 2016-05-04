package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;

/*
 * Based on the code of http://sourcey.com/beautiful-android-login-and-signup-screens-with-material-design/
 */
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    private EditText nameEditText;
    private EditText firstNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private String name;
    private String firstName;
    private String email;
    private String password;
    private Bitmap photo;

    private MeetingApplication appInstance = MeetingApplication.getAppInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameEditText = (EditText) findViewById(R.id.text_name);
        firstNameEditText = (EditText) findViewById(R.id.text_first_name);
        emailEditText = (EditText) findViewById(R.id.text_email);
        passwordEditText = (EditText) findViewById(R.id.text_password);

        Button takePhotoButton = (Button) findViewById(R.id.btn_photo);
        if (checkDeviceCamera()) {
            takePhotoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePhoto();
                }
            });
        } else {
            takePhotoButton.setEnabled(false);
        }

        Button signUpButton = (Button) findViewById(R.id.btn_signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        TextView signInLink = (TextView)  findViewById(R.id.link_signin);
        signInLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /*
     * Retrieve the resulting photo of the camera as a Bitmap object and resize it
     * see http://developer.android.com/intl/es/reference/android/provider/MediaStore.html#ACTION_IMAGE_CAPTURE
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap tmp = (Bitmap) extras.get("data");
            this.photo = Bitmap.createScaledBitmap(tmp, 50, 50, true);
        } else if(resultCode != RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.camera_error), Toast.LENGTH_LONG).show();
        }
    }

    /*
     * Take a photo with the default app
     */
    private void takePhoto() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /*
     * Check if the device has a camera
     */
    private boolean checkDeviceCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /*
     * Store the new user in the database
     */
    private void signUp() {
        String photo_encoded = null;

        if(! validator()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.validate_error_message), Toast.LENGTH_LONG).show();
            return;
        }

        // convert the photo to a base64 string for encoding in the database
        // see http://stackoverflow.com/questions/9224056/android-bitmap-to-base64-string
        if(this.photo != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.photo.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            photo_encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.signup_message_dialog));
        progressDialog.show();

        // remote db
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mail", this.email);
            jsonObject.put("password", this.password);
            jsonObject.put("firstname", this.firstName);
            jsonObject.put("surname", this.name);
            jsonObject.put("photo", photo_encoded);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonRequestHelper request = new JsonRequestHelper(
                Request.Method.POST,
                appInstance.remoteDBHandler.apiUserURL(),
                jsonObject,
                "",
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                        progressDialog.hide();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
                        } else {
                            switch (error.networkResponse.statusCode) {
                                case 409:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.db_user_exist_error), Toast.LENGTH_LONG).show();
                                    break;
                                case 500:
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.server_error), Toast.LENGTH_LONG).show();
                                    break;
                            }
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
         * Check if the lastName, firstName, email and password are correct following rules
         * if one input is invalid, set an error message for this input and return false
         * if the input is valid, remove any error message
         */
        if (name.isEmpty() || name.length() < 2) {
            nameEditText.setError(getString(R.string.last_name_error));
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
