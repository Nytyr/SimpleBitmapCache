package me.nytyr.simplebitmapcache.utils;

import android.os.AsyncTask;
import android.os.Build;

public abstract class ThreadTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    public AsyncTask<Params, Progress, Result> run(Params... params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            return execute(params);
        }
    }

}
