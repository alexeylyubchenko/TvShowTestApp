package alexey.tvshowapptest.app.dagger.modules.views;

import alexey.tvshowapptest.app.dagger.scopes.FragmentScope;
import alexey.tvshowapptest.data.viewmodels.MainScreenViewModel;
import alexey.tvshowapptest.views.fragments.View;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexey Lyubchenko.
 */

@Module
public class MainScreenViewModule {

    View view;

    public MainScreenViewModule(View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    public View getView() {
        return view;
    }

}

