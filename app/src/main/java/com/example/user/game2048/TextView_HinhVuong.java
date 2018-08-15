package com.example.user.game2048;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import me.grantland.widget.AutofitTextView;

/**
 * Created by USER on 01/02/2018.
 */

/*
* Custom lại TextView thành hình vuông
* */
public class TextView_HinhVuong extends AutofitTextView {
    public TextView_HinhVuong(Context context) {
        super(context);
    }

    public TextView_HinhVuong(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TextView_HinhVuong(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // Khi TextView được vẽ
    // set kích thước chiều dài bằng chiều rộng
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();

        setMeasuredDimension(width, width);
    }
}
