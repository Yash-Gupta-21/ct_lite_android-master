package com.i9930.croptrails.RoomDatabase.ResourceCost;

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
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;

public class ResourceCacheManager {

    private Context context;
    private static ResourceCacheManager instance;
    private AppDatabase db;

    public static ResourceCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new ResourceCacheManager(context);
        }
        return instance;
    }

    public ResourceCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }


    public void getResource(final ResourceFetchListener listener, String farmId) {
      /*  db.resourceDaoInterface().getResources(farmId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResourceCostT>() {
            @Override
            public void accept(ResourceCostT resourceCost) throws Exception {
                listener.onResourceLoaded(resourceCost);
            }
        });*/


        db.resourceDaoInterface().getResources(farmId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ResourceCostT>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable

                    }

                    @Override
                    public void onSuccess(ResourceCostT timelines) {
                        // update the UI
                        listener.onResourceLoaded(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        Log.e("TestActivity", "exception getting coupons", e);
                        listener.onResourceLoaded(null);
                    }
                });

    }


    public void getAllResources(final AllResourceFetchListener listener) {
        db.resourceDaoInterface().getResources().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ResourceCostT>>() {
            @Override
            public void accept(List<ResourceCostT> resourceCostTList) throws Exception {
                listener.onAllResourceLoaded(resourceCostTList);
            }
        });
    }

    public void addResource( final ResourceCostT resourceCost) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.resourceDaoInterface().insert(resourceCost);

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

    public interface ResourceAddListener{
        public void onResourceAdded();
    }


    public void addResource( ResourceAddListener listener,final ResourceCostT resourceCost) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.resourceDaoInterface().insert(resourceCost);

                //}
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
            listener.onResourceAdded();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }


    public void deleteAllResources(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.resourceDaoInterface().deleteAllResourceData();

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


    public void update( final ResourceCostT resourceCost){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.resourceDaoInterface().update(resourceCost);



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
                db.resourceDaoInterface().update(offlineFarmId,farmId);



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
