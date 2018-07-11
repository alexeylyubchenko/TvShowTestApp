package alexey.tvshowapptest.app;

import android.app.Application;
import android.content.Context;

import alexey.tvshowapptest.app.dagger.components.AppComponent;
import alexey.tvshowapptest.app.dagger.components.DaggerAppComponent;
import alexey.tvshowapptest.app.dagger.modules.AppModule;

/**
 * Created by Alexey Lyubchenko.
 */

public class AppEngine extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        injectDagger();
    }

    private void injectDagger() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .build();
        appComponent.injectApp(this);
    }
}
