package com.trevor.photoalbum;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.trevor.photoalbum.machine.MachineEntity;
import com.trevor.photoalbum.thread.ThreadProxy;
import com.trevor.photoalbum.utils.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author DAIFEI
 * @date 2018/3/7
 */
public class PhotoAlbumActivity extends AppCompatActivity {
    /**
     * 宽的压缩范围
     */
    private static final float STANDARD_WIDTH = 480f;
    /**
     * 高的压缩范围
     */
    private static final float STANDARD_HEIGHT = 800f;
    /**
     * 保存图片的后缀名-png
     */
    private static final String SUFFIX_PNG = "_Camera.png";
    /**
     * 保存图片的后缀名-jpg
     */
    private static final String SUFFIX_JPG = "_Camera.jpg";
    /**
     * 保存图片的后缀名-webp
     */
    private static final String SUFFIX_WEBP = "_Camera.webp";

    /**
     * 文件后缀名
     */
    private static final String SUFFIX;

    /**
     * 保存图片的名称前缀
     */
    private static final String FILE_SIMPLE_NAME = "Trevor_";
    /**
     * 拍照 requestCode
     */
    public static final int TAKE_PHOTO = 1621;
    /**
     * 相册 requestCode
     */
    public static final int TAKE_ALBUM = 1622;
    /**
     * {@link MachineEntity} 实现文件传送桥梁
     */
    private static ActivityListener mListener;
    /**
     * 临时保存文件
     */
    private File file;

    /**
     * 压缩格式
     */
    private static final Bitmap.CompressFormat COMPRESS_FORMAT;

    static {
        /*
         * WEBP 的压缩格式是在 API 14 才提供支持的新的压缩格式，
         * 固 小于14 的手机上运行 JPEG 的压缩格式
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
            SUFFIX = SUFFIX_JPG;
        } else {
            COMPRESS_FORMAT = Bitmap.CompressFormat.WEBP;
            SUFFIX = SUFFIX_WEBP;
        }
    }

    public static void startActivity(Context context, ActivityListener listener) {
        mListener = listener;
        Intent intent = new Intent(context, PhotoAlbumActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createIntent();
    }

    @Override
    protected void onPause() {
        /*
         * 暂时解决了有动画的问题
         */
        overridePendingTransition(0, 0);
        super.onPause();
    }

    private void createIntent() {
        switch (mListener.getType()) {
            // 拍照
            case MachineEntity.CAMERA:
                Uri uri = null;
                File directory = new File(FileUtil.getRootPaths(getApplicationContext()));

                file = new File(
                        directory,
//                        "Trevor_" + System.currentTimeMillis() + "_Camera.jpg"
                        FILE_SIMPLE_NAME + System.currentTimeMillis() + SUFFIX
                );

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(
                            this,
                            "com.trevor.photoalbum.provider",
                            file);
                } else {
                    uri = Uri.fromFile(file);
                }

                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent, TAKE_PHOTO);
                break;

            // 相册
            case MachineEntity.PHOTO_ALBUM:
                Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                intentAlbum.setType("image/*");
                intentAlbum.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intentAlbum, TAKE_ALBUM);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAKE_PHOTO) {
                // 拍照返回
                compress(file);
            } else if (requestCode == TAKE_ALBUM) {
                // 相册返回
                result(data.getData());
            }
        } else if (resultCode == RESULT_CANCELED) {
            // 取消了
            PhotoAlbumActivity.this.finish();
        } else {
            // 退出
            PhotoAlbumActivity.this.finish();
        }
    }

    /**
     * 转换Uri 并返回File
     *
     * @param uri uri
     */
    private void result(Uri uri) {
        String filePath = FileUtil.getPath(this, uri);
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                mListener.onFile(file);
                PhotoAlbumActivity.this.finish();
            } else {
                // 不可读写
                Log.e("PhotoAlbum：", file.getAbsolutePath() + "is ont fund !");
                /*Toast.makeText(
                        PhotoAlbumActivity.this,
                        "获取文件失败", Toast.LENGTH_LONG).show();*/
                PhotoAlbumActivity.this.finish();
            }
        } else {
            // null
            Log.e("PhotoAlbum：", "获取文件路径失败!");
            /*Toast.makeText(
                    PhotoAlbumActivity.this,
                    "获取文件路径失败", Toast.LENGTH_LONG).show();*/
            PhotoAlbumActivity.this.finish();
        }
    }

    /**
     * 压缩并返回File
     *
     * @param file File
     */
    private void compress(final File file) {

        Log.e(
                PhotoAlbumActivity.class.getSimpleName() + "： 压缩前的大小 - ",
                "" + file.length());

        ThreadProxy.write()
                .corePoolSize(1)
                .maximumPoolSize(1)
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                        if (bitmap == null) {

                            int h = options.outHeight;
                            int w = options.outWidth;

                            int zoomRatio = 1;
                            if (w > h && w > STANDARD_WIDTH) {
                                zoomRatio = (int) (w / STANDARD_WIDTH);
                            } else if (w < h && h > STANDARD_HEIGHT) {
                                zoomRatio = (int) (h / STANDARD_HEIGHT);
                            }
                            if (zoomRatio <= 0) {
                                zoomRatio = 1;
                            }

                            options.inSampleSize = zoomRatio;
                            options.inJustDecodeBounds = false;
                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);

                            // 压缩 并 覆盖保存
                            try {
                                BufferedOutputStream bos = new BufferedOutputStream(
                                        new FileOutputStream(file)
                                );
                                //  TODO
                                bitmap.compress(COMPRESS_FORMAT, 50, bos);

                                bos.flush();
                                bos.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        // runOnUiThread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (file.exists()) {
                                    mListener.onFile(file);
                                    PhotoAlbumActivity.this.finish();
                                } else {
                                    Log.e("PhotoAlbum：", file.getAbsolutePath() + "is ont fund !");
                                    PhotoAlbumActivity.this.finish();
                                }
                            }
                        });
                    }
                });
    }
}
