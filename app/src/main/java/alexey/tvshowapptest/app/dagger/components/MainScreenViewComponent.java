package alexey.tvshowapptest.app.dagger.components;

import alexey.tvshowapptest.app.dagger.modules.views.MainScreenViewModule;
import alexey.tvshowapptest.app.dagger.scopes.FragmentScope;
import alexey.tvshowapptest.data.viewmodels.MainScreenViewModel;
import alexey.tvshowapptest.views.fragments.MainScreenFragment;
import alexey.tvshowapptest.views.fragments.View;
import dagger.Component;

/**
 * Created by Alexey Lyubchenko.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = MainScreenViewModule.class)
public interface MainScreenViewComponent {
    void injectFragment(MainScreenFragment fragment);

    MainScreenViewModel getMainScreenViewModel();
}
