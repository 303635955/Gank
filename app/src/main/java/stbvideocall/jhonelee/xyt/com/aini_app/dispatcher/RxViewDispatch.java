package stbvideocall.jhonelee.xyt.com.aini_app.dispatcher;


import android.support.annotation.NonNull;


import stbvideocall.jhonelee.xyt.com.aini_app.actions.RxError;
import stbvideocall.jhonelee.xyt.com.aini_app.sotres.RxStoreChange;



public interface RxViewDispatch {

  /**
   * All the stores will call this event after they process an action and the store change it.
   * The view can react and request the needed data
   */
  void onRxStoreChanged(@NonNull RxStoreChange change);

  /**
   * Called when an error occur in some point of the flux flow.
   *
   * @param error {@link RxError} containing the information for that specific error
   */
  void onRxError(@NonNull RxError error);


}
