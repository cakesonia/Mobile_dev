package com.example.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView txtWelcome;
    private Button btnSignOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        txtWelcome = findViewById(R.id.txtWelcome);
        btnSignOut = findViewById(R.id.btnSignOut);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                openSignIn();
            }
        });

        displayUserName();
    }

    private void displayUserName() {
        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                txtWelcome.append(" " + userName + "!");
            } else {
                Toast.makeText(WelcomeActivity.this, R.string.no_current_user, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }
}
