package com.i9930.croptrails.RoomDatabase.Farmer;

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
import com.i9930.croptrails.RoomDatabase.DoneActivities.AllActivityFetchListener;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownCacheManager2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmCacheManager;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmLoadListener;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmNotifyInterface;

public class FarmerCacheManager {

    public interface FarmerInsertListener{
        public void onFarmerInserted(long farmerId);
    }


    public interface GetFarmerListener{
        public void onFarmerLoaded(FarmerT farmerT);
    }


    public interface GetAllFarmerListener{
        public void onFarmerLoaded(List<FarmerT> farmerT);
    }

    private Context context;
    private static FarmerCacheManager _instance;
    private AppDatabase db;

    public static FarmerCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new FarmerCacheManager(context);
        }
        return _instance;
    }

    public FarmerCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void addFarmer(final FarmerInsertListener listener, final FarmerT farmerT) {

        final long[] id = new long[1];

        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                id[0] =db.farmerDaoInterface().insert(farmerT);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                try {
                    listener.onFarmerInserted(id[0]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void deleteAllFarmers(/*final FarmNotifyInterface notifyInterface*/){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.farmerDaoInterface().deleteAllFarmers();
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


    public void getFarmer(final GetFarmerListener getFarmerListener, String farmerId) {
        db.farmerDaoInterface().getFarmerByFarmerId(farmerId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<FarmerT>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(FarmerT farmerT) {
                        // update the UI

                        getFarmerListener.onFarmerLoaded(farmerT);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        getFarmerListener.onFarmerLoaded(null);
                    }
                });

    }


    public void getAllFarmers(final GetAllFarmerListener listener,String isOfflineAdded) {
        db.farmerDaoInterface().getAllFarmerOffline(isOfflineAdded,"N").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<FarmerT>>() {
            @Override
            public void accept(List<FarmerT> doneActivityList) throws Exception {
                listener.onFarmerLoaded(doneActivityList);
            }
        });
    }

    public void getAllFarmers(final GetAllFarmerListener listener,String isOfflineAdded,String isUpdated) {
        db.farmerDaoInterface().getAllFarmer(isOfflineAdded,isUpdated).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<FarmerT>>() {
            @Override
            public void accept(List<FarmerT> doneActivityList) throws Exception {
                listener.onFarmerLoaded(doneActivityList);
            }
        });
    }



    public void updateFarm( String farmerIdOffline,String farmerId,String isUploaded) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.farmerDaoInterface().update(farmerIdOffline,farmerId,isUploaded);


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

    public void updateFarmData( String farmerId,String data) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.farmerDaoInterface().updateData(farmerId,data);


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

    public interface OnUpdateSuccessListener{
        public void onFarmerUpdated(boolean isUpdated);
    }

    public void updateFarm( OnUpdateSuccessListener listener,FarmerT farmerT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.farmerDaoInterface().update(farmerT);


            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                if (listener!=null)
                    listener.onFarmerUpdated(true);
            }

            @Override
            public void onError(Throwable e) {
                if (listener!=null)
                    listener.onFarmerUpdated(false);
            }
        });
    }


}
