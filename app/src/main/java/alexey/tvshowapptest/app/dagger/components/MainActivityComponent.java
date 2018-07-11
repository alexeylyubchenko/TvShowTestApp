package alexey.tvshowapptest.app.dagger.components;

import alexey.tvshowapptest.app.dagger.modules.views.MainActivityModule;
import alexey.tvshowapptest.app.dagger.scopes.ActivityScope;
import alexey.tvshowapptest.views.activities.MainActivity;
import dagger.Component;

/**
 * Created by Alexey Lyubchenko.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MainActivityModule.class)
public interface MainActivityComponent {
    void injectActivity(MainActivity activity);
}
