package com.i9930.croptrails.RoomDatabase.DropDownData2;

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

public class DropDownCacheManager2 {


    public interface  OnFarmerClusterFetchListener{
        public void onDataLoaded(DropDownT2 dropDown);
    }

    public interface  DropDownAddListener{
        public void onDropDownAdded();
    }

    static DropDownCacheManager2 cacheManager;
    Context context;
    AppDatabase db;

    public static DropDownCacheManager2 getInstance(Context context){
        if (cacheManager==null){
            cacheManager=new DropDownCacheManager2(context);
        }
        return cacheManager;
    }

    private DropDownCacheManager2(Context context){
        this.context=context;
        db=AppDatabase.getAppDatabase(context);
    }



    public void getAllChemicalUnits(final DropDownFetchListener2 chemicalUnitFetchListener) {
        db.chemicalUnitDaoInterface2().getChemicalUnits().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<DropDownT2>>() {
            @Override
            public void accept(List<DropDownT2> chemicalUnitList) throws Exception {
                chemicalUnitFetchListener.onChemicalUnitLoaded(chemicalUnitList);
            }
        });
    }

    public void getAllChemicalUnits(final DropDownFetchListener2 chemicalUnitFetchListener, String[] where) {
        db.chemicalUnitDaoInterface2().getWhere(where).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<DropDownT2>>() {
            @Override
            public void accept(List<DropDownT2> chemicalUnitList) throws Exception {
                chemicalUnitFetchListener.onChemicalUnitLoaded(chemicalUnitList);
            }
        });
    }

    public void getDataByType(final OnFarmerClusterFetchListener listener, String data_type) {
        db.chemicalUnitDaoInterface2().getFarmerOrcLusterList(data_type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DropDownT2>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(DropDownT2 timelines) {
                        // update the UI
                        Log.e("getDataByType","TYPE "+data_type+" DATA "+new Gson().toJson(timelines));
                        listener.onDataLoaded(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        listener.onDataLoaded(null);
                    }
                });

    }
    public void addChemicalUnit( final DropDownT2 chemicalUnit) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                    db.chemicalUnitDaoInterface2().insert(chemicalUnit);

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

    public void addChemicalUnit(final DropDownT2 chemicalUnit, DropDownAddListener listener) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.chemicalUnitDaoInterface2().insert(chemicalUnit);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                listener.onDropDownAdded();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void deleteAllChemicalUnit(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.chemicalUnitDaoInterface2().deleteAllChemicals();
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

    public void deleteViaType(String type){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                int i=db.chemicalUnitDaoInterface2().deleteDD(type.trim());
                Log.e("deleteViaType","DELETED "+i+" AND TYPE "+type);
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

    public void updateFarm( final DropDownT2 dropDownT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.chemicalUnitDaoInterface2().update(dropDownT);


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
