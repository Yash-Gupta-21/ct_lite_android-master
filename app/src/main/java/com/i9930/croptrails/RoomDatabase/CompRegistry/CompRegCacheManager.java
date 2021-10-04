package com.i9930.croptrails.RoomDatabase.CompRegistry;

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
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;

public class CompRegCacheManager {

    private Context context;
    private static CompRegCacheManager _instance;
    private AppDatabase db;

    public static CompRegCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new CompRegCacheManager(context);
        }
        return _instance;
    }

    public CompRegCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getCompRegData(final DatabaseResInterface databaseResInterface) {
        try {
            db.compRegDao().getAllCompRegData().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<CompRegModel>>() {
                @Override
                public void accept(List<CompRegModel> compRegModels) throws Exception {
                    try {
                        databaseResInterface.onGetAllCompRegData(compRegModels);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void getCompRegData(final CompRegListener databaseResInterface,String feature) {


        db.compRegDao().getFeature(feature).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<CompRegModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(CompRegModel timelines) {
                        // update the UI
                        databaseResInterface.onRegLoaded(timelines);

                    }

                    @Override
                    public void onError(Throwable e) {
                        // show an error message

                    }
                });


    }

    public void deleteAllData(/*final DatabaseResInterface databaseResInterface*/){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.compRegDao().deleteAllData();
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

    public void updateCompRegData(final DatabaseResInterface databaseResInterface, final String data, final String date,final String feature){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                CompRegModel compRegModel = new CompRegModel(data,date,feature);
                db.compRegDao().updateData(compRegModel);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                databaseResInterface.onUpdateCompRegData();
            }

            @Override
            public void onError(Throwable e) {
                databaseResInterface.onUpdateNotSuccess();
            }
        });
    }

    public void addCompRegData(final DatabaseResInterface databaseResInterface, final String data, final String date,final String feature) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                CompRegModel compRegModel = new CompRegModel(data,date,feature);
                db.compRegDao().insertCompRegData(compRegModel);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                if (databaseResInterface!=null)
                databaseResInterface.onCompRegDataAdded();
            }

            @Override
            public void onError(Throwable e) {
                if (databaseResInterface!=null)
                databaseResInterface.onDataNotAdded();
            }
        });


    }

    public interface CompRegListener{

        public void onRegLoaded(CompRegModel compRegModel);
    }

}
