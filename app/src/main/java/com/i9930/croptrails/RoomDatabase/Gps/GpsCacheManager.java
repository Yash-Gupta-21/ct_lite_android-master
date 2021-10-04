package com.i9930.croptrails.RoomDatabase.Gps;

import android.content.Context;
import android.util.Log;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.i9930.croptrails.RoomDatabase.AppDatabase;

/*Always use a Thread, AsyncTask, or any worker threads to perform database operations in Room.*/

public class GpsCacheManager {
    private Context context;
    private static GpsCacheManager _instance;
    private AppDatabase db;

    public static GpsCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new GpsCacheManager(context);
        }
        return _instance;
    }

    public GpsCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getContacts(final GetSetInterface getSetInterface) {
        db.gpsDao().getAllContacts().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<GpsLog>>() {
            @Override
            public void accept(List<GpsLog> gpsLogList) throws Exception {
                getSetInterface.onContactsLoaded(gpsLogList);
            }
        });
    }

    public void addContact(final GetSetInterface getSetInterface, final String lat_cord, final String long_cord, final String sv_id, final String enter_date,final String time) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                GpsLog gpsLog = new GpsLog(lat_cord,long_cord,sv_id,enter_date,time);
                gpsLog.setTime(time);
                gpsLog.setEnter_date(enter_date);
                db.gpsDao().insertContact(gpsLog);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                getSetInterface.onContactsAdded();
            }

            @Override
            public void onError(Throwable e) {
                getSetInterface.onDataNotAvailable();
            }
        });
    }

    public void deleteAllGps(final GetSetInterface getSetInterface){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.gpsDao().deleteAllGps();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                Log.e("UploadGpsLogToServer","Deleted All Gps Data");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("UploadGpsLogToServer","Error to Delete All Gps Data");
            }
        });
    }

}
