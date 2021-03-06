package com.eaglesakura.alternet.internal;

import com.eaglesakura.alternet.cache.ICacheWriter;
import com.eaglesakura.io.CancelableInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * ネットワーク用StreamをラップするためのInputStream
 */
public class NetworkParseInputStream extends DigestInputStream {
    /**
     * 一度に読み込みを許可する最大容量。
     * キャンセルを細かい単位で行えるように、ある程度以上は一度に読み込めないようにする。
     */
    private static final int MAX_READ_BYTES = 1024 * 4;
    private final ICacheWriter mCacheWriter;

    private final NetworkProfileImpl mProfile;

    public NetworkParseInputStream(InputStream stream, NetworkProfileImpl profile, ICacheWriter cacheWriter, MessageDigest digest, CallbackHolder callback) {
        super(new CancelableInputStream(stream, () -> callback.isCanceled()), digest);
        mCacheWriter = cacheWriter;
        mProfile = profile;
    }

    private void onDownloadStep(byte[] buffer, int offset, int length) throws IOException {
        mProfile.onDownloadStep(length);

        if (mCacheWriter == null || length <= 0) {
            return;
        } else {
            mCacheWriter.write(buffer, offset, length);
        }
    }

    @Override
    public int read() throws IOException {
        byte[] buf = new byte[1];
        if (read(buf, 0, 1) < 0) {
            return -1;
        }
        return ((int) buf[0]) & 0x000000FF;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        // キャンセルチェックを容易にするため、一度の取得を小さく保つ
        byteCount = Math.min(MAX_READ_BYTES, byteCount);

        int result = super.read(buffer, byteOffset, byteCount);
        onDownloadStep(buffer, byteOffset, result);
        return result;
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long skip(long byteCount) throws IOException {
        if (byteCount < 0) {
            throw new UnsupportedOperationException();
        }

        byte[] temp = new byte[Math.min((int) byteCount, MAX_READ_BYTES)];
        int count;
        int sumSkip = 0;
        // 指定量を読み込むことでスキップ扱いとする
        while ((count = read(temp, 0, Math.min(temp.length, (int) byteCount - sumSkip))) > 0 && (sumSkip < byteCount)) {
            onDownloadStep(temp, 0, count);
            sumSkip += count;
        }
        return sumSkip;
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean markSupported() {
        return false;
    }
}
