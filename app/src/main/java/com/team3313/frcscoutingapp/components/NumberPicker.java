package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Copyright (c) 2010, Jeffrey F. Cole
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 	Redistributions of source code must retain the above copyright notice, this
 * 	list of conditions and the following disclaimer.
 *
 * 	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 * 	Neither the name of the technologichron.net nor the names of its contributors
 * 	may be used to endorse or promote products derived from this software
 * 	without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
public class NumberPicker extends LinearLayout {

    Button dec, inc;
    TextView display;
    Integer value;

    public NumberPicker(Context context) {
        super(context);
        this.setOrientation(LinearLayout.HORIZONTAL);
        value = 0;

        dec = new Button(context);
        dec.setText("-");
        dec.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        dec.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue(value - 1);
            }
        });
        this.addView(dec);

        display = new TextView(context);
        display.setText("0");
        display.setGravity(Gravity.CENTER);
        display.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        this.addView(display);

        inc = new Button(context);
        inc.setText("+");
        inc.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        inc.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setValue(value + 1);
            }
        });
        this.addView(inc);
    }


    public void setValue(int value) {
        this.value = value;
        display.setText(String.valueOf(this.value));
    }

    public Integer getValue() {
        return value;
    }
}