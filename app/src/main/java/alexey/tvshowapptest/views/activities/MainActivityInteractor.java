package alexey.tvshowapptest.views.activities;

import android.support.v4.app.DialogFragment;

import alexey.tvshowapptest.views.base.BaseFragment;

/**
 * Created by Alexey Lyubchenko.
 */

public interface MainActivityInteractor {
    void showLoadingProgress();
    void hideLoadingProgress();
    void showMessage(String message);
    void hideKeyBoard();

    void switchFragment(BaseFragment fragment, String tag);
}
