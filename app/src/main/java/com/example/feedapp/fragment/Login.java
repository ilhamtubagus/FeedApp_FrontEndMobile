package com.example.feedapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.feedapp.R;
import com.example.feedapp.activity.MainActivity;
import com.example.feedapp.rest.ApiClient;
import com.example.feedapp.rest.Auth;
import com.example.feedapp.rest.DataMessage;
import com.example.feedapp.rest.LoginResult;
import com.example.feedapp.rest.ResponseMessage;
import com.example.feedapp.utils.APIErrorUtils;
import com.example.feedapp.utils.FormUtils;
import com.example.feedapp.utils.SessionManager;
import com.google.android.material.snackbar.Snackbar;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends Fragment implements Validator.ValidationListener {
    private final String TAG_SIGNUP = "tag_signup_fragment";
    private final Auth authService = ApiClient.getClient().create(Auth.class);
    @BindView(R.id.et_email)
    @NotEmpty
    @Email
    EditText editTextEmail;

    @BindView(R.id.et_password)
    @NotEmpty
    @Password(min = 8, message = "Password must be at least 8 characters")
    EditText editTextPassword;

    @BindView(R.id.btn_login)
    Button buttonLogin;
    @BindView(R.id.tv_signUp)
    TextView textViewSignUp;
    @BindView(R.id.pb_login)
    ProgressBar progressBarLogin;
    private Validator validator;
    private View v;
    private Snackbar snackbar;
    private View.OnClickListener loginClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FormUtils.closeKeyboard(getContext(), getActivity());
            validator.validate();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, v);

        validator = new Validator(this);
        validator.setValidationListener(this);

        SpannableString spannableString = new SpannableString(textViewSignUp.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                fragmentTransaction();
            }
        };
        spannableString.setSpan(clickableSpan, spannableString.length() - 7, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewSignUp.setText(spannableString);
        textViewSignUp.setMovementMethod(LinkMovementMethod.getInstance());
        buttonLogin.setOnClickListener(loginClicked);

        return v;
    }

    @Override
    public void onValidationSucceeded() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        loginAction(email, password);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            List Errors = error.getFailedRules();
            Rule rule = (Rule) Errors.get(0);
            String message = rule.getMessage(getContext());

            // Display error messages
            if (view instanceof TextView) {
                ((EditText) view).setError(message);
            }
        }
    }

    private void loginAction(String email, String password) {
        Call<LoginResult> call = authService.login(email, password);
        FormUtils.onLoadingForm(buttonLogin, progressBarLogin, editTextEmail, editTextPassword);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                    LoginResult loginResult = response.body();
                    //Toast.makeText(getContext(), loginResult.getToken(), Toast.LENGTH_SHORT).show();
                    SessionManager sessionManager = new SessionManager(getContext());
                    sessionManager.setLoggedIn(getContext(), loginResult.getToken());
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    ResponseMessage error = APIErrorUtils.parseError(response);
                    List<DataMessage> dataMessages = error.getData();
                    if (dataMessages.size() > 0) {
                        displayMessage(dataMessages);
                    }
                }
                FormUtils.onFinishedLoadingForm(buttonLogin, progressBarLogin, editTextEmail, editTextPassword);
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                FormUtils.onFinishedLoadingForm(buttonLogin, progressBarLogin, editTextEmail, editTextPassword);
                snackbar = Snackbar.make(v, "Connection failed", Snackbar.LENGTH_INDEFINITE).setAction("Try again", loginClicked);
                snackbar.show();
            }
        });
    }

    private void displayMessage(List<DataMessage> dataMessages) {
        for (int i = 0; i < dataMessages.size(); i++) {
            String field = dataMessages.get(i).getField();
            switch (field.toLowerCase()) {
                case "password":
                    editTextPassword.setError(dataMessages.get(i).getMessage());
                    break;
                case "email":
                    editTextEmail.setError(dataMessages.get(i).getMessage());
                    break;
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }
    private void fragmentTransaction(){
        SignUp signUp = (SignUp) getFragmentManager().findFragmentByTag(TAG_SIGNUP);
        if (signUp == null || !signUp.isInLayout()) {
            getFragmentManager().beginTransaction().replace(R.id.fl_auth, new SignUp()).commit();
        }
    }
}
