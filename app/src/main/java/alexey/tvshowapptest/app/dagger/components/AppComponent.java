package alexey.tvshowapptest.app.dagger.components;

import android.content.Context;
import android.net.NetworkInfo;

import javax.inject.Singleton;

import alexey.rxnetwork.RxNetwork;
import alexey.tvshowapptest.app.AppEngine;
import alexey.tvshowapptest.app.dagger.modules.ApiModule;
import alexey.tvshowapptest.app.dagger.modules.AppModule;
import alexey.tvshowapptest.app.dagger.modules.NetworkModule;
import alexey.tvshowapptest.app.dagger.modules.StorageModule;
import alexey.tvshowapptest.data.api.MovieDBApi;
import dagger.Component;

/**
 * Created by Alexey Lyubchenko.
 */

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, StorageModule.class, ApiModule.class})
public interface AppComponent {
    void injectApp(AppEngine appEngine);

    Context getContext();
    RxNetwork getRxNetwork();
    MovieDBApi getMovieDBApi();
}
