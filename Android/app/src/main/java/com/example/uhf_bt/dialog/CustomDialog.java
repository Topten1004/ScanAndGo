package com.example.uhf_bt.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.uhf_bt.R;
import com.example.uhf_bt.tool.ToastUtils;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

/**
 * Created by WuShengjun on 2017/11/5.
 */

public class CustomDialog extends BaseDialog {
    private ViewGroup mMessageLayout;
    private View lineView;
    private TextView tv_title, tv_msg;
    private TextView btn_ok, btn_cancel;
    private OnOkBtnClickListener onOkBtnClickListener;
    private OnCancelBtnClickListener onCancelBtnClickListener;
    private OnDialogDismissListener onDialogDismissListener;

    public CustomDialog(Context context) {
        this(context, true);
    }

    public CustomDialog(Context context, boolean cancelable) {
        this(context, cancelable, null);
    }

    public CustomDialog(Context context, boolean cancelable, View contentView) {
        super(context, cancelable);
        this.mContentView = contentView;
        init();
    }

    private void init() {
        mContentView = View.inflate(mContext, R.layout.usuallib_normal_dialog_layout, null);
        mMessageLayout = getView(mContentView, R.id.messageLayout);
        tv_title = getView(mContentView, R.id.tv_title);
        tv_msg = getView(mContentView, R.id.tv_msg);
        lineView = getView(mContentView, R.id.title_line_heng);
        btn_cancel = getView(mContentView, R.id.btn_cancel);
        btn_cancel.setOnClickListener(mListener);
        btn_ok = getView(mContentView, R.id.btn_ok);
        btn_ok.setOnClickListener(mListener);
        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(onDialogDismissListener != null) {
                    onDialogDismissListener.onDismiss();
                }
            }
        });
        // 将布局设置给Dialog
        mDialog.setContentView(mContentView);
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btn_ok) {
                if(onOkBtnClickListener != null) {
                    onOkBtnClickListener.onOkClick(mDialog, mContentView);
                } else {
                    ToastUtils.showNormal(mContext, "Ok Button clicked");
                }
            } else if(view.getId() == R.id.btn_cancel) {
                if(onCancelBtnClickListener != null) {
                    onCancelBtnClickListener.onCancelClick(mDialog);
                } else {
                    dismiss();
                }
            }
        }
    };

    public CustomDialog setMessageView(View messageView) {
        return setMessageView(messageView, -1);
    }

    public CustomDialog setMessageView(View messageView, int gravity) {
        if(mMessageLayout != null) {
            if (tv_msg != null) {
                mMessageLayout.removeView(tv_msg);
                tv_msg = null;
            }
            mMessageLayout.addView(messageView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMessageLayout.getLayoutParams();
            if (params != null && gravity != -1) {
                params.gravity = gravity;
                mMessageLayout.setLayoutParams(params);
            }
        }
        return this;
    }

    public CustomDialog setContentView(View contentView) {
        return super.setContentView(contentView);
    }

    public CustomDialog setCancelable(boolean cancelable) {
        return super.setCancelable(cancelable);
    }

    public CustomDialog setCanceledOnTouchOutside(boolean cancel) {
        return super.setCanceledOnTouchOutside(cancel);
    }

    public CustomDialog setTitle(CharSequence title) {
        return setTitle(title, 0, -1);
    }

    public CustomDialog setTitle(CharSequence title, @ColorRes int colorId, float textSize) {
        if(tv_title != null) {
            tv_title.setText(title);
            setTextParms(tv_title, colorId, textSize);
        }
        return this;
    }

    public CustomDialog setMessage(CharSequence message) {
        return setMessage(message, 0, -1);
    }

    public CustomDialog setMessage(CharSequence message, int gravity) {
        return setMessage(message, 0, -1, gravity);
    }

    public CustomDialog setMessage(CharSequence message, @ColorRes int colorId, float textSize) {
        return setMessage(message, colorId, textSize, -1);
    }

    public CustomDialog setMessage(CharSequence message, @ColorRes int colorId, float textSize, int gravity) {
        if(tv_msg != null) {
            tv_msg.setText(message);
            setTextParms(tv_msg, colorId, textSize);
            if(gravity != -1) {
                tv_msg.setGravity(gravity);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mMessageLayout.getLayoutParams();
                if(params != null) {
                    params.gravity = gravity;
                    mMessageLayout.setLayoutParams(params);
                }
            }
        }
        return this;
    }

    public CustomDialog setMsgLayoutPadding(int left, int top, int right, int bottom) {
        if(mMessageLayout != null) {
            mMessageLayout.setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        }
        return this;
    }

    public CustomDialog setMessagePadding(int left, int top, int right, int bottom) {
        if(tv_msg != null) {
            tv_msg.setPadding(dp2px(left), dp2px(top), dp2px(right), dp2px(bottom));
        }
        return this;
    }

    public CustomDialog setOkBtnText(CharSequence text) {
        return setOkBtnText(text, 0, -1);
    }

    public CustomDialog setOkBtnText(CharSequence text, @ColorRes int colorId) {
        return setOkBtnText(text, colorId, -1);
    }

    public CustomDialog setOkBtnText(CharSequence text, @ColorRes int colorId, float textSize) {
        if(btn_ok != null) {
            btn_ok.setText(text);
            setTextParms(btn_ok, colorId, textSize);
        }
        return this;
    }

    public CustomDialog setCancelBtnText(CharSequence text) {
        return setCancelBtnText(text, 0, -1);
    }

    public CustomDialog setCancelBtnText(CharSequence text, @ColorRes int colorId, float textSize) {
        if(btn_cancel != null) {
            btn_cancel.setText(text);
            setTextParms(btn_cancel, colorId, textSize);
        }
        return this;
    }

    public CustomDialog setBtnsEnabled(boolean okBtnEnabled, boolean cancelBtnEnabled) {
        if(btn_ok != null) {
            btn_ok.setEnabled(okBtnEnabled);
        }
        if(btn_cancel != null) {
            btn_cancel.setEnabled(cancelBtnEnabled);
        }
        return this;
    }

    public CustomDialog setHeight(int height) {
        return super.setHeight(height);
    }

    @Override
    public CustomDialog setWidth(int width) {
        return super.setWidth(width);
    }

    @Override
    public CustomDialog setMinWidth(int minWidth) {
        return super.setMinWidth(minWidth);
    }

    @Override
    public CustomDialog setMaxHeight(int maxHeight) {
        return super.setMaxHeight(maxHeight);
    }

    @Override
    public CustomDialog setWindowBackground(@DrawableRes int resource) {
        return super.setWindowBackground(resource);
    }

    @Override
    public CustomDialog setWindowBackground(Drawable drawable) {
        return super.setWindowBackground(drawable);
    }

    public CustomDialog hideTitleBottomLine(boolean hide) {
        if (lineView != null) {
            if (hide) {
                lineView.setVisibility(View.GONE);
            } else {
                lineView.setVisibility(View.VISIBLE);
            }
        }
        return this;
    }

    public CustomDialog hideTitle(boolean hide) {
        if(tv_title != null) {
            if(hide) {
                tv_title.setVisibility(View.GONE);
                if(lineView != null) {
                    lineView.setVisibility(View.GONE);
                }
            } else {
                tv_title.setVisibility(View.VISIBLE);
                if(lineView != null) {
                    lineView.setVisibility(View.VISIBLE);
                }
            }
        }
        return this;
    }

    public CustomDialog hideCancelBtn(boolean hide) {
        if(btn_ok == null || btn_cancel == null)
            return this;

        if (hide) {
            btn_cancel.setVisibility(View.GONE);
            btn_ok.setBackgroundResource(R.drawable.selecter_bottomall_coner_fe_f0);
        } else {
            btn_cancel.setVisibility(View.VISIBLE);
            btn_ok.setBackgroundResource(R.drawable.selecter_bottomright_coner_fe_f0);
        }
        hideOrShowLines();
        return this;
    }

    public CustomDialog hideOkBtn(boolean hide) {
        if(btn_ok == null || btn_cancel == null)
            return this;

        if (hide) {
            btn_ok.setVisibility(View.GONE);
            btn_cancel.setBackgroundResource(R.drawable.selecter_bottomall_coner_fe_f0);
        } else {
            btn_ok.setVisibility(View.VISIBLE);
            btn_cancel.setBackgroundResource(R.drawable.selecter_bottomleft_coner_fe_f0);
        }
        hideOrShowLines();
        return this;
    }

    public OnOkBtnClickListener getOnOkBtnClickListener() {
        return onOkBtnClickListener;
    }

    public CustomDialog setOnOkBtnClickListener(OnOkBtnClickListener onOkBtnClickListener) {
        this.onOkBtnClickListener = onOkBtnClickListener;
        return this;
    }

    public OnCancelBtnClickListener getOnCancelBtnClickListener() {
        return onCancelBtnClickListener;
    }

    public CustomDialog setOnCancelBtnClickListener(OnCancelBtnClickListener onCancelBtnClickListener) {
        this.onCancelBtnClickListener = onCancelBtnClickListener;
        return this;
    }

    public OnDialogDismissListener getOnDialogDismissListener() {
        return onDialogDismissListener;
    }

    public CustomDialog setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
        return this;
    }

    public interface OnOkBtnClickListener {
        void onOkClick(Dialog dialog, View contentView);
    }

    public interface OnCancelBtnClickListener {
        void onCancelClick(Dialog dialog);
    }

    public interface OnDialogDismissListener {
        void onDismiss();
    }

    private void setTextParms(TextView textView, @ColorRes int colorId, float textSize) {
        if(colorId != 0)
            textView.setTextColor(ContextCompat.getColor(mContext, colorId));
        if(textSize > 0)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    private void hideOrShowLines() {
        View lineHeng = getView(mContentView, R.id.line_heng);
        if(lineHeng != null) {
            if (btn_ok.getVisibility() == View.GONE && btn_cancel.getVisibility() == View.GONE) {
                lineHeng.setVisibility(View.GONE);
            } else {
                lineHeng.setVisibility(View.VISIBLE);
            }
        }
        View lineShu = getView(mContentView, R.id.line_shu);
        if(lineShu != null) {
            if (btn_ok.getVisibility() == View.GONE || btn_cancel.getVisibility() == View.GONE) {
                lineShu.setVisibility(View.GONE);
            } else {
                lineShu.setVisibility(View.VISIBLE);
            }
        }
    }
}
