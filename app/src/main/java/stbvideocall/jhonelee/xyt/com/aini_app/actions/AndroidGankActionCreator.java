package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.data.GankType;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/14.
 */

public class AndroidGankActionCreator extends CategoryGankActionCreator{

    @Inject
    public AndroidGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    protected String getActionId() {
        return ActionType.GET_ANDROID_LIST;
    }

    public void getAndroidList(final int page){
        getGankList(GankType.ANDROID,page);
    }

}
