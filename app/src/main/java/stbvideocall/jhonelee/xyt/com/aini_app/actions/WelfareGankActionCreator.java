package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.data.GankType;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/14.
 */

public class WelfareGankActionCreator extends CategoryGankActionCreator{

    @Inject
    public WelfareGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    protected String getActionId() {
        return ActionType.GET_WELFARE_LIST;
    }

    public void getWelfareList(final int page){
        getGankList(GankType.WELFARE,page);
    }

}
