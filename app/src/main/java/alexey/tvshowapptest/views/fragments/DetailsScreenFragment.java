package alexey.tvshowapptest.views.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.View;

import java.util.Observable;

import alexey.tvshowapptest.adapters.SimilarTvShowsAdapter;
import alexey.tvshowapptest.app.AppEngine;
import alexey.tvshowapptest.app.dagger.components.DaggerDetailsScreenViewComponent;
import alexey.tvshowapptest.app.dagger.components.DetailsScreenViewComponent;
import alexey.tvshowapptest.app.dagger.modules.views.DetailsScreenViewModule;
import alexey.tvshowapptest.data.models.TvShow;
import alexey.tvshowapptest.data.viewmodels.DetailsScreenViewModel;
import alexey.tvshowapptest.data.viewmodels.ItemTvShowViewModel;
import alexey.tvshowapptest.databinding.FragmentDetailsScreenBinding;
import alexey.tvshowapptest.utils.Constants;
import alexey.tvshowapptest.views.base.BaseFragment;


public class DetailsScreenFragment extends BaseFragment<DetailsScreenViewModel> implements OnRecyclerItemClickListener {

    DetailsScreenViewComponent mDetailsScreenViewComponent;
    FragmentDetailsScreenBinding mFragmentDetailsScreenBinding;
    SimilarTvShowsAdapter mSimilarTvShowsAdapter;

    TvShow mTvShow;

    public DetailsScreenFragment() {
        // Required empty public constructor
    }

    public static DetailsScreenFragment newInstance(TvShow tvShow) {
        DetailsScreenFragment fragment = new DetailsScreenFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.SHOWS_ARG_KEY, tvShow);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(Constants.SHOWS_ARG_KEY)) {
            try {
                mTvShow = getArguments().getParcelable(Constants.SHOWS_ARG_KEY);
            } catch (Exception exp) {
                exp.printStackTrace();
            } finally {
                if(mTvShow == null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentDetailsScreenBinding = FragmentDetailsScreenBinding.inflate(inflater, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        mViewModel.setTvShow(mTvShow);
        mViewModel.fetchSimilarTvShows();

        return mFragmentDetailsScreenBinding.getRoot();
    }

    @Override
    protected void injectDagger() {
        mDetailsScreenViewComponent = DaggerDetailsScreenViewComponent.builder()
                .appComponent(AppEngine.getAppComponent())
                .detailsScreenViewModule(new DetailsScreenViewModule(this))
                .build();

        mDetailsScreenViewComponent.injectFragment(this);
    }

    @Override
    protected void setupView() {
        mFragmentDetailsScreenBinding.setViewModel(mViewModel);
        mFragmentDetailsScreenBinding.setTvShow(new ItemTvShowViewModel(mTvShow, null));
        setupRecyclerViewList(mFragmentDetailsScreenBinding.similarTvShowsRecyclerView);
    }

    @Override
    protected void updateObservableInfo(Observable o, Object arg) {
        if(o instanceof DetailsScreenViewModel) {
            mViewModel = (DetailsScreenViewModel) o;
            mSimilarTvShowsAdapter.addTvShows(mViewModel.getTvShows());
        }
    }

    @Override
    protected void setupRecyclerViewList(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        mSimilarTvShowsAdapter = new SimilarTvShowsAdapter(this);
        recyclerView.setAdapter(mSimilarTvShowsAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mViewModel.loadNextPageIfNeeded(llm, dx);
            }
        });
    }

    @Override
    public void onItemClicked(TvShow tvShow) {
        mMainActivityInteractor.switchFragment(DetailsScreenFragment.newInstance(tvShow), Constants.FRAGMENT_DETAILS_TAG);
    }

    @Override
    public void setRecyclerViewState(Parcelable recyclerViewState) {
        if(mFragmentDetailsScreenBinding != null && mSimilarTvShowsAdapter != null) {
            try {
                mFragmentDetailsScreenBinding.similarTvShowsRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outSate) {
        if(mViewModel != null && mFragmentDetailsScreenBinding != null && mSimilarTvShowsAdapter != null) {
            try {
                Parcelable recyclerViewState =
                        mFragmentDetailsScreenBinding.similarTvShowsRecyclerView.getLayoutManager().onSaveInstanceState();
                mViewModel.onSaveInstanceState(outSate, recyclerViewState, mSimilarTvShowsAdapter.getTvShowsArray());
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }

        super.onSaveInstanceState(outSate);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.onRestoreInstanceState(savedInstanceState);
    }
}
