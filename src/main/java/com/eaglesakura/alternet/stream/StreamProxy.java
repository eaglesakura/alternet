package com.eaglesakura.alternet.stream;

import com.eaglesakura.alternet.HttpHeader;
import com.eaglesakura.alternet.Result;

import java.io.IOException;
import java.io.InputStream;

/**
 * パーサーに渡すストリームを制御する。
 *
 * 一度ByteArrayに変換する等、必要に応じた内部制御を行う。
 *
 * キャンセル制御はoriginalStreamで行うため、Proxy側で制御する必要はない。
 */
public interface StreamProxy {
    /**
     * パーサーに渡すストリームを生成する。
     * <p>
     * 内部的にByteArrayに変換する等のラップを行う。
     * <p>
     * 内部でoriginalStreamを閉じる必要はない。
     */
    <T> InputStream wrap(Result<T> connection, HttpHeader respHeader, InputStream originalStream) throws IOException;
}
