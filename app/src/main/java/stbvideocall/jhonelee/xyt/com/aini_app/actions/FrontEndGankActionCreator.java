package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.data.GankType;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FrontEndGankActionCreator extends CategoryGankActionCreator{

    @Inject
    public FrontEndGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    protected String getActionId() {
        return ActionType.GET_FRONT_END_LIST;
    }

    public void getFrontEndList(final int page){
        getGankList(GankType.FRONTEND,page);
    }

}
