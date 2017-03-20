package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.data.GankType;
import stbvideocall.jhonelee.xyt.com.aini_app.data.entity.Gank;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/14.
 */

public class IOSGankActionCreator extends CategoryGankActionCreator{

    @Inject
    public IOSGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    protected String getActionId() {
        return ActionType.GET_IOS_LIST;
    }

    public void getIOSList(final int page){
        getGankList(GankType.IOS,page);
    }
}
