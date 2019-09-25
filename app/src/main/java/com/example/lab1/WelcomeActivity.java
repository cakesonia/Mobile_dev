package com.example.lab1;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        txtWelcome = findViewById(R.id.txtWelcome);

        displayUserName();
    }

    public void displayUserName() {
        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                txtWelcome.append(" " + userName + "!");
            } else {
                Toast.makeText(WelcomeActivity.this, "WTF?display", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
