package stbvideocall.jhonelee.xyt.com.aini_app.daggar.component;

import javax.inject.Singleton;

import dagger.Component;
import stbvideocall.jhonelee.xyt.com.aini_app.daggar.Module.AppModule;
import stbvideocall.jhonelee.xyt.com.aini_app.dispatcher.Dispatcher;
import stbvideocall.jhonelee.xyt.com.aini_app.util.SubscriptionManager;

/**
 * Created by Administrator on 2017/3/13.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {


    Dispatcher getDispatcher();

    SubscriptionManager getSubscriptionManager();
}
