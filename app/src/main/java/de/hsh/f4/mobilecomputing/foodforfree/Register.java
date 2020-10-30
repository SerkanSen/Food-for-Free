package de.hsh.f4.mobilecomputing.foodforfree;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText mName,mEMail,mPasswort,mPasswort1;
    Button mRegistrierenBtn;
    TextView mLoginBtn;
    FirebaseAuth fireAuth;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName =findViewById(R.id.editName);
        mEMail=findViewById(R.id.email);
        mPasswort=findViewById(R.id.passwort);
        mPasswort1=findViewById(R.id.editPasswortKontrolle);
        mRegistrierenBtn =findViewById(R.id.loginBtn);
        mLoginBtn= findViewById(R.id.anmeldenTextView);

        fireAuth =FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fireAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        mRegistrierenBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = mEMail.getText().toString().trim();
                String passwort= mPasswort.getText().toString().trim();
                String passwort1= mPasswort1.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEMail.setError("Email wird benötigt.");
                    return;
            }
                if(TextUtils.isEmpty(passwort)){
                    mPasswort.setError("Passwort wird benötigt.");
                    return;
                }
                if (passwort.length() < 6) {
                    mPasswort.setError("Passwort muss 6 Zeichen lang sein");
                    return;
                }
                if (!passwort.equals(passwort1)){
                    mPasswort1.setError("Die Passwörter stimmen nicht überein.");
                     return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //registrieren

                fireAuth.createUserWithEmailAndPassword(email,passwort).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Neues Konto wurde erstellt.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(Register.this, "Fehler!"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }

        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


    }
}