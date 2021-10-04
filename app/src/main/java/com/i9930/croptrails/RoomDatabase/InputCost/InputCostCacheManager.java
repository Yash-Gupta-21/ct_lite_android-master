package com.i9930.croptrails.RoomDatabase.InputCost;

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
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;

public class InputCostCacheManager {

    private Context context;
    private static InputCostCacheManager inputCostCacheManager;
    private AppDatabase db;

    public static InputCostCacheManager getInstance(Context context){
        if (inputCostCacheManager==null){
            inputCostCacheManager=new InputCostCacheManager(context);
        }
        return inputCostCacheManager;
    }

    public InputCostCacheManager(Context context){
        this.context=context;
        db=AppDatabase.getAppDatabase(context);
    }



    public void getInputCost(final InputCostFetchListener listener, String farmId) {
        /*db.inputCostDaoInterface().getInputCost(farmId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<InputCostT>() {
            @Override
            public void accept(InputCostT inputCostT) throws Exception {
                listener.onInputCostLoaded(inputCostT);
            }
        });

*/
        db.inputCostDaoInterface().getInputCost(farmId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<InputCostT>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable
                    }

                    @Override
                    public void onSuccess(InputCostT timelines) {
                        // update the UI
                        listener.onInputCostLoaded(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        Log.e("TestActivity", "exception getting coupons", e);
                        listener.onInputCostLoaded(null);
                    }
                });

    }


    public void getAllInputCost(final AllInputCostFetchListener listener) {
        db.inputCostDaoInterface().getAllInputCosts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<InputCostT>>() {
            @Override
            public void accept(List<InputCostT> inputCostTList) throws Exception {
                listener.onAllInputCostLoaded(inputCostTList);
            }
        });
    }

    public interface InputCostAddListener{
        public void onCostAdded();
    }

    public void addInputCost(InputCostAddListener listener, final InputCostT inputCostT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.inputCostDaoInterface().insert(inputCostT);

                //}
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                listener.onCostAdded();
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    public void addInputCost( final InputCostT inputCostT) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                //  for (int i=0;i<harvestTList.size();i++){
                db.inputCostDaoInterface().insert(inputCostT);

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


    public void deleteAllInputCost(){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.inputCostDaoInterface().deleteAllCostData();

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


    public void update( final InputCostT inputCostT){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.inputCostDaoInterface().update(inputCostT);



            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                //listener.onResourceUpdated();
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
                db.inputCostDaoInterface().update(offlineFarmId,farmId);



            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                //listener.onResourceUpdated();
            }

            @Override
            public void onError(Throwable e) {
            }
        });

    }


}
