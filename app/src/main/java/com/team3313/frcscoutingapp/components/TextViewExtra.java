package com.team3313.frcscoutingapp.components;

import android.content.Context;

/**
 * Created by oa10712 on 3/14/2018.
 */

public class TextViewExtra extends android.support.v7.widget.AppCompatTextView {
    public String data = "";

    public TextViewExtra(Context context, String display, String extra) {
        super(context);
        this.data = extra;
        this.setText(display);
    }
}
