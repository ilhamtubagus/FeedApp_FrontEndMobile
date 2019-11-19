package com.example.feedapp.fragment;

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
import androidx.navigation.Navigator;

import com.example.feedapp.R;
import com.example.feedapp.rest.ApiClient;
import com.example.feedapp.rest.Auth;
import com.example.feedapp.rest.DataMessage;
import com.example.feedapp.rest.ResponseMessage;
import com.example.feedapp.utils.APIErrorUtils;
import com.example.feedapp.utils.FormUtils;
import com.google.android.material.snackbar.Snackbar;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Max;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Or;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends Fragment implements Validator.ValidationListener {
    private final String TAG_LOGIN = "tag_login_fragment";
    private final Auth authService = ApiClient.getClient().create(Auth.class);
    @BindView(R.id.et_email)
    @NotEmpty
    @Email
    EditText editTextEmail;

    @BindView(R.id.et_name)
    @NotEmpty
    EditText editTextName;

    @BindView(R.id.et_password)
    @NotEmpty
    @Password(min = 8, message = "Password must be at least 8 characters")
    EditText editTextPassword;

    @BindView(R.id.et_confirmPassword)
    @NotEmpty
    @ConfirmPassword(message = "Password confirmation is incorrect")
    EditText editTextConfirmPassword;

    @BindView(R.id.btn_signUp)
    Button buttonSignUp;
    @BindView(R.id.tv_login)
    TextView textViewLogin;
    @BindView(R.id.pb_signUp)
    ProgressBar progressBarSignUp;
    private Validator validator;
    private View v;
    private Snackbar snackbar;
    private View.OnClickListener signUpClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FormUtils.closeKeyboard(getContext(), getActivity());
            validator.validate();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, v);

        validator = new Validator(this);
        validator.setValidationListener(this);

        SpannableString spannableString = new SpannableString(textViewLogin.getText());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                fragmentTransaction();
            }
        };
        spannableString.setSpan(clickableSpan, spannableString.length() - 5, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewLogin.setText(spannableString);
        textViewLogin.setMovementMethod(LinkMovementMethod.getInstance());

        buttonSignUp.setOnClickListener(signUpClicked);

        return v;
    }

    private void signUpAction(String email, String name, String password, String confirmPassword) {
        Call<ResponseMessage> call = authService.signUp(email, name, password, confirmPassword);
        FormUtils.onLoadingForm(buttonSignUp, progressBarSignUp, editTextEmail, editTextPassword, editTextName, editTextConfirmPassword);
        call.enqueue(new Callback<ResponseMessage>() {
            @Override
            public void onResponse(Call<ResponseMessage> call, Response<ResponseMessage> response) {
                FormUtils.onFinishedLoadingForm(buttonSignUp, progressBarSignUp, editTextEmail, editTextPassword, editTextName, editTextConfirmPassword);
                if (response.code() == 201) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fragmentTransaction();
                } else {
                    ResponseMessage error = APIErrorUtils.parseError(response);
                    List<DataMessage> dataMessages = error.getData();
                    if (dataMessages.size() > 0) {
                        displayMessage(dataMessages);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseMessage> call, Throwable t) {
                FormUtils.onFinishedLoadingForm(buttonSignUp, progressBarSignUp, editTextEmail, editTextPassword, editTextName, editTextConfirmPassword);
                snackbar = Snackbar.make(v, "Connection failed", Snackbar.LENGTH_INDEFINITE).setAction("Try again", signUpClicked);
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
                case "name":
                    editTextName.setError(dataMessages.get(i).getMessage());
                    break;
                case "passwordconfirmation":
                    editTextConfirmPassword.setError(dataMessages.get(i).getMessage());
                    break;
            }
        }
    }

    @Override
    public void onValidationSucceeded() {
        String email = editTextEmail.getText().toString();
        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        signUpAction(email, name, password, confirmPassword);
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

    @Override
    public void onPause() {
        super.onPause();
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    private void fragmentTransaction(){
        Login login = (Login) getFragmentManager().findFragmentByTag(TAG_LOGIN);
        if (login == null || !login.isInLayout()) {
            getFragmentManager().beginTransaction().replace(R.id.fl_auth, new Login()).commit();
        }
    }
}
