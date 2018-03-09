package com.trevor.photoalbum.source;

import android.content.Context;

/**
 *
 * @author DAIFEI
 * @date 2018/3/7
 */
public class Source {

    private Context context;

    public Source(Context context) {
        this.context = context;
    }

    public Context getContext(){
        return context;
    }
}
