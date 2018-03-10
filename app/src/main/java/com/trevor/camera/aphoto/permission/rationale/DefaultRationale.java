package com.trevor.camera.aphoto.permission.rationale;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.trevor.camera.aphoto.R;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;

/**
 *
 * @author DAIFEI
 * @date 2018/3/2
 */
public class DefaultRationale implements Rationale {


    /**
     * 默认提示语
     */
    private int msg = R.string.message_permission_rationale;

    /**
     * setMsg
     * @param msg 提示语，建议针对不同的权限，解释为什么需要该权限；
     *            <br> StringRes 末尾必须跟： "\n\n%1$s"<br/>
     */
    public DefaultRationale setMsg(@StringRes int msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context,permissions);
        String message = context.getString(
                msg,
                TextUtils.join("\n", permissionNames));

        AlertDialog.newBuilder(context)
                .setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(message)
                .setPositiveButton(R.string.resume, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户继续
                        executor.execute();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户终止
                        executor.cancel();
                    }
                })
                .show();
    }
}
