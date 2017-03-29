package com.eaglesakura.alternet.parser;


import com.eaglesakura.alternet.Result;

import java.io.InputStream;

/**
 * オブジェクトのパースを行う
 * <p>
 * InputStream内でキャンセルチェックを行い、必要に応じて例外を投げるため明示的なcancelチェックを行わない。
 *
 * 入力されたdataのclose処理は外部で行うため、内部で行う必要はない。
 */
public interface RequestParser<T> {
    T parse(Result<T> sender, InputStream data) throws Exception;
}