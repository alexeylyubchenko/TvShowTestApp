package alexey.tvshowapptest.views.fragments;

import android.os.Parcelable;

/**
 * Created by Alexey Lyubchenko.
 */

public interface View {
    void showLoading();
    void hideLoading();
    void showMessage(String message);

    void onDataLoadSucceed();
    void setRecyclerViewState(Parcelable recyclerViewState);
}
