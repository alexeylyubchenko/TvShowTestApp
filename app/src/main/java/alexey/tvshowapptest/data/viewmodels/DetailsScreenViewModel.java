package alexey.tvshowapptest.data.viewmodels;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;

import alexey.rxnetwork.RxNetwork;
import alexey.tvshowapptest.data.api.MovieDBApi;
import alexey.tvshowapptest.data.models.TvShow;
import alexey.tvshowapptest.data.viewmodels.base.ViewModel;
import alexey.tvshowapptest.utils.Constants;
import alexey.tvshowapptest.views.fragments.View;
import dagger.Module;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexey Lyubchenko.
 */

@Module
public class DetailsScreenViewModel extends ViewModel {

    private TvShow mTvShow;
    public ObservableField<Integer> similarLltVisibility = new ObservableField<>();

    @Inject
    public DetailsScreenViewModel(@NonNull Context context, @NonNull RxNetwork rxNetwork, @NonNull MovieDBApi api) {
        super(context, rxNetwork, api);
        similarLltVisibility.set(android.view.View.GONE);
    }

    public void setTvShow(@NonNull TvShow tvShow) {
        this.mTvShow = tvShow;
    }

    public void fetchSimilarTvShows() {
        if(mTvShow == null) {
            return;
        }

        Disposable disposable = mMovieDBApi.getSimilarTvShows(mTvShow.getId(),
                                                                Constants.API_KEY,
                                                                Constants.API_PARAM_LANG_VALUE,
                                                                currentPage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doFinally(() -> {
                    loadingNow = false;
                })
                .subscribe(
                        response -> {
                            if(response != null && response.getTvShows() != null) {

                                if(currentPage == 1 && response.getTvShows().size() > 0) {
                                    similarLltVisibility.set(android.view.View.VISIBLE);
                                }

                                totalPages = response.getTotalPages();
                                updateTvShowsList(response.getTvShows());
                            }
                        }, throwable -> {
                            mView.showMessage(throwable.getMessage());
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void loadDataFromNetwork() {
        fetchSimilarTvShows();
    }

}
