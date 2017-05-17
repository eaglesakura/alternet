package com.eaglesakura.alternet.request;

import com.eaglesakura.alternet.ErrorPolicy;
import com.eaglesakura.alternet.RetryPolicy;
import com.eaglesakura.alternet.cache.CachePolicy;
import com.eaglesakura.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SimpleHttpRequest extends ConnectRequest {
    private Map<String, String> mParams = new HashMap<>();

    private String mEncoding = "UTF-8";

    private CachePolicy mCachePolicy = new CachePolicy();

    private RetryPolicy mRetryPolicy = new RetryPolicy(10);

    private ErrorPolicy mErrorPolicy = new ErrorPolicy();

    public SimpleHttpRequest(Method method) {
        super(method);
    }

    public void setUrl(String url, Map<String, String> params) {
        this.url = url;
        if (params != null) {
            this.mParams = params;
        }
    }

    private String encodeParams() {
        if (mParams == null || mParams.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = mParams.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            try {
                result.append(URLEncoder.encode(entry.getKey(), mEncoding));
                result.append('=');
                result.append(URLEncoder.encode(entry.getValue(), mEncoding));
                result.append('&');
            } catch (Exception e) {

            }
        }
        return result.toString();
    }

    @Override
    public String getUrl() {
        String params = encodeParams();
        if (StringUtil.isEmpty(params)) {
            // パラメータが無いのでそのまま返す
            return this.url;
        }

        // URLにパラメータを乗せる
        StringBuilder result = new StringBuilder(this.url);
        if (url.indexOf("?") < 0) {
            result.append('?');
        } else {
            result.append('&');
        }
        result.append(params);
        return result.toString();
    }

    @Override
    public CachePolicy getCachePolicy() {
        return mCachePolicy;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }

    public void setRetryPolicy(RetryPolicy retryPolicy) {
        mRetryPolicy = retryPolicy;
    }

    public void setErrorPolicy(ErrorPolicy errorPolicy) {
        mErrorPolicy = errorPolicy;
    }

    @Override
    public ErrorPolicy getErrorPolicy() {
        return mErrorPolicy;
    }

    @Override
    public ConnectContent getContent() {
        String paramString = encodeParams();
        if (StringUtil.isEmpty(paramString)) {
            return null;
        }

        final byte[] paramStringBytes = paramString.getBytes();
        return new ConnectContent() {
            @Override
            public long getLength() {
                return paramStringBytes.length;
            }

            @Override
            public InputStream openStream() throws IOException {
                return new ByteArrayInputStream(paramStringBytes);
            }

            @Override
            public String getContentType() {
                return "application/x-www-form-urlencoded; charset=" + mEncoding;
            }
        };
    }
}

