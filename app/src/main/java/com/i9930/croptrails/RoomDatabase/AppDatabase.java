package com.i9930.croptrails.RoomDatabase;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.AddFarmCDao;
import com.i9930.croptrails.RoomDatabase.AddFarmCache.AddFarmCache;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownDaoInterface;
import com.i9930.croptrails.RoomDatabase.DropDownData.DropDownT;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegDao;
import com.i9930.croptrails.RoomDatabase.CompRegistry.CompRegModel;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityDaoInterface;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownDaoInterface2;
import com.i9930.croptrails.RoomDatabase.DropDownData2.DropDownT2;
import com.i9930.croptrails.RoomDatabase.FarmTable.Farm;
import com.i9930.croptrails.RoomDatabase.FarmTable.FarmDaoInterface;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerDaoInterface;
import com.i9930.croptrails.RoomDatabase.Farmer.FarmerT;
import com.i9930.croptrails.RoomDatabase.Gps.GpsDao;
import com.i9930.croptrails.RoomDatabase.Gps.GpsLog;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestDaoInterface;
import com.i9930.croptrails.RoomDatabase.Harvest.HarvestT;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostDaoInterface;
import com.i9930.croptrails.RoomDatabase.InputCost.InputCostT;
import com.i9930.croptrails.RoomDatabase.Pld.OfflinePld;
import com.i9930.croptrails.RoomDatabase.Pld.PldDaoInterface;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceCostT;
import com.i9930.croptrails.RoomDatabase.ResourceCost.ResourceDaoInterface;
import com.i9930.croptrails.RoomDatabase.Task.SvTask;
import com.i9930.croptrails.RoomDatabase.Task.TaskDaoInterface;
import com.i9930.croptrails.RoomDatabase.Timeline.Timeline;
import com.i9930.croptrails.RoomDatabase.Timeline.TimelineDaoInterface;
import com.i9930.croptrails.RoomDatabase.Visit.Visit;
import com.i9930.croptrails.RoomDatabase.Visit.VrDaoInterface;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitDaoInterface;
import com.i9930.croptrails.RoomDatabase.VisitStructure.VisitTable;

//@Database(entities = {GpsLog.class, CompRegModel.class},version = 2,exportSchema = false)
@Database(entities = {GpsLog.class, CompRegModel.class,
        Farm.class, VisitTable.class, Timeline.class,
        HarvestT.class, Visit.class, InputCostT.class,
        ResourceCostT.class, DoneActivity.class, DropDownT.class, DropDownT2.class,
        AddFarmCache.class, OfflinePld.class,
        FarmerT.class, SvTask.class},version = 38,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;
    public abstract FarmDaoInterface farmDao();
    public abstract TimelineDaoInterface TimelineDao();
    public abstract CompRegDao compRegDao();
    public abstract GpsDao gpsDao();
    public abstract VisitDaoInterface visitDao();
    public abstract VrDaoInterface vrDaoInterface();
    public abstract TaskDaoInterface taskDaoInterface();
    public abstract HarvestDaoInterface harvestDaoInterface();

    public abstract ActivityDaoInterface activityDaoInterface();
    public abstract ResourceDaoInterface resourceDaoInterface();
    public abstract InputCostDaoInterface inputCostDaoInterface();
    public abstract DropDownDaoInterface chemicalUnitDaoInterface();
    public abstract DropDownDaoInterface2 chemicalUnitDaoInterface2();
    public abstract FarmerDaoInterface farmerDaoInterface();
    public abstract PldDaoInterface pldDaoInterface();

    public abstract AddFarmCDao addFarmCDao();
    private static String TAG="AppDatabase";
    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "room_database")
                            .fallbackToDestructiveMigration()
                            .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    public static void eraseDB(Context context,DbClearListener listener){

        try {
            AppDatabase db = getAppDatabase(context);
            Completable one = Completable.fromAction(() ->  db.clearAllTables());
            Completable.concatArray(one).observeOn(Schedulers.single()) // OFF UI THREAD
                    .doOnSubscribe(__ -> {
                        Log.w(TAG, "Begin transaction. " + Thread.currentThread().toString());
                    })
                    .doOnComplete(() -> {
                        Log.w(TAG, "Set transaction successful."  + Thread.currentThread().toString());
                    })
                    .doFinally(() -> {
                        Log.w(TAG, "End transaction."  + Thread.currentThread().toString());
                    })
                    .subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread()) // ON UI THREAD
                    .subscribeWith(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            Log.w(TAG, "onSubscribe."  + Thread.currentThread().toString());
                        }

                        @Override
                        public void onComplete() {
                            Log.w(TAG, "onComplete."  + Thread.currentThread().toString());
                            try {
                                if (listener!=null)
                                    listener.onDbCleared();
                            }catch (Exception e1){

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError." + Thread.currentThread().toString());
                            try {
                                if (listener!=null)
                                    listener.onDbClearFail();
                            }catch (Exception e2){}
                        }
                    });
        }catch (Exception e){
            Log.e(TAG, "onException." + Thread.currentThread().toString(),e);
        }

    }

    public interface DbClearListener{
        public void onDbCleared();
        public void onDbClearFail();

    }
}
