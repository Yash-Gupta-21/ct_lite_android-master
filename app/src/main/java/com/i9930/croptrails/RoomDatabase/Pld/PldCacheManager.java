package com.i9930.croptrails.RoomDatabase.Pld;

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

public class PldCacheManager {


    private Context context;
    private static PldCacheManager _instance;
    private AppDatabase db;

    public interface  OnFarmInserted {
        public void onInserted(long id);
    }

    public interface PldFetchListener{
        public void onPldLoaded(List<OfflinePld>pldList);
    }


    public static PldCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new PldCacheManager(context);
        }
        return _instance;
    }

    public PldCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getAllPld(final PldFetchListener mainViewInterface) {
        db.pldDaoInterface().getPldList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<OfflinePld>>() {
            @Override
            public void accept(List<OfflinePld> farms) throws Exception {
                mainViewInterface.onPldLoaded(farms);
            }
        });
    }

    public void getOfflineAddedPld(final PldFetchListener mainViewInterface) {
        db.pldDaoInterface().getNotUploadedPld("N").
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<OfflinePld>>() {
            @Override
            public void accept(List<OfflinePld> farms) throws Exception {
                mainViewInterface.onPldLoaded(farms);
            }
        });
    }


    public void addPld( OfflinePld offlinePld) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.pldDaoInterface().insertPld(offlinePld);
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



    public void updatePld( final OfflinePld offlinePld) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                    db.pldDaoInterface().update(offlinePld);


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



    public void deleteAllFarms(/*final FarmNotifyInterface notifyInterface*/){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.pldDaoInterface().deleteAllPld();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                /*notifyInterface.farmDeleted();*/
            }

            @Override
            public void onError(Throwable e) {
               /* notifyInterface.farmDeletionFaild();*/
            }
        });
    }



}
