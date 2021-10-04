package com.i9930.croptrails.RoomDatabase.VisitStructure;

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

public class VisitCacheManager {

    private Context context;
    private static VisitCacheManager instance;
    private AppDatabase db;

    public static VisitCacheManager getInstance(Context context) {
        if (instance == null) {
            instance = new VisitCacheManager(context);
        }
        return instance;
    }

    public VisitCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }


    public void getAllVisits(final VisitNotifyInterface mainViewInterface) {
        db.visitDao().getVisits().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<VisitTable>>() {
            @Override
            public void accept(List<VisitTable> visitTableList) throws Exception {
                mainViewInterface.onVisitLoaded(visitTableList);
            }
        });
    }

    public void getVisitStructure(final VisitNotifyInterface mainViewInterface) {
        db.visitDao().getVisitStructure().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<VisitTable>>() {
            @Override
            public void accept(List<VisitTable> visitTableList) throws Exception {
                mainViewInterface.onVisitLoaded(visitTableList);
            }
        });
    }

    public void addVisits(final VisitNotifyInterface addNoteViewInterface, final List<VisitTable> visitTableList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                for (int i=0;i<visitTableList.size();i++){
                    db.visitDao().insertVisits(visitTableList.get(i));

                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                addNoteViewInterface.onVisitAdded();
            }

            @Override
            public void onError(Throwable e) {
                addNoteViewInterface.onVisitInsertError(e);
            }
        });
    }

    public void addSingleVisit(final VisitNotifyInterface addNoteViewInterface, final VisitTable visitTable) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                    db.visitDao().insertVisits(visitTable);

            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                addNoteViewInterface.onVisitAdded();
            }

            @Override
            public void onError(Throwable e) {
                addNoteViewInterface.onVisitInsertError(e);
            }
        });
    }

    public void deleteAllVisitStructure(/*final VisitNotifyInterface notifyInterface*/){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.visitDao().deleteAllVisits();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                /*notifyInterface.visitDeleted();*/
            }

            @Override
            public void onError(Throwable e) {
                /*notifyInterface.visitDeletionFailed();*/
            }
        });
    }




}
