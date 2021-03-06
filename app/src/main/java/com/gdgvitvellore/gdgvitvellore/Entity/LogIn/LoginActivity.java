package com.gdgvitvellore.gdgvitvellore.Entity.LogIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gdgvitvellore.gdgvitvellore.Entity.Navigation.HomeActivity;
import com.gdgvitvellore.gdgvitvellore.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LoginActivity.class.getSimpleName(); //returns name of the class
    private static final int RC_SIGN_IN = 007;
    TextView skipThis;
    public static GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private SignInButton buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //TODO: Change the name of the buttonSignIn into standard ButtonSignIn

        buttonSignIn = (SignInButton) findViewById(R.id.button_sign_in);
        buttonSignIn.setOnClickListener(this);
        skipThis=(TextView)findViewById(R.id.skip_this);
        skipThis.setOnClickListener(this);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),"fonts/Montserrat-Regular.ttf");

        skipThis.setTypeface(custom_font);
        //Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        buttonSignIn.setSize(SignInButton.SIZE_STANDARD);
        buttonSignIn.setScopes(gso.getScopeArray());
    }

    private void signIn() {
        //Maybe here signInIntent will get intent data from mGoogleApiClient
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        //Starting the intent prompts the user to select a Google account to sign in with.
        // If you requested scopes beyond profile, email, and openid,
        // the user is also prompted to grant access to the requested resources.
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "HandleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            //Signed in succesfilly, show authenticated UI
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name:" + acct.getDisplayName());
            String personName = acct.getDisplayName();
            String personPhotoUrl;
            if(acct.getPhotoUrl()!=null)
            {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            else
            {
                personPhotoUrl="https://developers.google.com/experts/img/user/user-default.png";
            }
            String personemail = acct.getEmail();

                Intent toMainActivity=new Intent(this, HomeActivity.class);
                toMainActivity.putExtra("personName",personName);
                toMainActivity.putExtra("personPhotoUrl",personPhotoUrl);
                Log.e(TAG, "Name: " + personName + ",Image: " + personPhotoUrl);
                startActivity(toMainActivity);
        }
    }

    // onActivityResult() is called whenever user returns from Google Login UI.
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
                // and the GoogleSignInResult will be available instantly.
                Log.d(TAG, "Got cached sign-in");
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                // If the user has not previously signed in on this device or the sign-in has expired,
                // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                // single sign-on will occur in this branch.
                showProgressDialog();
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        hideProgressDialog();
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed: " + connectionResult);
    }

    private void showProgressDialog(){
        if(mProgressDialog==null){
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog(){
        if(mProgressDialog!=null && mProgressDialog.isShowing()){
            mProgressDialog.hide();
        }
    }

    @Override
    public void onClick(View v){
        int id=v.getId();
        switch (id)
        {
            case R.id.button_sign_in:
                signIn();
                break;
            case R.id.skip_this:
                Intent intent=new Intent(this,HomeActivity.class);
                intent.putExtra("personName","Guest");
                intent.putExtra("personPhotoUrl","");
                startActivity(intent);
        }
    }


   }