package com.eaglesakura.alternet;

import com.eaglesakura.alternet.cache.CacheController;
import com.eaglesakura.alternet.cache.file.FileCacheController;
import com.eaglesakura.alternet.cache.tkvs.TextCacheController;
import com.eaglesakura.alternet.internal.AndroidHttpClientResultImpl;
import com.eaglesakura.alternet.internal.HttpResult;
import com.eaglesakura.alternet.internal.CallbackHolder;
import com.eaglesakura.alternet.parser.RequestParser;
import com.eaglesakura.alternet.request.ConnectRequest;
import com.eaglesakura.alternet.stream.ByteArrayStreamController;
import com.eaglesakura.alternet.stream.StreamProxy;
import com.eaglesakura.alternet.stream.RawStreamController;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * ネットワークの接続制御を行う
 */
public class Alternet {
    private final Context mContext;

    private StreamProxy mStreamProxy;

    private CacheController mCacheController;

    public Alternet(Context context) {
        mContext = context.getApplicationContext();
        mStreamProxy = new RawStreamController();
    }

    public Context getContext() {
        return mContext;
    }

    public void setStreamProxy(StreamProxy proxy) {
        this.mStreamProxy = proxy;
    }

    public StreamProxy getStreamProxy() {
        return mStreamProxy;
    }

    public void setCacheController(CacheController cacheController) {
        this.mCacheController = cacheController;
    }

    public CacheController getCacheController() {
        return mCacheController;
    }

    /**
     * テキストのREST APIを利用するコネクタを生成する
     */
    public static Alternet newRestfulInstance(Context context) {
        Alternet result = new Alternet(context);
        TextCacheController cacheController = new TextCacheController(context);
        cacheController.setEncodeBase64(false);

        result.setCacheController(cacheController);
        result.setStreamProxy(new ByteArrayStreamController());
        return result;
    }

    /**
     * 巨大なバイナリを取得するコネクタを生成する
     */
    public static Alternet newLargeBinaryInstance(Context context, File cacheDir) throws IOException {
        Alternet result = new Alternet(context);
        result.setCacheController(new FileCacheController(cacheDir));
        result.setStreamProxy(new RawStreamController());
        return result;
    }

    /**
     * ネットワーク接続クラスを取得する。
     * 戻り値を取得した時点で、すでにネットワーク接続は終えている。
     *
     * @param request 通信リクエスト
     * @param parser  通信パーサ
     * @param <T>     戻り値の型
     * @return 実行タスク
     */
    public <T> Result<T> fetch(ConnectRequest request, RequestParser<T> parser, CancelCallback<T> cancelCallback) throws IOException {
        final HttpResult<T> connection = new AndroidHttpClientResultImpl<>(this, request, parser);
        CallbackHolder<T> holder = new CallbackHolder<>(cancelCallback, connection);
        connection.fetch(holder);
        return connection;
    }

    public interface CancelCallback<T> {
        /**
         * タスクをキャンセルさせる場合はtrue
         */
        boolean isCanceled(Result<T> connection);
    }
}
