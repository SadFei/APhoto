package com.trevor.photoalbum;

import java.io.File;

/**
 *
 * @author DAIFEI
 * @date 2018/3/7
 */
public interface ActivityListener {

    /**
     * type
     * @return type
     */
    int getType();

    /**
     * file
     * @param file file
     */
    void onFile(File file);
}
