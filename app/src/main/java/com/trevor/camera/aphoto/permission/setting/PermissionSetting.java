package com.trevor.camera.aphoto.permission.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.trevor.camera.aphoto.R;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.SettingService;

import java.util.List;

/**
 *
 * @author YanZhenjie
 * @date 2018/1/1
 */
public final class PermissionSetting {

    /**
     * 默认提示语
     */
    private int msg = R.string.message_permission_always_failed;

    private static PermissionSetting INSTANCE = null;

    private Context mContext;

    public static PermissionSetting newInstance(){
        if (null == INSTANCE){
            synchronized (PermissionSetting.class){
                if (null == INSTANCE){
                    INSTANCE = new PermissionSetting();
                }
            }
        }
        return INSTANCE;
    }

    public PermissionSetting setContext(Context mContext) {
        this.mContext = mContext;
        return INSTANCE;
    }

    /**
     *  设置为什么需要该权限的提示语；
     *  <br>已提供默认提示语</br>
     *
     * @param msg 提示语，建议针对不同的权限，解释为什么需要该权限；
     *            <br> StringRes 末尾必须跟： "\n\n%1$s"<br/>
     * @return {@link PermissionSetting}
     */
    public PermissionSetting setMsg(@StringRes int msg) {
        this.msg = msg;
        return INSTANCE;
    }

    /**
     *  showSetting
     *
     * @param permissions 正在申请的权限
     */
    public void showSetting(final List<String> permissions) {
        List<String> permissionNames = Permission.transformText(mContext, permissions);
        String message = mContext.getString(msg, TextUtils.join("\n", permissionNames));

        final SettingService settingService = AndPermission.permissionSetting(mContext);
        AlertDialog.newBuilder(mContext)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingService.execute();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        settingService.cancel();
                    }
                })
                .show();
    }
}