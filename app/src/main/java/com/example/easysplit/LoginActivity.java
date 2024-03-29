package com.example.easysplit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private EditText email;
    private EditText password;
    private Button login;
    //private TextView registerUser;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
      //  registerUser = findViewById(R.id.register_user);

        auth = FirebaseAuth.getInstance();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(LoginActivity.this, "before calling!", Toast.LENGTH_SHORT).show();
                    loginUser(txt_email , txt_password);
                    //Toast.makeText(LoginActivity.this, "afetr calling!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email , password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this,"Login successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this , MainActivity.class));
                finish();
            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,"Wrong username or password !", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void onBackPressed(){
        finishAffinity();
    }
}