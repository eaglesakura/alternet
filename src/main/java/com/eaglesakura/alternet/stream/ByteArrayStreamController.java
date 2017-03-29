package com.eaglesakura.alternet.stream;

import com.eaglesakura.alternet.HttpHeader;
import com.eaglesakura.alternet.Result;
import com.eaglesakura.util.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 通信結果を一度ByteArrayに変換するコントローラ
 */
public class ByteArrayStreamController implements StreamProxy {

    @Override
    public <T> InputStream wrap(Result<T> connection, HttpHeader respHeader, InputStream originalStream) throws IOException {
        byte[] buffer = IOUtil.toByteArray(originalStream, false);
        return new ByteArrayInputStream(buffer);
    }
}
