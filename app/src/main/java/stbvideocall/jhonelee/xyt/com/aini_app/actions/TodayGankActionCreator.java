package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import stbvideocall.jhonelee.xyt.com.aini_app.data.GankType;
import stbvideocall.jhonelee.xyt.com.aini_app.data.response.DateData;
import stbvideocall.jhonelee.xyt.com.aini_app.data.response.DayData;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankGirlImageItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankHeaderItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankItem;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.model.HttpService;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;


/**
 * Created by Administrator on 2017/3/8.
 */

public class TodayGankActionCreator extends RxActionCreator{

    //定义数据转化模板
    private static SimpleDateFormat sDataFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    @Inject
    public TodayGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    public void getTodayGank(){
        final RxAction rxAction = newRxAction(ActionType.GET_TODAY_GANK);
        if(hasRxAction(rxAction)) {
            return;
        }
        addRxAction(rxAction, HttpService.Factory.getGankService()
                .getDateHistory()
                .filter(new Func1<DateData, Boolean>() {
                    @Override
                    public Boolean call(DateData dateData) {
                        return (null != dateData && null != dateData.results && dateData.results.size() > 0);
                    }
                })
                .map(new Func1<DateData, Calendar>() {
                    @Override
                    public Calendar call(DateData dateData) {
                        Calendar calendar = Calendar.getInstance(Locale.CHINA);
                        try {
                            calendar.setTime(sDataFormat.parse(dateData.results.get(0)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            calendar = null;
                        }
                        return calendar;
                    }
                })
                .filter(new Func1<Calendar, Boolean>() {
                    @Override
                    public Boolean call(Calendar calendar) {
                        return null != calendar;
                    }
                })
                .flatMap(new Func1<Calendar, Observable<DayData>>() {
                    @Override
                    public Observable<DayData> call(Calendar calendar) {
                        return HttpService.Factory.getGankService()
                                .getDayGank(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                    }
                })
                .map(new Func1<DayData, List<GankItem>>() {
                    @Override
                    public List<GankItem> call(DayData dayData) {
                        return getGankList(dayData);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<GankItem>>() {
                    @Override
                    public void call(List<GankItem> gankList) {
                        rxAction.getData().put(Key.DAY_GANK, gankList);
                        postRxAction(rxAction);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        postError(rxAction, throwable);
                    }
                }));
    }

    private List<GankItem> getGankList(DayData dayData) {

        List<GankItem> gankList = new ArrayList<>();

        if (dayData == null || dayData.results == null) {
            return null;
        }
        //对数据进行处理，解析成你所需要的model
        if(dayData.results.welfareList != null && dayData.results.welfareList.size()>0){
            gankList.add(GankGirlImageItem.newImageItem(dayData.results.welfareList.get(0)));
        }

        if (null != dayData.results.androidList && dayData.results.androidList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.ANDROID));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.androidList));
        }
        if (null != dayData.results.iosList && dayData.results.iosList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.IOS));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.iosList));
        }
        if (null != dayData.results.frontEndList && dayData.results.frontEndList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.FRONTEND));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.frontEndList));
        }
        if (null != dayData.results.extraList && dayData.results.extraList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.EXTRA));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.extraList));
        }
        if (null != dayData.results.casualList && dayData.results.casualList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.CASUAL));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.casualList));
        }
        if (null != dayData.results.appList && dayData.results.appList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.APP));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.appList));
        }
        if (null != dayData.results.videoList && dayData.results.videoList.size() > 0) {
            gankList.add(new GankHeaderItem(GankType.VIDEO));
            gankList.addAll(GankNormalItem.newGankList(dayData.results.videoList));
        }

        return gankList;
    }

}
