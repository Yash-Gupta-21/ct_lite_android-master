package com.i9930.croptrails.Notification.Services;

import android.content.Context;

import androidx.work.Worker;
import androidx.work.WorkerParameters;
import io.reactivex.annotations.NonNull;

public class UploadWorker extends Worker {

    public UploadWorker(
        @NonNull Context context,
        @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
      // Do the work here--in this case, upload the images.


      // Indicate whether the task finished successfully with the Result
      return Result.success();
    }
}