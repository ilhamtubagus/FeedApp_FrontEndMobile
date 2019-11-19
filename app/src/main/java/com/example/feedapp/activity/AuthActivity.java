package com.example.feedapp.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedapp.R;
import com.example.feedapp.fragment.Login;
import com.facebook.stetho.Stetho;

public class AuthActivity extends AppCompatActivity {
    private final String LOGIN_TAG = "Login Tag";
    private final String SIGN_UP_TAG = "Sign Up Tag";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_auth);

        Login login = (Login) getSupportFragmentManager().findFragmentByTag(LOGIN_TAG);
        if (login == null || !login.isInLayout()){
            getSupportFragmentManager().beginTransaction().add(R.id.fl_auth, new Login()).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_auth, new Login()).commit();
        }

    }
}
