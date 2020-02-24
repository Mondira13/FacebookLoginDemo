package com.java.facebooklogindemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ImageView profPic;
    private TextView mName;
    private LoginButton loginButton;

    private static final String EMAIL = "email";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profPic = findViewById(R.id.profPic);
        mName = findViewById(R.id.name);
        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Collections.singletonList(EMAIL));

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, mFBSignInCallback);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    profPic.setVisibility(View.VISIBLE);
                    mName.setVisibility(View.VISIBLE);
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile", "email"));
                }
                else {
                    profPic.setVisibility(View.GONE);
                    mName.setVisibility(View.GONE);
                    LoginManager.getInstance().logOut();
                }

            }
        });

    }


    private FacebookCallback mFBSignInCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            String name = loginResult.getAccessToken().getUserId();
            String url = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?return_ssl_resources=1";
            mName.setText("User Id  :  " + name);
            Glide.with(MainActivity.this).load(url).into(profPic);
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(FacebookException exception) {
            Toast.makeText(MainActivity.this, exception.toString(), Toast.LENGTH_LONG).show();
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
