package com.example.laharijan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.editEmailLogin);
        edtPassword = findViewById(R.id.passwordFieldLogin);
        btnLogin = findViewById(R.id.submitLogin);
        btnLogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(LoginActivity.this);
        progress.setTitle("Authentication");
        progress.setMessage("Please wait until, Login authentication finishes");
    }

    @Override
    public void onClick(View v) {
        if(edtEmail.getText().toString().equals("")||
        edtPassword.getText().toString().equals("")) {
            Toast.makeText(LoginActivity.this, "Please, fill all the fields!", Toast.LENGTH_SHORT).show();
        } else {
            progress.show();
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                }
            });
        }
    }
}