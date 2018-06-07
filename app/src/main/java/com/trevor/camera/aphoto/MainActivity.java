package com.trevor.camera.aphoto;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;

import com.trevor.camera.aphoto.permission.rationale.DefaultRationale;
import com.trevor.camera.aphoto.permission.setting.PermissionSetting;
import com.trevor.camera.aphoto.view.CameraBottomSheetDialogFragment;
import com.trevor.photoalbum.PhotoAlbum;
import com.trevor.photoalbum.result.ActionResult;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.List;

/**
 * 示例程序
 *
 * @author DAIFEI
 */
public class MainActivity extends AppCompatActivity implements ViewContract.ActivityPresenter {

    private CameraBottomSheetDialogFragment dialogFragment;
    private AppCompatImageView imageViewTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageViewTwo = (AppCompatImageView) findViewById(R.id.trevor_image_view_two);
    }

    public void openCamera(View view) {
        open();
    }

    private void open() {
        if (dialogFragment == null) {
            dialogFragment = new CameraBottomSheetDialogFragment();
            dialogFragment.setPresenter(this);
            dialogFragment.show(getSupportFragmentManager(), "CameraDialog");
        } else {
            dialogFragment.show(getSupportFragmentManager(), "CameraDialog");
        }
    }

    @Override
    public void requestPermission(final int id, String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .rationale(new DefaultRationale().setMsg(R.string.message_permission_rationale))
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 授权
                        if (id == R.id.camera_dialog_pz) {
                            openCameras();
                        } else if (id == R.id.camera_dialog_xc) {
                            openPhotoAlbum();
                        }
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        // 拒绝
                        if (AndPermission.hasAlwaysDeniedPermission(
                                MainActivity.this,
                                permissions)) {
                            // 总是被拒绝的权限
                            PermissionSetting.newInstance()
                                    .setContext(MainActivity.this)
                                    .setMsg(R.string.message_permission_always_failed)
                                    .showSetting(permissions);
                        }
                    }
                })
                .start();
    }

    private void openCameras() {
        /*
         * APhoto 使用示例 - （打开相机拍照）
         */
        PhotoAlbum.with(this)
                .openCamera()
                .onResult(new ActionResult() {
                    @Override
                    public void onFile(File file) {
                        imageViewTwo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                        Log.d(
                                MainActivity.class.getSimpleName() + "： 压缩后的大小 - ",
                                "" + file.length());
                    }
                })
                .start();
    }

    private void openPhotoAlbum() {
        /*
         * APhoto 使用示例 - （打开相册）
         */
        PhotoAlbum.with(this)
                .openPhotoAlbum()
                .onResult(new ActionResult() {
                    @Override
                    public void onFile(File file) {
                        // 相册选取的文件
                        imageViewTwo.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                        Log.d(
                                MainActivity.class.getSimpleName() + "： 相册选取的文件大小 - ",
                                "" + file.length());
                    }
                })
                .start();
    }
}
