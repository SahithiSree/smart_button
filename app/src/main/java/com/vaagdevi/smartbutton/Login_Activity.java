package com.vaagdevi.smartbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    FirebaseAuth fAuth;
    EditText editmaillogin,editpasswordlogin;
    Button login,signup;
   // com.google.firebase.auth.FirebaseAuth FirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        Button clear = (Button) findViewById(R.id.clear);
        final Button exit = (Button) findViewById(R.id.exit);
        signup = (Button) findViewById(R.id.signup);
        fAuth=FirebaseAuth.getInstance();
        login = (Button) findViewById(R.id.login);

         editmaillogin = (EditText) findViewById(R.id.editmaillogin);
         editpasswordlogin = (EditText) findViewById(R.id.editpasswordlogin);
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    Toast.makeText(Login_Activity.this, "User logged in ", Toast.LENGTH_SHORT).show();
//                    Intent I = new Intent(Login_Activity.this,Home.class);
//                    startActivity(I);
//                } else {
//                    Toast.makeText(Login_Activity.this, "Login to continue", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(Login_Activity.this,Register_Activity.class);
                startActivity(I);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editmaillogin.getText().toString().trim();
                String password =editpasswordlogin.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    editmaillogin.setError("Email is Required.");
                    return;

                } else if (TextUtils.isEmpty(password)) {
                    editpasswordlogin.setError("password is required.");
                    return;
                } else if (password.length() < 6) {
                    editmaillogin.setError("password must be <= 6 Characters");
                    return;
                } else {
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login_Activity.this, "Logged in.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Home.class));
                            } else {
                                Toast.makeText(Login_Activity.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }
            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editmaillogin.setText("");
                editpasswordlogin.setText("");

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        fAuth.addAuthStateListener(authStateListener);
//    }


}