package stbvideocall.jhonelee.xyt.com.aini_app.sotres;


import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.RxActionDispatch;

public abstract class RxStore implements RxActionDispatch {

  private final Dispatcher dispatcher;

  public RxStore(Dispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  public void register() {
    dispatcher.subscribeRxStore(this);
  }

  public void unregister() {
    dispatcher.unsubscribeRxStore(this);
  }

  protected void postChange(RxStoreChange change) {
    dispatcher.postRxStoreChange(change);
  }
}
