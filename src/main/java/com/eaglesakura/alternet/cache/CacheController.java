package com.eaglesakura.alternet.cache;

import com.eaglesakura.alternet.HttpHeader;
import com.eaglesakura.alternet.request.ConnectRequest;

import java.io.IOException;
import java.io.InputStream;

public interface CacheController {

    long CACHE_ONE_MINUTE = 1000 * 60;
    long CACHE_ONE_HOUR = CACHE_ONE_MINUTE * 60;
    long CACHE_ONE_DAY = CACHE_ONE_HOUR * 24;
    long CACHE_ONE_WEEK = CACHE_ONE_DAY * 7;
    long CACHE_ONE_MONTH = CACHE_ONE_WEEK * 4;
    long CACHE_ONE_YEAR = CACHE_ONE_DAY * 365;

    /**
     * キャッシュ登録を行うためのインターフェースを取得する。
     * <p/>
     * 不正な場合は例外を投げる。
     */
    ICacheWriter newCacheWriter(ConnectRequest request, HttpHeader respHeader) throws IOException;

    /**
     * 既存のキャッシュを開く。
     * <p/>
     * キャッシュがない場合、FileNotFoundExceptionを投げる。
     */
    InputStream openCache(ConnectRequest request) throws IOException;
}
