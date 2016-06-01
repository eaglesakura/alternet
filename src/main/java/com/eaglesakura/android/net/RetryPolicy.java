package com.eaglesakura.android.net;

import android.support.annotation.FloatRange;

/**
 * リトライ設定
 */
public class RetryPolicy {
    int retryNum = 3;

    float backoff = 1.25f;

    long baseWaitTime = 1000;

    public RetryPolicy(int retryNum) {
        this.retryNum = retryNum;
    }

    public RetryPolicy(int retryNum, float backoff, long baseWaitTime) {
        this.retryNum = retryNum;
        this.backoff = backoff;
        this.baseWaitTime = baseWaitTime;
    }

    public int getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(int retryNum) {
        if (retryNum < 0) {
            throw new IllegalArgumentException();
        }
        this.retryNum = retryNum;
    }

    public float getBackoff() {
        return backoff;
    }

    public void setBackoff(@FloatRange(from = 1.0) float backoff) {
        if (backoff < 1.0f) {
            throw new IllegalArgumentException();
        }
        this.backoff = backoff;
    }

    public long getBaseWaitTime() {
        return baseWaitTime;
    }

    public void setBaseWaitTime(long baseWaitTime) {
        if (baseWaitTime <= 0) {
            throw new IllegalArgumentException();
        }
        this.baseWaitTime = baseWaitTime;
    }

    /**
     * 次のバックオフ時間を取得する
     */
    public long nextBackoffTimeMs(int retryNum, long currentBackoff) {
        float result = (float) currentBackoff;
        for (int i = 0; i < retryNum; ++i) {
            result *= backoff;
        }
        return (long) result;
    }
}
