package com.egabi.core.di;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


public class UploadInterceptor implements Interceptor {
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body()))
                .build();

    }
}