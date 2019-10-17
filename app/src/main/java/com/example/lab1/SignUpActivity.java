package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

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
                validateAndSignUp();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null).
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signUp(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            setUserName(txtInputName.getEditableText().toString().trim(), currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignUpActivity.this, R.string.auth_failed,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setUserName(String name, FirebaseUser user) {
        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();

        if (user != null) {
            updateUserProfile(user, userProfileChangeRequest);
        } else {
            Toast.makeText(SignUpActivity.this, R.string.username_not_exist,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserProfile(FirebaseUser user, UserProfileChangeRequest userProfileChangeRequest) {
        user.updateProfile(userProfileChangeRequest)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            openWelcome();
                        }
                    }
                });
    }

    private boolean validateEmailField(String email) {
        if (email.isEmpty()) {
            txtInputEmail.setError(getString(R.string.required_field));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtInputEmail.setError(getString(R.string.invalid_email));
            return false;
        } else {
            txtInputEmail.setError(null);
            return true;
        }
    }

    private boolean validateNameField(String name) {
        if (name.isEmpty()) {
            txtInputName.setError(getString(R.string.required_field));
            return false;
        } else {
            txtInputName.setError(null);
            return true;
        }
    }

    private boolean validatePhoneField(String phone) {
        if (phone.isEmpty()) {
            txtInputPhone.setError(getString(R.string.required_field));
            return false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            txtInputPhone.setError(getString(R.string.invalid_phone_number));
            return false;
        } else {
            txtInputPhone.setError(null);
            return true;
        }
    }

    private boolean validatePasswordField(String password) {
        if (password.isEmpty()) {
            txtInputPassword.setError(getString(R.string.required_field));
            return false;
        } else if (password.length() < 8) {
            txtInputPassword.setError(getString(R.string.password_symbols));
            return false;
        } else {
            txtInputPassword.setError(null);
            return true;
        }
    }

    private void validateAndSignUp() {
        String strInputEmail = txtInputEmail.getEditableText().toString().trim();
        String strInputName = txtInputName.getEditableText().toString().trim();
        String strInputPhone = txtInputPhone.getEditableText().toString().trim();
        String strInputPassword = txtInputPassword.getEditableText().toString().trim();

        if (!validateEmailField(strInputEmail) | !validateNameField(strInputName) | !validatePhoneField(strInputPhone) |
                !validatePasswordField(strInputPassword)) {
            return;
        }

        signUp(txtInputEmail.getEditableText().toString().trim(),
                txtInputPassword.getEditableText().toString().trim());
    }

    private void openWelcome() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
    }

    public void openSignIn(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

}