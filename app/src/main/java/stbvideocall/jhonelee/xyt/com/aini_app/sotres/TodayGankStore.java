package stbvideocall.jhonelee.xyt.com.aini_app.sotres;

import java.util.List;

import javax.inject.Inject;

import stbvideocall.jhonelee.xyt.com.aini_app.actions.ActionType;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.Key;
import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxAction;
import stbvideocall.jhonelee.xyt.com.aini_app.data.ui.GankItem;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;

/**
 * Created by Administrator on 2017/3/6.
 */

public class TodayGankStore extends RxStore{



    public static final String ID = "TodayGankStore";

    private List<GankItem> mItems;

    @Inject
    public TodayGankStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void onRxAction(RxAction action) {
        switch (action.getType()) {
            case ActionType.GET_TODAY_GANK:
                mItems = action.get(Key.DAY_GANK);
                break;
            default:
                return;
        }
        postChange(new RxStoreChange(ID, action));
    }

    public List<GankItem> getItems() {
        return mItems;
    }
}
