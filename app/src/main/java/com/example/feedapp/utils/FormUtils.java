package com.example.feedapp.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class FormUtils {
    public static void onLoadingForm(Button button, ProgressBar progressBar, EditText... editTexts){
        button.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        for (EditText editText:editTexts) {
            editText.setEnabled(false);
            editText.clearFocus();
        }
    }

    public static void onFinishedLoadingForm(Button button, ProgressBar progressBar, EditText... editTexts){
        button.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
        for (EditText editText: editTexts) {
            editText.setEnabled(true);
        }
    }
    public static void closeKeyboard(Context context, Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
