package com.i9930.croptrails.RoomDatabase.Task;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.Harvest.FetchLastVisitIdListener;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Visit.AllVisitFetchListener;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VisitCountListener;
import com.i9930.croptrails.RoomDatabase.Visit.VrCacheManager;
import com.i9930.croptrails.RoomDatabase.Visit.VrFetchListener;

public class TaskCacheManager {

    private Context context;
    private static TaskCacheManager instance;
    private AppDatabase db;

    public interface CountFetchListener{
        public int onCountLoaded(int count);
    }

    public static TaskCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new TaskCacheManager(context);
        }
        return instance;
    }

    public TaskCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public interface TaskListener{
        public void onTaskFetched(SvTask svTask);
        public void onAllTaskDeleted(boolean isAllDeleted);
    }


    public void getTaskById(final TaskListener taskListener, String taskId) {
        db.taskDaoInterface().getTask(taskId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<SvTask>() {
            @Override
            public void accept(SvTask svTask) throws Exception {
                taskListener.onTaskFetched(svTask);
            }
        });
    }

    public interface AllTaskFetchListener{
        public void onTaskLoaded(List<SvTask> taskList);
    }

    public void getAllTasks(final AllTaskFetchListener allTaskFetchListener) {
        db.taskDaoInterface().getAllTasks().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<SvTask>>() {
            @Override
            public void accept(List<SvTask> taskList) throws Exception {
                if (allTaskFetchListener!=null)
                allTaskFetchListener.onTaskLoaded(taskList);
            }
        });
    }

    public int getCountByType(int type) {
     return    db.taskDaoInterface().getCount(type);
    }

    public void getTaskMaxId(final TaskListener taskListener) {

        db.taskDaoInterface().getTask().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<SvTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable

                    }

                    @Override
                    public void onSuccess(SvTask timelines) {
                        // update the UI
//                        Log.e("TaskCacheManager","DATA "+new Gson().toJson(timelines));
                        taskListener.onTaskFetched(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
//                        Log.e("TestActivity", "exception getting coupons", e);
                        taskListener.onTaskFetched(null);
                    }
                });

    }

    public void addTask(final SvTask svTask) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //for (int i=0;i<visitList.size();i++){
                db.taskDaoInterface().insertVisits(svTask);

                //}
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void update( final SvTask svTask){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.taskDaoInterface().update(svTask);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {
            }
        });

    }



    public void deleteAllTasks(TaskListener listener) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.taskDaoInterface().deleteAllTask();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                if (listener!=null)
                    listener.onAllTaskDeleted(true);
            }

            @Override
            public void onError(Throwable e) {
                if (listener!=null)
                    listener.onAllTaskDeleted(false);
            }
        });
    }

    /*public void update( String id){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.taskDaoInterface().update(id,"Y");
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
            }

            @Override
            public void onError(Throwable e) {
            }
        });

    }*/

}
