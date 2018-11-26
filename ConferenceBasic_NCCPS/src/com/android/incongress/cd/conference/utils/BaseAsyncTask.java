package com.android.incongress.cd.conference.utils;

import android.app.Activity;
import android.os.AsyncTask;

/**
 * Created by Jacky on 2016/11/27 0027.
 */

public abstract class BaseAsyncTask extends AsyncTask {
    private Activity mActivity;

    public BaseAsyncTask(Activity activity) {
        this.mActivity = activity;
    }
    @Override
    protected Object doInBackground(Object[] params) {
        if(null !=  mActivity) {
            backgroundWork();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(null !=  mActivity) {
            preWork();
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(null != mActivity) {
            postWork();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        cancelWork();
    }

    protected abstract void backgroundWork();
    protected abstract void preWork();
    protected abstract void postWork();
    protected abstract void cancelWork();
}
