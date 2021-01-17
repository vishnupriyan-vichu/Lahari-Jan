package com.example.laharijan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtUsername, edtEmail, edtPhone, edtPassword;
    private TextView loginText;
    private Button registerBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtUsername = findViewById(R.id.editUserName);
        edtEmail = findViewById(R.id.editEmail);
//        edtPhone = findViewById(R.id.editTextPhoneLogin);
        edtPassword = findViewById(R.id.passwordField);
        loginText = findViewById(R.id.loginLink);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        registerBtn = findViewById(R.id.submit);
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance("https://lahari-jan-default-rtdb.firebaseio.com/").getReference().child("Users");
        ProgressDialog progress = new ProgressDialog(SignUpActivity.this);
        progress.setTitle("Authentication");
        progress.setMessage("Please wait until, authentication finishes.");
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtUsername.getText().toString().equals("") ||
                edtEmail.getText().toString().equals("") ||
//                edtPhone.getText().toString().equals("") ||
                edtPassword.getText().toString().equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please, fill all the fields!", Toast.LENGTH_SHORT).show();
                }
                else {
                    progress.show();
                    mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        userRef.child(mAuth.getCurrentUser().getUid()).child("Name")
                                        .setValue(edtUsername.getText().toString());
                                        Toast.makeText(SignUpActivity.this, edtUsername.getText().toString()+", Successfully registered your account!", Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(SignUpActivity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        progress.dismiss();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}