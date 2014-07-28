package org.kokos.geolocatorgui;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Calls AfterTextChangedListener when user stops typing.
 */
public class DelayedTextChangedHandler {
    private final Handler handler;
    private final EditText editText;
    private final AfterTextChangedListener afterTextChangedListener;

    private long lastTypeTime = 0;
    private static final int MIN_EVENT_FIRE_DELAY_MS = 500;

    public DelayedTextChangedHandler(EditText editText, AfterTextChangedListener afterTextChangedListener) {
        this.handler = new Handler();
        this.editText = editText;
        this.afterTextChangedListener = afterTextChangedListener;
    }

    public void startListening() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                handleTextChanged(s);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    private void handleTextChanged(CharSequence newText) {
        long now = System.currentTimeMillis();
        if (lastTypeTime != 0 && now - lastTypeTime >= MIN_EVENT_FIRE_DELAY_MS) {
            lastTypeTime = now;
            afterTextChangedListener.afterTextChanged(newText);
        } else {
            lastTypeTime = now;
            sheduleEcho(now);
        }
    }

    private void sheduleEcho(final long now) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleEcho(now);
            }
        }, MIN_EVENT_FIRE_DELAY_MS);
    }

    private void handleEcho(long echoScheduledTime) {
        // check if nothing was typed since this echo was scheduled
        if (echoScheduledTime == lastTypeTime) {
            afterTextChangedListener.afterTextChanged(editText.getText());
        }
    }
}
