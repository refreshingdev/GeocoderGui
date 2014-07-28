package org.kokos.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * No modal dialogs (blocking calls) in Android!
 */
public class KDialogs {
    private final Context context;

    public KDialogs(Context context) {
        this.context = context;
    }

    public void askAndDo(int messageId, Runnable runWhenYes) {
        askAndDo(context.getText(messageId), runWhenYes);
    }

    public void askAndDo(CharSequence message, final Runnable runWhenYes) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(
                android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runWhenYes.run();
                    }
                }).setNegativeButton(context.getText(android.R.string.no), null).show();
    }

    public void informAndDo(int messageId, Runnable runAfter) {
        informAndDo(context.getText(messageId), runAfter);
    }


    public void informAndDo(CharSequence message, final Runnable runAfter) {
        new AlertDialog.Builder(context).setMessage(message).setPositiveButton(
                android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        runAfter.run();
                    }
                }).setCancelable(false).show();
    }
}
