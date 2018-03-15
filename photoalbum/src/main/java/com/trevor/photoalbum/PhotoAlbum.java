package com.trevor.photoalbum;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.trevor.photoalbum.machine.MachineEntity;
import com.trevor.photoalbum.result.ActionResult;
import com.trevor.photoalbum.source.Source;

/**
 * @author DAIFEI
 * @date 2018/3/6
 */
public class PhotoAlbum {

    private static final MachineFactory MACHINE;

    static {
        MACHINE = new MachineProxy();
    }

    public static Machine with(@NonNull Activity activity) {
        return MACHINE.create(new Source(activity));
    }

    public static Machine with(@NonNull Fragment fragment) {
        return MACHINE.create(new Source(fragment.getActivity()));
    }

    public static Machine with(@NonNull android.support.v4.app.Fragment fragment) {
        return MACHINE.create(new Source(fragment.getContext()));
    }

    public static Machine with(@NonNull Context context) {
        return MACHINE.create(new Source(context));
    }


    public interface MachineFactory {
        /**
         * 创建 MachineEntity
         *
         * @param source {@link Source}
         * @return {@link MachineEntity}
         */
        Machine create(Source source);
    }


    /**
     * Machine
     */
    public interface Machine {
        /**
         * 打开相机
         *
         * @return {@link MachineEntity}
         */
        Machine openCamera();

        /**
         * 打开相册
         *
         * @return {@link MachineEntity}
         */
        Machine openPhotoAlbum();

        /**
         * 设置压缩格式; 不设置，默认为 {@link Bitmap.CompressFormat JPEG}
         *
         * @param compressFormat {@link Bitmap.CompressFormat}
         * @return {@link MachineEntity}
         */
        Machine onCompressFormat(@NonNull Bitmap.CompressFormat compressFormat);

        /**
         * 返回结果
         *
         * @param action {@link ActionResult}
         * @return {@link MachineEntity}
         */
        Machine onResult(ActionResult action);

        /**
         * 开始
         */
        void start();
    }

    /**
     * 机器代理
     */
    private static class MachineProxy implements MachineFactory {
        @Override
        public Machine create(Source source) {
            return new MachineEntity(source);
        }
    }
}
