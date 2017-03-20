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

public class AndroidStore extends RxStore{

    public static final String ID = "AndroidStore";

    private int mPage;
    private List<GankNormalItem> mGankList;

    @Inject
    public AndroidStore(Dispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void onRxAction(RxAction action) {

        switch (action.getType()){
            case ActionType.GET_ANDROID_LIST:
                mPage = action.get(Key.PAGE);
                mGankList = action.get(Key.GANK_LIST);
                break;
            default:
                return;
        }
        postChange(new RxStoreChange(ID,action));
    }

    public int getPage() {
        return mPage;
    }
    public List<GankNormalItem> getGankList() {
        return mGankList;
    }

}
