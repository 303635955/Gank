package stbvideocall.jhonelee.xyt.com.aini_app.dispatcher;


import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxAction;

/**
 * This interface must be implemented by the store
 */
public interface RxActionDispatch {

  void onRxAction(RxAction action);
}
