package stbvideocall.jhonelee.xyt.com.aini_app.actions;


import java.util.List;

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
 * Created by Administrator on 2017/3/14.
 */

abstract class CategoryGankActionCreator extends RxActionCreator{
    private static final int DEFAULT_PAGE_COUNT = 17;


    public CategoryGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    protected int getPageCount(){
        return DEFAULT_PAGE_COUNT;
    }

    protected abstract String getActionId();

    protected void getGankList(final String category,final int page){
        final RxAction rxAction = newRxAction(getActionId());
        if(hasRxAction(rxAction)){
            return;
        }
        addRxAction(rxAction, HttpService.Factory.getGankService()
                .getGank(category, getPageCount(), page)
                .map(new Func1<GankData, List<GankNormalItem>>() {
                    @Override
                    public List<GankNormalItem> call(GankData gankData) {
                        if(null == gankData || null == gankData.results || 0 == gankData.results.size()) {
                            return null;
                        }
                        return GankNormalItem.newGankList(gankData.results, page);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<GankNormalItem>>() {
                    @Override
                    public void call(List<GankNormalItem> gankNormalItems) {
                        rxAction.getData().put(Key.GANK_LIST, gankNormalItems);
                        rxAction.getData().put(Key.PAGE, page);
                        postRxAction(rxAction);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        postError(rxAction, throwable);
                    }
                }));
    }
}
