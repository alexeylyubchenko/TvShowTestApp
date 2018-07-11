package alexey.tvshowapptest.data.viewmodels;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import alexey.rxnetwork.RxNetwork;
import alexey.tvshowapptest.data.api.MovieDBApi;
import alexey.tvshowapptest.data.viewmodels.base.ViewModel;
import alexey.tvshowapptest.utils.Constants;
import alexey.tvshowapptest.views.fragments.View;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexey Lyubchenko.
 */

public class MainScreenViewModel extends ViewModel {

    @Inject
    public MainScreenViewModel(@NonNull Context context, @NonNull RxNetwork rxNetwork, @NonNull MovieDBApi api) {
        super(context, rxNetwork, api);
    }

    public void fetchPopularTvShows() {
        mView.showLoading();

        Disposable disposable = mMovieDBApi.getPopularTvShows(Constants.API_KEY, Constants.API_PARAM_LANG_VALUE, currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doFinally(() -> {
                    loadingNow = false;
                    mView.hideLoading();
                })
                .subscribe(
                        response -> {
                            if(response != null && response.getTvShows() != null) {
                                totalPages = response.getTotalPages();
                                updateTvShowsList(response.getTvShows());
                            }
                        },
                        throwable -> {
                            mView.showMessage(throwable.getMessage());
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void loadDataFromNetwork() {
        fetchPopularTvShows();
    }
}
