package com.i9930.croptrails.RoomDatabase.Timeline;

import android.content.Context;
import android.util.Log;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import com.i9930.croptrails.RoomDatabase.AppDatabase;
import com.i9930.croptrails.RoomDatabase.DoneActivities.ActivityFetchListener;
import com.i9930.croptrails.RoomDatabase.DoneActivities.DoneActivity;

public class TimelineCacheManager {

    private Context context;
    private static TimelineCacheManager _instance;
    private AppDatabase db;

    public static TimelineCacheManager getInstance(Context context) {
        if (_instance == null) {
            _instance = new TimelineCacheManager(context);
        }
        return _instance;
    }

    public TimelineCacheManager(Context context) {
        this.context = context;
        db = AppDatabase.getAppDatabase(context);
    }

    public void getAllTimeline(final TimelineNotifyInterface tmainViewInterface, String farmId) {
        /*db.TimelineDao().getTimeline(farmId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Timeline>() {
                    @Override
                    public void accept(Timeline timelines) throws Exception {
                        tmainViewInterface.onTimelineLoaded(timelines);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e("TestActivity", "exception getting coupons", throwable);
                        tmainViewInterface.onTimelineLoaded(null);
                    }
                });*/


        db.TimelineDao().getTimeline(farmId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Timeline>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // add it to a CompositeDisposable

                    }

                    @Override
                    public void onSuccess(Timeline timelines) {
                        // update the UI
                        tmainViewInterface.onTimelineLoaded(timelines);
                    }                       @Override
                    public void onError(Throwable e) {
                        // show an error message
                        Log.e("TestActivity", "exception getting coupons", e);
                        tmainViewInterface.onTimelineLoaded(null);
                    }
                });




       /* Timeline timelines=db.TimelineDao().getTimeline(farmId);
        tmainViewInterface.onTimelineLoaded(timelines);*/

    }



    public void addTimeline(final TimelineNotifyInterface addNoteViewInterface, final Timeline timelineList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.TimelineDao().insertTimeline(timelineList);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                if (addNoteViewInterface!=null)
                addNoteViewInterface.onTimelineAdded();

            }

            @Override
            public void onError(Throwable e) {
                Log.e("TestActivity", "exception add timeline", e);
                if (addNoteViewInterface!=null)
                addNoteViewInterface.onTimelineInsertError(e);
            }
        });
    }

  /*  public interface TimelineAddListener{
        public void onTimelineAdded();
    }

    public void addTimeline(final TimelineAddListener addNoteViewInterface, final Timeline timelineList) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {

                db.TimelineDao().insertTimeline(timelineList);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onComplete() {
                addNoteViewInterface.onTimelineAdded();
            }

            @Override
            public void onError(Throwable e) {
                addNoteViewInterface.onTimelineInsertError(e);
            }
        });
    }
    */

    public void deleteAllTimeline(/*final TimelineNotifyInterface tnotifyInterface*/) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.TimelineDao().deleteAllTimelines();
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onComplete() {
                //tnotifyInterface.TimelineDeleted();
            }

            @Override
            public void onError(Throwable e) {
                // tnotifyInterface.TimelineDeletionFaild();
            }
        });
    }

    public void update(final Timeline timeline) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.TimelineDao().updateTimeline(timeline);


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

    public void update(String offlineFarmId,String farmId) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                db.TimelineDao().updateTimeline(offlineFarmId,farmId);


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
