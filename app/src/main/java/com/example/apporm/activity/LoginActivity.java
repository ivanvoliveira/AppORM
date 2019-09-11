package com.example.apporm.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.apporm.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager callbackManager;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginButton buttonFacebook = findViewById(R.id.button_facebook);

        auth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();
        buttonFacebook.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAcessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginVerify();
    }

    private void handleFacebookAcessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            finish();
                            Log.d("FacebookAuth: ", "signInWithCredential: success");
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } else {
                            Log.w("FacebookAuth: ", "signInWithCredential: fail", task.getException());
                            Toast.makeText(LoginActivity.this, "Falha na autenticação", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginVerify(){
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
