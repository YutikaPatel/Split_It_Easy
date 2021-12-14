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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button register;
    private EditText name;
    private FirebaseAuth auth;
    String emailTxt,passwordTxt,nameTxt;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        register = findViewById(R.id.register);

        auth= FirebaseAuth.getInstance();



        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                emailTxt= email.getText().toString();
                passwordTxt= password.getText().toString();
                nameTxt= name.getText().toString();
                if(TextUtils.isEmpty(emailTxt) || TextUtils.isEmpty(passwordTxt)){
                    Toast.makeText(RegisterActivity.this,"Empty Credentials", Toast.LENGTH_SHORT).show();
                }else if(passwordTxt.length()<6){
                    Toast.makeText(RegisterActivity.this,"Password too short", Toast.LENGTH_SHORT).show();
                }else{
                    registerUser(emailTxt,passwordTxt);
                }
            }

        });
    }

    public void registerUser(String email, String password){

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    //ArrayList<String> Trips= new ArrayList<String>();
                    Map<String,Object> user= new HashMap<>();
                    user.put("Name",nameTxt);
                    db.collection("UserData").document(emailTxt).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                            }else{

                            }
                        }
                    });

                    ArrayList<String> Trips= new ArrayList<String>();

                    Toast.makeText(RegisterActivity.this,"Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this , MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(RegisterActivity.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onBackPressed(){
        finishAffinity();
    }

}



