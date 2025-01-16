package com.example.inspireface_example.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.inspireface_example.R;


public class InputTextView extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    /**
     * 删除按钮的引用
     */
    private Drawable mClearDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFocus;

    private boolean needCursorCenter = true;  //光标居中
    private String str_hint = "";

    public InputTextView(Context context) {
        this(context, null);
    }

    public InputTextView(Context context, AttributeSet attrs) {
        //这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public InputTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setNeedCursorCenter(boolean needCursorCenter) {
        this.needCursorCenter = needCursorCenter;
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void init() {
        //获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
//        	throw new NullPointerException("You can add drawableRight attribute in XML");
            mClearDrawable = getResources().getDrawable(R.drawable.clear_input);
        }

        float s = 0.5f;
        mClearDrawable.setBounds(0, 0, (int) (mClearDrawable.getIntrinsicWidth() * s), (int) (mClearDrawable.getIntrinsicHeight() * s));
        //默认设置隐藏图标
        setClearIconVisible(false);
        //设置焦点改变的监听
        setOnFocusChangeListener(this);
        //设置输入框里面内容发生改变的监听
        addTextChangedListener(this);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeTextChangedListener(this);
    }

    /**
     * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件
     * 当我们按下的位置 在  EditText的宽度 - 图标到控件右边的间距 - 图标的宽度  和
     * EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;

        if (mListener != null) {
            mListener.focuStateChange(hasFocus);
        }

        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
            if (needCursorCenter) {
                if (TextUtils.isEmpty(getHint())) {
                    str_hint = "";
                } else {
                    str_hint = getHint().toString();
                    setHint("");
                }
            }
        } else {
            setClearIconVisible(false);
            if (needCursorCenter) {
                if (!TextUtils.isEmpty(str_hint)) {
                    setHint(str_hint);
                }
            }
        }
    }


    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     *
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }


    /**
     * 当输入框里面内容发生变化的时候回调的方法
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int count,
                              int after) {
        if (hasFocus) {
            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }



    private InputFocuStateListener mListener;

    public void setListener(InputFocuStateListener mlistener) {
        this.mListener = mlistener;
    }

    public interface InputFocuStateListener {
        void focuStateChange(boolean isFocu);
    }


}
