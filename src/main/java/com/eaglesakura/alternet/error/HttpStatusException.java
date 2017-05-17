package com.eaglesakura.alternet.error;

import com.eaglesakura.alternet.internal.CallbackHolder;
import com.eaglesakura.alternet.request.ConnectRequest;
import com.eaglesakura.io.CancelableInputStream;
import com.eaglesakura.json.JSON;

import android.annotation.SuppressLint;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class HttpStatusException extends IOException {
    final int mStatusCode;

    byte[] mErrorBuffer;

    public HttpStatusException(String detailMessage, int statusCode) {
        super(detailMessage);
        mStatusCode = statusCode;
    }

    public HttpStatusException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        mStatusCode = statusCode;
    }

    public HttpStatusException(Throwable cause, int statusCode) {
        super(cause);
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

    public boolean hasErrorBuffer() {
        return mErrorBuffer != null;
    }

    public byte[] getErrorBuffer() {
        return mErrorBuffer;
    }

    public String getErrorText() {
        return new String(mErrorBuffer);
    }

    public <T> T getErrorJson(Class<T> clazz) throws IOException {
        return JSON.decode(new ByteArrayInputStream(mErrorBuffer), clazz);
    }

    @SuppressLint("NewApi")
    public HttpStatusException setErrorResponse(HttpURLConnection connection, ConnectRequest request, CallbackHolder holder) throws IOException {
        if (request.getErrorPolicy() == null || !request.getErrorPolicy().isHandleErrorStream()) {
            return this;
        }

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try (InputStream is = new CancelableInputStream(connection.getErrorStream(), () -> holder.isCanceled())) {
            // バッファを全て読み取る
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        }

        mErrorBuffer = os.toByteArray();
        return this;
    }
}
