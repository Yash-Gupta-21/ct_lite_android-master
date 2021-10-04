package com.i9930.croptrails.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

public class ShowProgress {

    Activity context;
    ProgressDialog mProgressDialog;

    public ShowProgress(Activity context) {
        this.context = context;
    }


    public void showProgressDialog() {


        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog == null) {
                    mProgressDialog = new ProgressDialog(context);
                    mProgressDialog.setMessage("Please Wait...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                }
                if (!mProgressDialog.isShowing())
                    mProgressDialog.show();
            }
        });

    }

    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void showProgressDialog(String title,String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setTitle(title);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();

                }
            });
        }
    }

}
