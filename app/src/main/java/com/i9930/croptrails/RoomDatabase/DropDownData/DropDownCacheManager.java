package com.i9930.croptrails.RoomDatabase.DropDownData;

import android.content.Context;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

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
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrFetchListener;

public class DropDownCacheManager {


    public interface  OnFarmerClusterFetchListener{
        public void onDataLoaded(DropDownT dropDown);
    }

    public interface  DropDownAddListener{
        public void onDropDownAdded();
    }

    static DropDownCacheManager cacheManager;
    Context context;
    AppDatabase db;

    public static DropDownCacheManager getInstance(Context context){
        if (cacheManager==null){
            cacheManager=new DropDownCacheManager(context);
        }
        return cacheManager;
    }

    private DropDownCacheManager(Context context){
        this.context=context;
        db=AppDatabase.getAppDatabase(context);
    }



    public void getAllChemicalUnits(final DropDownFetchListener chemicalUnitFetchListener) {
        db.chemicalUnitDaoInterface().getChemicalUnits().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<DropDownT>>() {
            @Override
            public void accept(List<DropDownT> chemicalUnitList) throws Exception {
                chemicalUnitFetchListener.onChemicalUnitLoaded(chemicalUnitList);
            }
        });
    }

    public void getAllChemicalUnits(final DropDownFetchListener chemicalUnitFetchListener,String[] where) {
        db.chemicalUnitDaoInterface().getWhere(where).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<DropDownT>>() {
            @Override
            public void accept(List<DropDownT> chemicalUnitList) throws Exception {
                chemicalUnitFetchListener.onChemicalUnitLoaded(chemicalUnitList);
            }
        });
    }

    public void getDataByType(final OnFarmerClusterFetchListener listener, String data_type) {
        db.chemicalUnitDaoInterface().getFarmerOrcLusterList(data_type).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<DropDownT>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(DropDownT timelines) {
                        // update the UI
                        listener.onDataLoaded(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        listener.onDataLoaded(null);
                    }
                });

    }
    public void addChemicalUnit( final DropDownT chemicalUnit) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                    db.chemicalUnitDaoInterface().insert(chemicalUnit);

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

    public void addChemicalUnit( final DropDownT chemicalUnit,DropDownAddListener listener) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.chemicalUnitDaoInterface().insert(chemicalUnit);

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
                db.chemicalUnitDaoInterface().deleteAllChemicals();
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
                int i=db.chemicalUnitDaoInterface().deleteDD(type.trim());
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

    public void updateFarm( final DropDownT dropDownT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.chemicalUnitDaoInterface().update(dropDownT);


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
