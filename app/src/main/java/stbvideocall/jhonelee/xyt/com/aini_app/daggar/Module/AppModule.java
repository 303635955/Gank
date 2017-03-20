package stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import stbvideocall.jhonelee.xyt.com.aini_app.RxFlux;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/13.
 */
@Module
public class AppModule {

    private final RxFlux rxFlux;

    public AppModule(Application application){
        rxFlux =RxFlux.init(application);
    }

    @Provides
    @Singleton
    Dispatcher getDispatcher(){
        return rxFlux.getDispatcher();
    }

    @Provides
    @Singleton
    SubscriptionManager getSubscriptionManager(){
        return rxFlux.getSubscriptionManager();
    }




}
