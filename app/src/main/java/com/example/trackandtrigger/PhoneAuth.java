package com.example.trackandtrigger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    //Member variable for the key verification in progress
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    //Creating FirebaseAuth member variable
    private FirebaseAuth mAuth;

    //Read Views
    TextInputEditText PHONE_NUMBER,OTP;
    TextView REQUEST_OTP,VERIFY_OTP;

    //Setting Boolean to say whether or not we are in progress.
    private boolean mVerificationInProgress = false;

    //Adding verification id as a member variable.
    private String mVerificationId;

    //Adding a member variable for PhoneAuthProvider.ForceResendingToken callback.
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    //Adding a member variable for a callback which is our PhoneAuthProvider.OnVerificationStateChangeCallbacks.
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        // Restoring the instance state
        if(savedInstanceState!=null){
            onRestoreInstanceState(savedInstanceState);
        }

        // Assigning all the views
        PHONE_NUMBER = findViewById(R.id.phoneNo);
        OTP = findViewById(R.id.otp_editText);
        REQUEST_OTP = findViewById(R.id.request_otpBtn);
        VERIFY_OTP = findViewById(R.id.verify_otpBtn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initializing phone auth callbacks  (For verification, Not entering code yet, To get text send to device)
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // It will be invoked in two situations, i.e., instant verification and auto-retrieval:
                // 1 - In few of the cases, the phone number can be instantly verified without needing to  enter or send a verification code.
                // 2 - On some devices, Google Play services can automatically detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                makeToast("Verification Completed");
                mVerificationInProgress = false;

                //Calling signInWithPhoneAuthCredential.
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // It is invoked when an invalid request is made for verification.                 //For instance, if the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                mVerificationInProgress = false;

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // Setting error to text field
                    PHONE_NUMBER.setError("Invalid phone number.");
                    makeToast("Enter Valid Phone number with country code!");
                }
                else{
                    makeToast(e.getMessage());
                }
                makeToast("Verification failed!");
            }

        // Creating onCodeSent() method called after the verification code has been sent by SMS to the provided phone number.
        @Override
        public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code will be sent to the provided phone number
            // Now need to ask the user for entering the code and then construct a credential
            // through integrating the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);
            makeToast("OTP sent!");
            // Save the verification ID and resend token to use them later
            mVerificationId = verificationId;
            mResendToken = token;
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Checking if the user is signed in or not. If signed in, then update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser!=null){
//            Toast.makeText(this, "Currently Signed In: "+currentUser.getEmail(),Toast.LENGTH_SHORT).show();
//            REQUEST_OTP.setVisibility(View.GONE);
//            VERIFY_OTP.setVisibility(View.GONE);
//            PHONE_NUMBER.setHint("Already Signed In");
//            OTP.setHint("Already Signed In");
//            // TODO: 05-11-2020  updateUI
//        }
//        else{
//            REQUEST_OTP.setVisibility(View.VISIBLE);
//            VERIFY_OTP.setVisibility(View.VISIBLE);
//        }

        //check if a verification is in progress. If it is then we have to re verify.  
        if (mVerificationInProgress && validatePhoneNumber()) {
            String phoneNo = PHONE_NUMBER.getText().toString();
            startPhoneNumberVerification(phoneNo);
            makeToast("Verification is in progress...");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }
    //Implementing RestoreInstanceState to restore the flag.
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    private void startPhoneNumberVerification(String phoneNumber) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        //Setting flag to say that the verification is in process.
        mVerificationInProgress = true;
    }
    
    //Creating a helper method for verification of phone number with code.
    // Entering code and manually signing in with that code
    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private boolean validatePhoneNumber() {
        // TODO: 05-11-2020
        String phoneNumber = PHONE_NUMBER.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            PHONE_NUMBER.setError("Enter valid Phone Number.");
            return false;
        }
        return true;
    }

    private void makeToast(String message) {
        Toast.makeText(this, message,Toast.LENGTH_SHORT).show();
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        //Adding onCompleteListener to signInWithCredential.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign-In is successful, update the UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            makeToast("OTP succesfully matched!");
                            FirebaseUser user = task.getResult().getUser();
                            updateUI(user);
                            PHONE_NUMBER.setVisibility(View.GONE);
                        } else {
                            // If the Sign-In fails, it will display a message and also update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                OTP.setError("Wrong OTP!");
                            }
                            else{
                                makeToast(task.getException().getMessage());
                            }
                            REQUEST_OTP.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, tempDashboard.class);
        startActivity(intent);
    }

    //Creating helper method for resending verification code.
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        makeToast("Siging in started!");
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void requestOTP(View view) {
        if(!validatePhoneNumber()){
            return;
        }
        REQUEST_OTP.setVisibility(View.GONE);
        startPhoneNumberVerification(PHONE_NUMBER.getText().toString());
    }

    public void verifyOTP(View view) {
        String otp = OTP.getText().toString();
        makeToast(otp);
        if (TextUtils.isEmpty(otp)) {
            OTP.setError("Cannot be empty.");
            return;
        }
        //Call the verifyPhoneNumberWithCode () method.
        makeToast("Comparing...");
        verifyPhoneNumberWithCode(mVerificationId, otp);
    }
}