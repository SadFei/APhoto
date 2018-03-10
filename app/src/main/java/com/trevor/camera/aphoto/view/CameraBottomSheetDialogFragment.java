package com.trevor.camera.aphoto.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trevor.camera.aphoto.R;
import com.trevor.camera.aphoto.ViewContract;
import com.yanzhenjie.permission.Permission;

/**
 * @author DAIFEI
 * @date 2018/3/1
 */

public class CameraBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    TextView pz;
    TextView xc;
    TextView qx;

    private ViewContract.ActivityPresenter presenter = null;

    public void setPresenter(ViewContract.ActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.camera_dialog, container, false);
        bindingView(view);
        return view;
    }

    public void bindingView(View view) {
        pz = (TextView) view.findViewById(R.id.camera_dialog_pz);
        xc = (TextView) view.findViewById(R.id.camera_dialog_xc);
        qx = (TextView) view.findViewById(R.id.camera_dialog_qx);

        pz.setOnClickListener(this);
        xc.setOnClickListener(this);
        qx.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.camera_dialog_pz) {
            dismiss();
            // 拍照
            presenter.requestPermission(
                    id,
                    Permission.CAMERA,
                    Permission.READ_EXTERNAL_STORAGE,
                    Permission.WRITE_EXTERNAL_STORAGE);

        } else if (id == R.id.camera_dialog_xc) {
            dismiss();
            // 相册
            presenter.requestPermission(
                    id,
                    Permission.Group.STORAGE);

        } else if (id == R.id.camera_dialog_qx) {
            // 取消
            dismiss();
        }
    }

}
