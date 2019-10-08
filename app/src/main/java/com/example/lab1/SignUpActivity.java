package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\+380(\\d){9}");

    private FirebaseAuth mAuth;

    private EditText txtInputEmail;
    private EditText txtInputName;
    private EditText txtInputPhone;
    private EditText txtInputPassword;

    private Button btnSignUp;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputName = findViewById(R.id.txtInputName);
        txtInputPhone = findViewById(R.id.txtInputPhone);
        txtInputPassword = findViewById(R.id.txtInputPassword);

        btnSignUp = findViewById(R.id.btnSignUp);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.INVISIBLE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateEmailField() | !validateNameField() | !validatePhoneField() |
                        !validatePasswordField()) {
                    return;
                }

                signUp(txtInputEmail.getEditableText().toString().trim(),
                        txtInputPassword.getEditableText().toString().trim());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public void signUp(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            setUserName(txtInputName.getEditableText().toString().trim(), currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setUserName(String name, FirebaseUser user) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        if (user != null) {
            user.updateProfile(userProfileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User profile updated.");
                                openWelcome();
                            }
                        }
                    });
        } else {
            Toast.makeText(SignUpActivity.this, "User is null, name isn't added",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validateEmailField() {
        String email = txtInputEmail.getEditableText().toString().trim();
        if (email.isEmpty()) {
            txtInputEmail.setError("This field is required");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtInputEmail.setError("Invalid email");
            return false;
        } else {
            txtInputEmail.setError(null);
            return true;
        }
    }

    public boolean validateNameField() {
        String name = txtInputName.getEditableText().toString().trim();
        if (name.isEmpty()) {
            txtInputName.setError("This field is required");
            return false;
        } else {
            txtInputName.setError(null);
            return true;
        }
    }

    public boolean validatePhoneField() {
        String phone = txtInputPhone.getEditableText().toString().trim();
        if (phone.isEmpty()) {
            txtInputPhone.setError("This field is required");
            return false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            txtInputPhone.setError("Invalid phone number");
            return false;
        } else {
            txtInputPhone.setError(null);
            return true;
        }
    }

    public boolean validatePasswordField() {
        String password = txtInputPassword.getEditableText().toString().trim();
        if (password.isEmpty()) {
            txtInputPassword.setError("This field is required");
            return false;
        } else if (password.length() < 8) {
            txtInputPassword.setError("Password must contain at least 8 symbols");
            return false;
        } else {
            txtInputPassword.setError(null);
            return true;
        }
    }

    public void openWelcome() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void openSignIn(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}
