package com.eaglesakura.alternet.internal;

import com.eaglesakura.alternet.Result;
import com.eaglesakura.alternet.Alternet;

public class CallbackHolder<T> {
    public final Alternet.CancelCallback<T> mCancelCallback;

    public final Result<T> mConnection;

    public CallbackHolder(Alternet.CancelCallback<T> cancelCallback, Result<T> mConnection) {
        this.mCancelCallback = cancelCallback;
        this.mConnection = mConnection;
    }

    public boolean isCanceled() {
        if (mCancelCallback == null) {
            return false;
        }

        return mCancelCallback.isCanceled(mConnection);
    }
}
