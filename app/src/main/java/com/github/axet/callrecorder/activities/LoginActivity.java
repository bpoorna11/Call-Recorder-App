package com.github.axet.callrecorder.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.axet.callrecorder.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import okhttp3.MultipartBody;

public class LoginActivity extends AppCompatActivity  {
    private SignInButton signIn;
    private Button so;
    private int RC_SIGN_IN = 1;
    private String TAG = "MainActivity";
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
   // RecordingService recordingService;
    boolean mBounded;
    public static String personEmail="emailid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        SharedPreferences sp1 = getSharedPreferences("sdata", LoginActivity.MODE_PRIVATE);
        if (sp1.getString("login", "no").equals("yes")) {
            //login value is yes, so start mainactivity
            //  personEmail=mAuth.getCurrentUser().getEmail().toString();
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
            //finish();
        } else {
            //login value is no, so start loginactivity

            signIn = (SignInButton) findViewById(R.id.sign_in_button);
          //  so = (Button) findViewById(R.id.signout);
            mAuth = FirebaseAuth.getInstance();
            // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            // Build a GoogleSignInClient with the options specified by gso.
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    signIn();

                }
            });
           /* so.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAuth.signOut();
                    so.setVisibility(View.GONE);
                }
            });*/
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "You are not able to log in", Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void updateUI(FirebaseUser user) {
     //   so.setVisibility(View.VISIBLE);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Toast.makeText(getApplicationContext(),"Welcome "+personName,Toast.LENGTH_LONG).show();
        }
        personEmail=mAuth.getCurrentUser().getEmail().toString();
        SharedPreferences sp=getSharedPreferences("sdata",MainActivity.MODE_PRIVATE);
        SharedPreferences.Editor edd=sp.edit();
        edd.putString("login", "yes");
        edd.commit();

        Intent in = new Intent(getApplicationContext(), MainActivity.class);
        in.putExtra("senddata",personEmail);
        startActivity(in);

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Runtime permission
        try {
            boolean permissionGranted_ReadCon = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
            if (permissionGranted_ReadCon) {

                try {
                    // toggleButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, 700);
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 700) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    //toggleButton.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(requestCode==1){
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //makeCall();
                }
            }
        }
    }
}