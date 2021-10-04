package com.i9930.croptrails.RoomDatabase.Visit;

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
    import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
    import com.i9930.croptrails.RoomDatabase.Harvest.FetchLastVisitIdListener;
    import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitTable;

public class VrCacheManager {

    private Context context;
    private static VrCacheManager instance;
    private AppDatabase db;

    public static VrCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new VrCacheManager(context);
        }
        return instance;
    }

    public VrCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }


    public void getVisit(final VrFetchListener vrFetchListener, String vrId) {
        db.vrDaoInterface().getVisit(vrId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Visit>() {
            @Override
            public void accept(Visit visitTableList) throws Exception {
                vrFetchListener.onVisitLoaded(visitTableList);
            }
        });
    }

    public void addVisits(final Visit visitList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //for (int i=0;i<visitList.size();i++){
                db.vrDaoInterface().insertVisits(visitList);

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

    public void update( final Visit visit){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.vrDaoInterface().update(visit);
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

    public void update( String id, String farmId){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.vrDaoInterface().update(id,farmId);
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


    public void deleteAllVisits() {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.vrDaoInterface().deleteAllVisits();
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


    public void getAllVisits(final AllVisitFetchListener allVisitFetchListener) {
        db.vrDaoInterface().getAllVisits().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Visit>>() {
            @Override
            public void accept(List<Visit> visitList) throws Exception {
                allVisitFetchListener.onAllVisitLoaded(visitList);
            }
        });
    }


    public void getLastVisit(final FetchLastVisitIdListener lastVisitIdListener) {
        db.vrDaoInterface().getLastVisit().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Visit>() {
            @Override
            public void accept(Visit visitTableList) throws Exception {
                lastVisitIdListener.onVisitIdLoaded(visitTableList);
            }
        });
    }

    public void getOfflineVisitCount(final VisitCountListener visitCountListener) {
        db.vrDaoInterface().getNumberOfRows().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                visitCountListener.onCountLoaded(integer);
            }
        });

    }
}
