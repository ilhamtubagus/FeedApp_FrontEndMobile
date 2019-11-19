package com.example.feedapp.utils;

import com.example.feedapp.rest.ApiClient;
import com.example.feedapp.rest.ResponseMessage;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class APIErrorUtils {
    public static ResponseMessage parseError(Response<?> response) {
        Converter<ResponseBody, ResponseMessage> converter =
                ApiClient.getClient()
                        .responseBodyConverter(ResponseMessage.class, new Annotation[0]);

        ResponseMessage error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ResponseMessage();
        }

        return error;
    }
}
