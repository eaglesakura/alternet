package com.eaglesakura.alternet.stream;

import com.eaglesakura.alternet.HttpHeader;
import com.eaglesakura.alternet.Result;

import java.io.IOException;
import java.io.InputStream;

public class RawStreamController implements StreamProxy {
    @Override
    public <T> InputStream wrap(Result<T> connection, HttpHeader respHeader, InputStream originalStream) throws IOException {
        return originalStream;
    }
}
