package alexey.tvshowapptest.data.viewmodels.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;

import alexey.rxnetwork.RxNetwork;
import alexey.tvshowapptest.R;
import alexey.tvshowapptest.data.api.MovieDBApi;
import alexey.tvshowapptest.data.models.TvShow;
import alexey.tvshowapptest.utils.Constants;
import alexey.tvshowapptest.views.fragments.View;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Alexey Lyubchenko.
 */

public abstract class ViewModel<T extends View> extends Observable {

    protected CompositeDisposable mCompositeDisposable;
    private Context mContext;
    protected MovieDBApi mMovieDBApi;
    protected T mView;

    private RxNetwork mRxNetwork;

    private List<TvShow> mTvShows;
    protected int currentPage = 1;
    protected boolean loadingNow = false;
    protected int totalPages = 1;

    protected abstract void loadDataFromNetwork();

    public ViewModel(@NonNull Context context, @NonNull RxNetwork rxNetwork, @NonNull MovieDBApi api) {
        mContext = context;
        mRxNetwork = rxNetwork;
        mMovieDBApi = api;

        mCompositeDisposable = new CompositeDisposable();
    }

    public void detachView() {
        if(mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }

        mCompositeDisposable = null;
        mContext = null;
        mView = null;
    }

    public void attachView(T view) {
        mView = view;
        mTvShows = new ArrayList<>();

        subscribeForNetworkStateUpdates();
    }

    private void subscribeForNetworkStateUpdates() {
        Disposable disposable = mRxNetwork
                .getNetworkStateUpdates()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((isConnected) -> {
                            if(!isConnected) {
                                onNoInternetConnection();
                            }
                        },
                        throwable -> {
                            if(mView != null) {
                                mView.showMessage(throwable.getMessage());
                            }
                        }
                );

        mCompositeDisposable.add(disposable);
    }

    private void onNoInternetConnection() {
        mView.showMessage(mContext.getString(R.string.no_internet));
    }

    protected void updateTvShowsList(List<TvShow> list) {
        if(list != null && list.size() == 0 && currentPage == 1) {
            mView.showMessage(mContext.getString(R.string.no_results));
        }

        mTvShows.clear();
        mTvShows.addAll(list);
        setChanged();
        notifyObservers();
        mView.onDataLoadSucceed();
    }

    public List<TvShow> getTvShows() {
        return mTvShows;
    }

    public void loadNextPageIfNeeded(LinearLayoutManager llm, int delta) {
        int pastVisiblesItems, visibleItemCount, totalItemCount;

        if (!loadingNow) {
            if (delta > 0) {
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loadingNow = true;
                    currentPage++;
                    if(currentPage < totalPages) {
                        loadDataFromNetwork();
                    }
                }
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle, Parcelable recyclerViewState, TvShow[] dataList) {
        if(recyclerViewState != null && dataList != null && dataList.length > 0) {
            bundle.putParcelableArray(Constants.SAVED_STATE_DATA_LIST, dataList);
            bundle.putParcelable(Constants.SAVED_RECYCLER_VIEW_STATE, recyclerViewState);
            bundle.putInt(Constants.SAVED_STATE_CURRENT_PAGE_KEY, currentPage);
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if(savedInstanceState.containsKey(Constants.SAVED_RECYCLER_VIEW_STATE)) {
                mView.setRecyclerViewState(savedInstanceState.getParcelable(Constants.SAVED_RECYCLER_VIEW_STATE));
            }

            if(savedInstanceState.containsKey(Constants.SAVED_STATE_CURRENT_PAGE_KEY)) {
                currentPage = savedInstanceState.getInt(Constants.SAVED_STATE_CURRENT_PAGE_KEY);
            }

            if(savedInstanceState.containsKey(Constants.SAVED_STATE_DATA_LIST)) {
                TvShow[] array = (TvShow[]) savedInstanceState.getParcelableArray(Constants.SAVED_STATE_DATA_LIST);
                if(array != null) {
                    List list = Arrays.asList(array);
                    updateTvShowsList(list);
                }
            }
        }
    }
}
