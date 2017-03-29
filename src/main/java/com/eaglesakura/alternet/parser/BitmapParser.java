package com.eaglesakura.alternet.parser;

import com.eaglesakura.alternet.Result;
import com.eaglesakura.android.util.ImageUtil;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * 取得したデータをBitmap化するParser
 */
public class BitmapParser implements RequestParser<Bitmap> {
    int maxWidth = 0;
    int maxHeight = 0;

    public BitmapParser() {
    }

    public BitmapParser(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    @Override
    public Bitmap parse(Result<Bitmap> sender, InputStream data) throws Exception {
        Bitmap bitmap = ImageUtil.decode(data);
        if (maxWidth > 0 && maxHeight > 0) {
            Bitmap scaled = ImageUtil.toScaledImage(bitmap, maxWidth, maxHeight);
            if (bitmap != scaled) {
                bitmap.recycle();
            }
            return scaled;
        } else {
            return bitmap;
        }
    }
}
