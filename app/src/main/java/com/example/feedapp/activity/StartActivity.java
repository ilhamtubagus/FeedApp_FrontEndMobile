package com.example.feedapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedapp.R;
import com.example.feedapp.rest.ApiClient;
import com.example.feedapp.rest.Auth;
import com.example.feedapp.rest.LoginResult;
import com.example.feedapp.rest.ResponseMessage;
import com.example.feedapp.utils.APIErrorUtils;
import com.example.feedapp.utils.SessionManager;
import com.facebook.stetho.Stetho;
import com.google.android.material.snackbar.Snackbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {
    private final Auth authService = ApiClient.getClient().create(Auth.class);
    @BindView(R.id.ll_startActivity)
    View parentLayout;
    @BindView(R.id.pb_activity_start)
    ProgressBar progressBar;
    private Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_start);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bootstrap();
    }

    private void bootstrap(){
        progressBar.setVisibility(View.VISIBLE);
        SessionManager sessionManager = new SessionManager(this);
        // Check whether token is exist in shared preferences;
        if (sessionManager.checkStatus()) {
            refreshToken();
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /*
     *  Background process for authentication
     */

    void refreshToken() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        System.out.println(sessionManager.getToken(getApplicationContext()));
        Call<LoginResult> call = authService.refreshToken(sessionManager.getToken(getApplicationContext()));
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    LoginResult loginResult = response.body();
                    Toast.makeText(getApplicationContext(), loginResult.getToken(), Toast.LENGTH_SHORT).show();
                    SessionManager sessionManager = new SessionManager(getApplicationContext());
                    sessionManager.setLoggedIn(getApplicationContext(), loginResult.getToken());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ResponseMessage error = APIErrorUtils.parseError(response);
                    //Toast.makeText(startActivity.getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.code() == 400 && error.getMessage().equalsIgnoreCase("Could not refresh token yet")) {
                        //Toast.makeText(getApplicationContext(), "No need to login again", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //if error code 500 and etc prompt user to login
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                snackbar = Snackbar.make(parentLayout, "Connection failed", Snackbar.LENGTH_INDEFINITE).setAction("Try again", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bootstrap();
                    }
                });
                snackbar.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (snackbar!=null){
            snackbar.dismiss();
        }
    }
}
