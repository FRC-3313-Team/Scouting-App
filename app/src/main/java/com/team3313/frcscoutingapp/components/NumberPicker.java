package com.team3313.frcscoutingapp.components;

import android.content.Context;
import android.os.Handler;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

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

    private final long REPEAT_DELAY = 50;

    private final int ELEMENT_HEIGHT = 60;
    private final int ELEMENT_WIDTH = ELEMENT_HEIGHT;

    private final int MINIMUM = 0;
    private final int MAXIMUM = 999;

    private final int TEXT_SIZE = 30;

    public Integer value;

    Button decrement;
    Button increment;
    public EditText valueText;

    private Handler repeatUpdateHandler = new Handler();

    private boolean autoIncrement = false;
    private boolean autoDecrement = false;

    class RepetetiveUpdater implements Runnable {
        public void run() {
            if (autoIncrement) {
                increment();
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
            } else if (autoDecrement) {
                decrement();
                repeatUpdateHandler.postDelayed(new RepetetiveUpdater(), REPEAT_DELAY);
            }
        }
    }

    public NumberPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        LayoutParams elementParams = new LinearLayout.LayoutParams(ELEMENT_HEIGHT, ELEMENT_WIDTH);
        initDecrementButton(context);
        initValueEditText(context);
        initIncrementButton(context);
        if (getOrientation() == VERTICAL) {
            addView(increment, elementParams);
            addView(valueText, elementParams);
            addView(decrement, elementParams);
        } else {
            addView(decrement, elementParams);
            addView(valueText, elementParams);
            addView(increment, elementParams);
        }
    }

    private void initIncrementButton(Context context) {
        increment = new Button(context);
        increment.setTextSize(TEXT_SIZE);
        increment.setText("+");

        // Increment once for a click
        increment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                increment();
            }
        });

        increment.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoIncrement = true;
                        repeatUpdateHandler.post(new RepetetiveUpdater());
                        return false;
                    }
                }
        );

        increment.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoIncrement) {
                    autoIncrement = false;
                }
                return false;
            }
        });
    }

    private void initValueEditText(Context context) {

        value = new Integer(0);

        valueText = new EditText(context);
        valueText.setTextSize(TEXT_SIZE);

        valueText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int arg1, KeyEvent event) {
                int backupValue = value;
                try {
                    value = Integer.parseInt(((EditText) v).getText().toString());
                } catch (NumberFormatException nfe) {
                    value = backupValue;
                }
                return false;
            }
        });

        valueText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ((EditText) v).selectAll();
                }
            }
        });
        valueText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        valueText.setText(value.toString());
        valueText.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    private void initDecrementButton(Context context) {
        decrement = new Button(context);
        decrement.setTextSize(TEXT_SIZE);
        decrement.setText("-");


        decrement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                decrement();
            }
        });


        decrement.setOnLongClickListener(
                new View.OnLongClickListener() {
                    public boolean onLongClick(View arg0) {
                        autoDecrement = true;
                        repeatUpdateHandler.post(new RepetetiveUpdater());
                        return false;
                    }
                }
        );

        decrement.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && autoDecrement) {
                    autoDecrement = false;
                }
                return false;
            }
        });
    }

    public void increment() {
        if (value < MAXIMUM) {
            value = value + 1;
            valueText.setText(value.toString());
        }
    }

    public void decrement() {
        if (value > MINIMUM) {
            value = value - 1;
            valueText.setText(value.toString());
        }
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value > MAXIMUM) value = MAXIMUM;
        if (value >= 0) {
            this.value = value;
            valueText.setText(this.value.toString());
        }
    }

}