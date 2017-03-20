package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import stbvideocall.jhonelee.xyt.com.aini_app.data.response.GankData;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.model.HttpService;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/17.
 */

public class QueryActionCreator extends RxActionCreator{

    private static final int DEFAULT_COUNT = 17;

    @Inject
    public QueryActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    public void query(String querytext, final int page){

        final RxAction rxAction = newRxAction(ActionType.QUERY_GANK);
        if(hasRxAction(rxAction)){
            return;
        }

        addRxAction(rxAction, HttpService.Factory.getGankService()
        .queryGank(querytext,DEFAULT_COUNT,page)
        .map(new Func1<GankData, List<GankNormalItem>>() {
            @Override
            public List<GankNormalItem> call(GankData gankData) {
                if(null == gankData || null == gankData.results || 0 == gankData.results.size()) {
                    return null;
                }
                return GankNormalItem.newGankList(gankData.results,page);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<GankNormalItem>>() {
            @Override
            public void call(List<GankNormalItem> list) {
                rxAction.getData().put(Key.PAGE,page);
                rxAction.getData().put(Key.QUERY_RESULT, list);
                postRxAction(rxAction);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                postError(rxAction,throwable);
            }
        }));
    }
}
