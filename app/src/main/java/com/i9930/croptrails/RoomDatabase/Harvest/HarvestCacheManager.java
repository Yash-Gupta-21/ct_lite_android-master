package com.i9930.croptrails.RoomDatabase.Harvest;

import android.content.Context;
import android.util.Log;

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
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;

public class HarvestCacheManager {

    private Context context;
    private static HarvestCacheManager instance;
    private AppDatabase db;

    public static HarvestCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new HarvestCacheManager(context);
        }
        return instance;
    }

    public HarvestCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }


    public void getHarvest(final HarvestFetchListener listener, String farmId) {
      /*  db.harvestDaoInterface().getHarvest(farmId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<HarvestT>() {
            @Override
            public void accept(HarvestT harvest) throws Exception {
                listener.onHarvestLoaded(harvest);
            }

        });
*/

        db.harvestDaoInterface().getHarvest(farmId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<HarvestT>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(HarvestT timelines) {
                        // update the UI
                        listener.onHarvestLoaded(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        Log.e("TestActivity", "exception getting coupons", e);
                        listener.onHarvestLoaded(null);
                    }
                });


    }


    public void getAllHarvest(final AllHarvestFetchListener allHarvestFetchListener) {
        db.harvestDaoInterface().getAllHarvest().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<HarvestT>>() {
            @Override
            public void accept(List<HarvestT> harvestTList) throws Exception {
                allHarvestFetchListener.onAllHarvestLoaded(harvestTList);
            }
        });
    }

    public interface HarvestInsertListener{
        public void onHarvestInserted();
    }

    public void addVisits(HarvestInsertListener listener, final HarvestT harvestTList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
              //  for (int i=0;i<harvestTList.size();i++){
                    db.harvestDaoInterface().insertHarvest(harvestTList);

                //}
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                listener.onHarvestInserted();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void addVisits( final HarvestT harvestTList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.harvestDaoInterface().insertHarvest(harvestTList);

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
    public void updateHarvestData( final HarvestT harvestT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.harvestDaoInterface().update(harvestT);

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

    public void updateHarvestData( String offlineFarmId,String farmId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.harvestDaoInterface().update(offlineFarmId,farmId);

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
                db.harvestDaoInterface().deleteAllHarvest();
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
