package roy.anubhav.home;

import android.app.Application;

import roy.anubhav.core.dagger.CoreComponent;
import roy.anubhav.core.dagger.DaggerCoreComponent;

public class ApplicationClass extends Application {

    private CoreComponent coreComponent=null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public CoreComponent getCoreComponent(){

        if(coreComponent==null)
            coreComponent = DaggerCoreComponent.create();

        return coreComponent;
    }

}
