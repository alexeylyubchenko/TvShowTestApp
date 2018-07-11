package alexey.tvshowapptest.views.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Observable;

import alexey.tvshowapptest.adapters.PopularTvShowsAdapter;
import alexey.tvshowapptest.app.AppEngine;
import alexey.tvshowapptest.app.dagger.components.DaggerMainScreenViewComponent;
import alexey.tvshowapptest.app.dagger.components.MainScreenViewComponent;
import alexey.tvshowapptest.app.dagger.modules.views.MainScreenViewModule;
import alexey.tvshowapptest.data.models.TvShow;
import alexey.tvshowapptest.data.viewmodels.MainScreenViewModel;
import alexey.tvshowapptest.databinding.FragmentMainScreenBinding;
import alexey.tvshowapptest.utils.Constants;
import alexey.tvshowapptest.views.base.BaseFragment;


public class MainScreenFragment extends BaseFragment<MainScreenViewModel> implements OnRecyclerItemClickListener {

    MainScreenViewComponent mMainScreenViewComponent;
    FragmentMainScreenBinding mFragmentMainScreenBinding;
    PopularTvShowsAdapter mPopularTvShowsAdapter;

    public MainScreenFragment() {
        // Required empty public constructor
    }

    public static MainScreenFragment newInstance() {
        MainScreenFragment fragment = new MainScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentMainScreenBinding = FragmentMainScreenBinding.inflate(inflater, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        mViewModel.fetchPopularTvShows();

        return mFragmentMainScreenBinding.getRoot();
    }


    @Override
    protected void injectDagger() {
        mMainScreenViewComponent = DaggerMainScreenViewComponent.builder()
                .appComponent(AppEngine.getAppComponent())
                .mainScreenViewModule(new MainScreenViewModule(this))
                .build();

        mMainScreenViewComponent.injectFragment(this);
    }

    @Override
    protected void setupView() {
        mPopularTvShowsAdapter = new PopularTvShowsAdapter(this);
        mFragmentMainScreenBinding.setViewModel(mViewModel);
        setupRecyclerViewList(mFragmentMainScreenBinding.populatTvShowList);
    }

    @Override
    protected void setupRecyclerViewList(RecyclerView recyclerView) {
        recyclerView.setAdapter(mPopularTvShowsAdapter);

        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mViewModel.loadNextPageIfNeeded(llm, dy);
            }
        });
    }

    @Override
    protected void updateObservableInfo(Observable o, Object arg) {
        if(o instanceof MainScreenViewModel) {
            mViewModel = (MainScreenViewModel) o;
            mPopularTvShowsAdapter.addTvShows(mViewModel.getTvShows());
        }
    }

    @Override
    public void onItemClicked(TvShow tvShow) {
        mMainActivityInteractor.switchFragment(DetailsScreenFragment.newInstance(tvShow), Constants.FRAGMENT_DETAILS_TAG);
    }

    @Override
    public void onSaveInstanceState(Bundle outSate) {
        if(mViewModel != null && mFragmentMainScreenBinding != null && mPopularTvShowsAdapter != null) {
            try {
                Parcelable recyclerViewState = mFragmentMainScreenBinding.populatTvShowList.getLayoutManager().onSaveInstanceState();
                mViewModel.onSaveInstanceState(outSate, recyclerViewState, mPopularTvShowsAdapter.getTvShowsArray());
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

    @Override
    public void setRecyclerViewState(Parcelable recyclerViewState) {
        if(mFragmentMainScreenBinding != null && mPopularTvShowsAdapter != null) {
            try {
                mFragmentMainScreenBinding.populatTvShowList.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
    }

}
