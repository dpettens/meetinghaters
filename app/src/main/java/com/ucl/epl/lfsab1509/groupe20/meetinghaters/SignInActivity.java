package com.ucl.epl.lfsab1509.groupe20.meetinghaters;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class SignInActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        /*
         * Google Sign In : https://developers.google.com/identity/sign-in/android/sign-in
         * This function create an object to request the email and the basic profile form the user
         */
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /*
         * Create a GoogleApiClient with access to the Google Sign-In with the options in the
         * GoogleSignInOptions object
         */
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        /*
         * Create a OnClick listener for the Google Sign In Button
         * and customize it by setting a wide button
         */
        SignInButton mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * An unresolvable error has occurred and Google APIs (including Sign-In)
         * will not be available.
         */
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
         * Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
         */
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
            /*
             * Signed in successfully, show authenticated UI.
             */
                GoogleSignInAccount account = result.getSignInAccount();
                String id = account.getId();
                String name = account.getDisplayName();
                String mail = account.getEmail();
                Uri photo = account.getPhotoUrl();
            }
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
