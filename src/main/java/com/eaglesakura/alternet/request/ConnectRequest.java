package com.eaglesakura.alternet.request;

import com.eaglesakura.alternet.ErrorPolicy;
import com.eaglesakura.alternet.HttpHeader;
import com.eaglesakura.alternet.RetryPolicy;
import com.eaglesakura.alternet.cache.CachePolicy;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class ConnectRequest {
    @Keep
    public enum Method {
        GET {
            @Override
            public String toString() {
                return "GET";
            }
        },
        POST {
            @Override
            public String toString() {
                return "POST";
            }
        },
        HEAD {
            @Override
            public String toString() {
                return "HEAD";
            }
        },
        DELETE {
            @Override
            public String toString() {
                return "DELETE";
            }
        },
        PUT {
            @Override
            public String toString() {
                return "PUT";
            }
        },
        PATCH {
            @Override
            public String toString() {
                return "PATCH";
            }
        },
        OPTIONS {
            @Override
            public String toString() {
                return "OPTIONS";
            }
        },
        PULL {
            @Override
            public String toString() {
                return "PULL";
            }
        };
    }

    private final Method method;

    protected String url;

    protected HttpHeader header = new HttpHeader();

    /**
     * 通信タイムアウト時間を指定する
     */
    private long readTimeoutMs = 1000 * 10;

    /**
     * 接続タイムアウト時間を指定する
     */
    private long connectTimeoutMs = 1000 * 10;

    protected ConnectRequest(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    /**
     * 通信時のヘッダを取得する
     */
    public HttpHeader getHeader() {
        return header;
    }

    /**
     * タイムアウト時間を指定する
     */
    public void setReadTimeoutMs(long readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public long getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public long getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    public void setConnectTimeoutMs(long connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    /**
     * キャッシュ制御を取得する
     * nullを返却した場合、キャッシュ制御を行わない
     */
    @NonNull
    public abstract CachePolicy getCachePolicy();

    /**
     * リトライ制御を取得する
     * nullを返却した場合、リトライ制御を行わない。
     */
    @NonNull
    public abstract RetryPolicy getRetryPolicy();

    /**
     * エラーハンドル制御を取得する
     */
    @NonNull
    public abstract ErrorPolicy getErrorPolicy();

    /**
     * POST時のBodyを取得する
     * <p/>
     * nullを返却した場合、POST時に何もデータを付与しない。
     */
    @Nullable
    public abstract ConnectContent getContent();
}
