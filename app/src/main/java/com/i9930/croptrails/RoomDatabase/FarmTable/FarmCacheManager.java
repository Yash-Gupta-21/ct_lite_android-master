package com.i9930.croptrails.RoomDatabase.FarmTable;

import android.content.Context;

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
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerCacheManager;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;

public class FarmCacheManager {


    private Context context;
    private static FarmCacheManager _instance;
    private AppDatabase db;

    public interface  OnFarmInserted {
        public void onInserted(long id);
    }

    public interface FetchFarmerFarmListener{
        public void onFarmsLoaded(List<Farm> list);
    }

    public static FarmCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new FarmCacheManager(context);
        }
        return _instance;
    }

    public FarmCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getAllFarms(final FarmNotifyInterface mainViewInterface) {
        db.farmDao().getFarms().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Farm>>() {
            @Override
            public void accept(List<Farm> farms) throws Exception {
                mainViewInterface.onFarmLoaded(farms);
            }
        });
    }

    public void getAllFarmsOfFarmer(final FetchFarmerFarmListener mainViewInterface,String farmerId,String isOfflineAdded) {
        db.farmDao().getFarmerFarms(farmerId,isOfflineAdded,"Y").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Farm>>() {
            @Override
            public void accept(List<Farm> farms) throws Exception {
                mainViewInterface.onFarmsLoaded(farms);
            }
        });
    }


    public void getExistingFarmerFarm(final FetchFarmerFarmListener mainViewInterface,String isExistingFarm, String isUploaded) {
        db.farmDao().getExistingFarmerFarm(isExistingFarm,isUploaded,"Y").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Farm>>() {
            @Override
            public void accept(List<Farm> farms) throws Exception {
                mainViewInterface.onFarmsLoaded(farms);
            }
        });
    }


    public void addFarms(final FarmNotifyInterface addNoteViewInterface, final List<Farm> farmList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (int i=0;i<farmList.size();i++){
                    db.farmDao().insertFarm(farmList.get(i));

                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                if (addNoteViewInterface!=null)
                addNoteViewInterface.onFarmAdded();
            }

            @Override
            public void onError(Throwable e) {
                if (addNoteViewInterface!=null)
                addNoteViewInterface.onFarmInsertError(e);
            }
        });
    }


    public void addFarm(final OnFarmInserted onFarmInserted, final Farm farmList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                long id=db.farmDao().insertFarm(farmList);
                if (onFarmInserted!=null)
                onFarmInserted.onInserted(id);

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


    public void updateFarm( final Farm farm) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                    db.farmDao().update(farm);


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

    public void updateFarm( String offlineFarmID,String farmId,String isOfflineAdded,String ownerId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.farmDao().update(offlineFarmID,farmId,isOfflineAdded,ownerId);


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
    public void updateFarmS( String offlineFarmID,String farmId,String isUploaded) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.farmDao().updateS(offlineFarmID,farmId,isUploaded,"N");


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
                db.farmDao().deleteAllFarms();
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

    public void getFarm(final FarmLoadListener FarmLoadListener, String farmId) {
        db.farmDao().getFarm(farmId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Farm>() {
            @Override
            public void accept(Farm farms) throws Exception {
                //mainViewInterface.onFarmLoaded(farms);
                FarmLoadListener.onFarmLoader(farms);
            }
        });
    }
    public interface FarmFetchByFarmerId{
        public void onFarmLoaded(Farm farm);
    }

    public void getFarmerFarm(final FarmFetchByFarmerId farmFetchByFarmerId, String farmerId) {
        db.farmDao().getFarmByFarmerId(farmerId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Farm>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(Farm farmerT) {
                        // update the UI

                        farmFetchByFarmerId.onFarmLoaded(farmerT);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        farmFetchByFarmerId.onFarmLoaded(null);
                    }
                });

    }
}
