package com.app.amur.amur.splashScreensSignInAndSignOut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

/**
 * Created by Digriz on 05.12.2017.
 */

public class SplashScreenEnterActivity extends AppCompatActivity  {


    EditText editTextEmail;
    EditText editTextPassword;
    Button btnEnter;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    TextView registration;
    TextView sendPassword;
    ProgressDialog progressDialog;
    ScalableVideoView mVideoView;
    ScalableType scalableType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        setContentView(R.layout.splash_screen_enter);
         editTextEmail = (EditText)findViewById(R.id.editTextEmail);
         editTextPassword = (EditText)findViewById(R.id.editTextPassword);
         btnEnter = (Button) findViewById(R.id.btnEnter) ;
         progressBar = (ProgressBar) findViewById(R.id.progressBar);
        registration = (TextView) findViewById(R.id.textRegistration);
        sendPassword = (TextView) findViewById(R.id.textSendPassword);
        Window window = this.getWindow();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Подождите...");

        mVideoView = (ScalableVideoView) findViewById(R.id.video_view);
        try {
            mVideoView.setRawData(R.raw.video);
            mVideoView.setLooping(true);
            scalableType = ScalableType.FIT_XY;
            mVideoView.setScalableType(scalableType);
            mVideoView.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                }
            });
        } catch (IOException ioe) {
            //handle error
        }


// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
      //  window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccentTwo));

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SplashScreenEnterActivity.this, MainActivity.class));
            finish();
        }
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenEnterActivity.this, SplashScreenActivity.class));

            }
        });
        sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashScreenEnterActivity.this, SplashScreenSendPasswordActivity.class));

            }
        });
        btnEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Введите ваш E-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Введите ваш пароль", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SplashScreenEnterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressDialog.dismiss();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        editTextPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(SplashScreenEnterActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(SplashScreenEnterActivity.this, MainActivity.class);
                                    startActivity(intent);
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