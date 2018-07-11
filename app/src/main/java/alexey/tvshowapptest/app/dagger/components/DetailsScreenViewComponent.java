package alexey.tvshowapptest.app.dagger.components;

import alexey.tvshowapptest.app.dagger.modules.views.DetailsScreenViewModule;
import alexey.tvshowapptest.app.dagger.scopes.FragmentScope;
import alexey.tvshowapptest.data.viewmodels.DetailsScreenViewModel;
import alexey.tvshowapptest.views.fragments.DetailsScreenFragment;
import dagger.Component;

/**
 * Created by Alexey Lyubchenko.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = DetailsScreenViewModule.class)
public interface DetailsScreenViewComponent {
    void injectFragment(DetailsScreenFragment fragment);
    DetailsScreenViewModel getDetailsScreenViewModel();
}
