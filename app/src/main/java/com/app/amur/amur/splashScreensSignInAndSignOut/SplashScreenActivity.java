package com.app.amur.amur.splashScreensSignInAndSignOut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.amur.amur.MainActivity;
import com.app.amur.amur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Digriz on 05.12.2017.
 */

public class SplashScreenActivity extends AppCompatActivity{


    EditText editTextEmail;
    EditText editTextPassword;
    Button btnRegister;
    ProgressBar progressBar;
    TextView textAlreadyRemember;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );



        setContentView(R.layout.splash_screen_registration);
         editTextEmail = (EditText)findViewById(R.id.editTextEmail);
         editTextPassword = (EditText)findViewById(R.id.editTextPassword);
         btnRegister = (Button) findViewById(R.id.btnRegistration) ;
         progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textAlreadyRemember = (TextView)findViewById(R.id.textAlreadyRemember);
        Window window = this.getWindow();


       progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Подождите...");


// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccentTwo));

        mAuth = FirebaseAuth.getInstance();
         final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        textAlreadyRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Введите ваш E-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Введите ваш пароль", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Пароль должен иметь не менее 6 символов", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                //create user
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SplashScreenActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SplashScreenActivity.this, "Аккаунт создан", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SplashScreenActivity.this, "Аккаунт не создан! " + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressDialog.dismiss();

    }




}
