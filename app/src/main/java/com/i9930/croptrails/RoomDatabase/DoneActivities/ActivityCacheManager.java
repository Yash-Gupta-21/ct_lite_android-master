package com.i9930.croptrails.RoomDatabase.DoneActivities;

import android.content.Context;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmNotifyInterface;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestCacheManager;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestFetchListener;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.RoomDatabase.Visit.VisitCountListener;

public class ActivityCacheManager {


    private Context context;
    private static ActivityCacheManager instance;
    private AppDatabase db;

    public static ActivityCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityCacheManager(context);
        }
        return instance;
    }

    public ActivityCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getAllActivities(final AllActivityFetchListener mainViewInterface) {
        db.activityDaoInterface().getAllActivities().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<DoneActivity>>() {
            @Override
            public void accept(List<DoneActivity> doneActivityList) throws Exception {
                mainViewInterface.onAllActivityLoaded(doneActivityList);
            }
        });
    }


    public void getActivity(final ActivityFetchListener listener, String activityId) {
        db.activityDaoInterface().getActivity(activityId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<DoneActivity>() {
            @Override
            public void accept(DoneActivity doneActivity) throws Exception {
                listener.onActivityLoaded(doneActivity);
            }
        });
    }

    public void addActivity( final DoneActivity doneActivity) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.activityDaoInterface().insert(doneActivity);

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


    public void deleteAllHarvests(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.activityDaoInterface().deleteAllActivities();
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


    public void update( final DoneActivity doneActivity){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.activityDaoInterface().update(doneActivity);



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

    public void update( String offlineFarmId,String farmId){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.activityDaoInterface().update(offlineFarmId,farmId);

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

    public void getOfflineVisitCount(final ActivityCountListener activityCountListener) {
        db.activityDaoInterface().getNumberOfRows().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                activityCountListener.onActivityCountLoaded(integer);
            }
        });

    }

}
