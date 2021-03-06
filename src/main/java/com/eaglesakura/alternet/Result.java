package com.eaglesakura.alternet;

import com.eaglesakura.alternet.request.ConnectRequest;
import com.eaglesakura.util.StringUtil;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class Result<T> {
    /**
     * キャッシュの指紋を取得する
     *
     * キャッシュから読み込まれた場合に有効となる。
     */
    @Nullable
    public abstract String getCacheDigest();

    /**
     * コンテンツの指紋を取得する
     *
     * ダウンロードが実行された場合に有効となる
     */
    @Nullable
    public abstract String getContentDigest();

    /**
     * リクエスト情報を取得する
     */
    @NonNull
    public abstract ConnectRequest getRequest();

    /**
     * 戻り値のヘッダ情報
     * 誤字のため、getResponseHeader()を使うべき。
     */
    @Nullable
    @Deprecated
    public HttpHeader getResponceHeader() {
        return getResponseHeader();
    }

    /**
     * 戻り値のヘッダ情報
     */
    @Nullable
    public abstract HttpHeader getResponseHeader();

    /**
     * データが前回取得時と比較して更新されている場合trueを返却する。
     * 初回取得時は必ずtrueとなる
     */
    public abstract boolean isModified();

    /**
     * parseされた戻り値を取得する
     */
    public abstract T getResult();


    /**
     * 現在のネットワーク状態を取得する
     */
    @NonNull
    public abstract NetworkProfile getProfile();

    /**
     * キャッシュを取得済みであればtrue
     */
    public boolean hasCache() {
        return !StringUtil.isEmpty(getCacheDigest());
    }

    /**
     * コンテンツを何らかの手段で取得済みであればtrue
     *
     * キャッシュロードした場合もtrueを返却する。
     */
    public boolean hasContent() {
        return getContentDigest() != null || getCacheDigest() != null;
    }
}
