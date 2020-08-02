package com.example.as2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText mUsername, mPassword;
    Button mLoginButton, mToRegisterButton;
    ProgressBar progressBar;
    FirebaseAuth fAuth;

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = findViewById(R.id.loginUsernameEditText);
        mPassword = findViewById(R.id.loginPasswordEditText);
        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        mLoginButton = findViewById(R.id.loginButton);
        mToRegisterButton = findViewById(R.id.toRegisterButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String username = mUsername.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username))
                {
                        mUsername.setError("Username is Required.");
                        return;
                }

                if (TextUtils.isEmpty(password))
                {
                        mPassword.setError("Password is Required.");
                        return;
                }

                if (password.length() < 6)
                {
                        mPassword.setError("Password must be more than five characters");
                        return;
                }
                progressBar.setVisibility(View.VISIBLE);

                // authenticate the user

                fAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Logged in Successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        button = (Button) findViewById(R.id.toRegisterButton);
        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openRegister();
            }
        });
    }

    public void openRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
}


























