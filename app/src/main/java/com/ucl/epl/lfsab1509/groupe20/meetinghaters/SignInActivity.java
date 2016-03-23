package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_GOOGLE_SIGN_IN = 9001;

    private Firebase mFirebaseRef;
    private Firebase.AuthStateListener mAuthStateListener;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_signin);

        /*
         * Google Sign In : https://developers.google.com/identity/sign-in/android/sign-in
         * This function create an object to request the email and the basic profile form the user
         */
        GoogleSignInOptions mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /*
         * Create a GoogleApiClient with access to the Google Sign-In with the options in the
         * GoogleSignInOptions object
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        /*
         * Create a OnClick listener for the Google Sign In Button
         * and customize it by setting a wide button
         */
        SignInButton mGoogleSignInButton = (SignInButton) findViewById(R.id.google_sign_in_button);
        mGoogleSignInButton.setSize(SignInButton.SIZE_WIDE);
        mGoogleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        /*
         * Create the reference to the Firebase server for auth the user
         */
         mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));


        /*
         * Add a Listener for track auth state changes of the user
         * form Firebase
         */
        mAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    Log.d(TAG, "AuthStateListener : " + authData.toString());
                }
            }
        };
        mFirebaseRef.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseRef.removeAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
         */
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                /*
                * Signed in successfully, retrieve the email address to get
                * token form Google Api Client
                */
                GoogleSignInAccount account = result.getSignInAccount();
                if(account != null) {
                    String email = account.getEmail();
                    getGoogleOAuthTokenAndLogin(email);
                }
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * An unresolvable error has occurred and Google APIs (including Sign-In)
         * will not be available.
         */
        displayError("onConnectionFailed", connectionResult.toString());
    }

    private void getGoogleOAuthTokenAndLogin(final String email) {
        /*
         * Get OAuth token in Background
         */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = "oauth2:profile email";
                    token = GoogleAuthUtil.getToken(SignInActivity.this, email, scope);
                } catch (Exception e) {
                    /*
                     * This is a network or server error or a error that denote
                     * an authorization problem
                     */
                    displayError("doInBackground", e.getMessage());
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                if (token != null) {
                    /*
                     * Successfully got OAuth token, now login with Google
                     */
                    mFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            if (authData != null) {
                                Map<String, String> map = new HashMap<>();
                                map.put("provider", authData.getProvider());
                                if(authData.getProviderData().containsKey("id"))
                                    map.put("id", authData.getProviderData().get("id").toString());
                                if(authData.getProviderData().containsKey("accessToken"))
                                    map.put("accessToken", authData.getProviderData().get("accessToken").toString());
                                if(authData.getProviderData().containsKey("email"))
                                    map.put("email", authData.getProviderData().get("email").toString());
                                if(authData.getProviderData().containsKey("displayName"))
                                    map.put("displayName", authData.getProviderData().get("displayName").toString());
                                if(authData.getProviderData().containsKey("profileImageURL"))
                                    map.put("profileImageURL", authData.getProviderData().get("profileImageURL").toString());

                                mFirebaseRef.child("users").child(authData.getUid()).setValue(map);
                            }
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            displayError("onAuthenticationError", firebaseError.toString());
                        }
                    });
                }
            }
        };

        task.execute();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    private void displayError(String functionName, String error) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_message), Toast.LENGTH_LONG).show();
        Log.e(TAG, functionName + " : " + error);
    }
}
