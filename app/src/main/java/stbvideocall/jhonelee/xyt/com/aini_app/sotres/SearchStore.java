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

public class SearchStore extends RxStore{

    public static final String ID = "SearchStore";

    private int mPage;
    private List<GankNormalItem> mGankList;

    @Inject
    public SearchStore(Dispatcher dispatcher) {
        super(dispatcher);
    }
    @Override
    public void onRxAction(RxAction action) {

        switch (action.getType()){
            case ActionType.QUERY_GANK:
                mPage = action.get(Key.PAGE);
                mGankList = action.get(Key.QUERY_RESULT);
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
