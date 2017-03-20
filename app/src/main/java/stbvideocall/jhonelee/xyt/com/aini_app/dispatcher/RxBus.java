package stbvideocall.jhonelee.xyt.com.aini_app.dispatcher;


import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2017/3/13.
 */

public class RxBus {
    private static RxBus instance;
    private final Subject sub = new SerializedSubject<>(PublishSubject.create());

    private RxBus(){
    }
    public synchronized static RxBus getInstance(){
        if(instance == null){
            instance = new RxBus();
        }
        return instance;
    }

    //发送事件
    public void  send(Object o){
        sub.onNext(o);
    }

    public Observable<Object> get(){
        return sub;
    }
}
