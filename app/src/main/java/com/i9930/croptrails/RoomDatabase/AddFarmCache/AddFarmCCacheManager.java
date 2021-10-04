package com.i9930.croptrails.RoomDatabase.AddFarmCache;

import android.content.Context;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.Action;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.CompRegistry.DatabaseResInterface;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;

public class AddFarmCCacheManager {

    private Context context;
    private static AddFarmCCacheManager _instance;
    private AppDatabase db;

    public static AddFarmCCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new AddFarmCCacheManager(context);
        }
        return _instance;
    }

    public AddFarmCCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getCompRegData(final FetchFarmCacheListener databaseResInterface) {
        db.addFarmCDao().getAllFarmCache().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<AddFarmCache>>() {
            @Override
            public void accept(List<AddFarmCache> compRegModels) throws Exception {
                databaseResInterface.onGetAllCompRegData(compRegModels);
            }
        });
    }

    public void addFarmCache( final AddFarmCache addFarmCache) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.addFarmCDao().insertAddFarmCache(addFarmCache);

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


    public void deleteAllData(/*final DatabaseResInterface databaseResInterface*/){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.addFarmCDao().deleteAllData();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                /*databaseResInterface.onAllDataDeleted();*/


            }

            @Override
            public void onError(Throwable e) {
                /*databaseResInterface.onAllDataNotDeleted();*/


            }
        });



    }
    public void updateHarvestData( final AddFarmCache harvestT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.addFarmCDao().updateData(harvestT);

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

}