package stbvideocall.jhonelee.xyt.com.aini_app.sotres;

import java.util.List;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.Key;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxAction;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankNormalItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;

/**
 * Created by Administrator on 2017/3/14.
 */

public class IOSStore extends RxStore{
    public static final String ID = "IOSStore";

    private int page;
    private List<GankNormalItem> mGankList;

    @Inject
    public IOSStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()){
            case ActionType.GET_IOS_LIST:
                page = action.get(Key.PAGE);
                mGankList = action.get(Key.GANK_LIST);

                postChange(new RxStoreChange(ID,action));
                break;
            default:
                break;
        }
    }

    public int getPage() {
        return page;
    }

    public List<GankNormalItem> getmGankList() {
        return mGankList;
    }
}
