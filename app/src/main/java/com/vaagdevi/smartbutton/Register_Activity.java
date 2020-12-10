package com.vaagdevi.smartbutton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register_Activity extends AppCompatActivity {
    EditText editname,editemail,editpassword;
    Button Register;
    TextView loginbtn;
    FirebaseAuth fAuth;
    DatabaseReference dref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
         editname=(EditText)findViewById(R.id.editname);
        editemail=(EditText)findViewById(R.id.editmail);
        editpassword=(EditText)findViewById(R.id.editpassword);
         Register=(Button)findViewById(R.id.Register);
        fAuth=FirebaseAuth.getInstance();
        dref = FirebaseDatabase.getInstance().getReference("Users");

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=editemail.getText().toString().trim();
                String password=editpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    editemail.setError("Email is Required.");
                    return;

                }
               else if(TextUtils.isEmpty(password)){
                    editpassword.setError("password is required.");
                    return;
                }
              else  if(password.length() <6){
                    editpassword.setError("password must be >= 6 Characters");
                    return;

                }
                else {
                    fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = fAuth.getCurrentUser();
                                finish();
                                HashMap<String, Object> Usermap = new HashMap<>();
                                Usermap.put("name",editname.getText().toString());
                                Usermap.put("email",editemail.getText().toString());
                                Usermap.put("password", editpassword.getText().toString());
                                Usermap.put("uuid", user.getUid());
                                Usermap.put("username", "sahithi");
                                Usermap.put("link","");
                                Usermap.put("latitude","");
                                Usermap.put("longitude","");
                                dref.child(user.getUid()).updateChildren(Usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Register_Activity.this, "Welcome User", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Register_Activity.this, Home.class));
                                        finish();
                                    }
                                });

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register_Activity.this, "Authentication failed",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(Register_Activity.this, Home.class));
            finishAffinity();
        }
    }


//                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(Register_Activity.this, "User Created.", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(getApplicationContext(),Home.class));
//                        }else{
//                            Toast.makeText(Register_Activity.this,"Error!"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });



    public void onLoginClick(View view) {
        startActivity(new Intent(Register_Activity.this, Login_Activity.class));
    }
}