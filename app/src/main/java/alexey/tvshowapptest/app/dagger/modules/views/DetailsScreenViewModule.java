package alexey.tvshowapptest.app.dagger.modules.views;

import alexey.tvshowapptest.app.dagger.scopes.FragmentScope;
import alexey.tvshowapptest.views.fragments.View;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Alexey Lyubchenko.
 */

@Module
public class DetailsScreenViewModule {

    View mView;

    public DetailsScreenViewModule(View view) {
        this.mView = view;
    }

    @FragmentScope
    @Provides
    public View getView() {
        return mView;
    }
}

