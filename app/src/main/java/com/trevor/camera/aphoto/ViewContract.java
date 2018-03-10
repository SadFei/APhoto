package com.trevor.camera.aphoto;

import android.support.annotation.IdRes;

/**
 *
 * @author DAIFEI
 * @date 2018/3/6
 */
public interface ViewContract {

    interface ActivityPresenter {

        /**
         *  检查权限
         *
         * @param id id res
         * @param permissions permission
         */
        void requestPermission(@IdRes int id, String... permissions);
    }

}
