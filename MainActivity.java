package com.example.niceweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {

    private ImageView MainLogo;
    private LoginButton FacebookLoginBtn;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainLogo = findViewById(R.id.imageView);
        FacebookLoginBtn = findViewById(R.id.login_facebook);
        callbackManager = CallbackManager.Factory.create();

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, UIActivity.class);
            startActivity(intent);
        }
        FacebookLoginBtn.setOnClickListener(v -> {
            Collection<String> permissionNeeds= Arrays.asList("email");
            LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, permissionNeeds);
        });

        FacebookLoginBtn.setPermissions(Arrays.asList("email", "user_birthday"));
        FacebookLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Intent intent = new Intent(MainActivity.this, UIActivity.class);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("Zaporizhia");

                myRef.child("niceweather-805e8-default-rtdb").setValue(AccessToken.getCurrentAccessToken());
                ;
                startActivity(intent);
            }

            @Override
            public void onCancel() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Вы отменили авторизацию.", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "При авторизации произошла ошибка: " + error.toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
