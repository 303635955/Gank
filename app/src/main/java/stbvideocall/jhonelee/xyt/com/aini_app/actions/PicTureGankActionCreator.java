package stbvideocall.jhonelee.xyt.com.aini_app.actions;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.data.GankType;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/14.
 */

public class PicTureGankActionCreator extends CategoryGankActionCreator{

    @Inject
    public PicTureGankActionCreator(Dispatcher dispatcher, SubscriptionManager manager) {
        super(dispatcher, manager);
    }

    @Override
    protected String getActionId() {
        return ActionType.GET_PICTURE_LIST;
    }

    public void getPictureList(final int page){
        getGankList(GankType.WELFARE,page);
    }

}
