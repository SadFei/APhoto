package com.trevor.photoalbum.machine;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.trevor.photoalbum.ActivityListener;
import com.trevor.photoalbum.PhotoAlbum;
import com.trevor.photoalbum.PhotoAlbumActivity;
import com.trevor.photoalbum.result.ActionResult;
import com.trevor.photoalbum.source.Source;

import java.io.File;

/**
 * @author DAIFEI
 * @date 2018/3/7
 */
public class MachineEntity implements PhotoAlbum.Machine, ActivityListener {

    /**
     * 相机
     */
    public static final int CAMERA = 0X521;
    /**
     * 相册
     */
    public static final int PHOTO_ALBUM = 0X522;

    /**
     * 相机 or 相册
     */
    private int mType = -1;

    /**
     * 压缩格式
     */
    private Bitmap.CompressFormat mCompressFormat = null;

    private Source source;

    private ActionResult result;

    public MachineEntity(Source source) {
        this.source = source;
    }

    @Override
    public PhotoAlbum.Machine openCamera() {
        mType = CAMERA;
        return this;
    }

    @Override
    public PhotoAlbum.Machine openPhotoAlbum() {
        mType = PHOTO_ALBUM;
        return this;
    }

    @Override
    public PhotoAlbum.Machine onCompressFormat(@NonNull Bitmap.CompressFormat compressFormat) {
        mCompressFormat = compressFormat;
        return this;
    }

    @Override
    public PhotoAlbum.Machine onResult(ActionResult action) {
        result = action;
        return this;
    }

    @Override
    public void start() {
        PhotoAlbumActivity.startActivity(source.getContext(), this);
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public Bitmap.CompressFormat getCompressFormat() {
        return mCompressFormat;
    }

    @Override
    public void onFile(File file) {
        result.onFile(file);
    }
}
