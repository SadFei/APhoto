package com.trevor.photoalbum;

import android.graphics.Bitmap;

import java.io.File;

/**
 * @author DAIFEI
 * @date 2018/3/7
 */
public interface ActivityListener {

    /**
     * type
     *
     * @return type
     */
    int getType();

    /**
     * CompressFormat
     *
     * @return {@link Bitmap.CompressFormat } 有可能为 null
     */
    Bitmap.CompressFormat getCompressFormat();

    /**
     * file
     *
     * @param file file
     */
    void onFile(File file);
}
