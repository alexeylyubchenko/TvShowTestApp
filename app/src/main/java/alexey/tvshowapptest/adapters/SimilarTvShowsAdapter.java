package alexey.tvshowapptest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import alexey.tvshowapptest.data.models.TvShow;
import alexey.tvshowapptest.data.viewmodels.ItemTvShowViewModel;
import alexey.tvshowapptest.databinding.SimilarItemBinding;
import alexey.tvshowapptest.views.fragments.OnRecyclerItemClickListener;

/**
 * Created by Alexey Lyubchenko.
 */

public class SimilarTvShowsAdapter extends TvShowAdapter<SimilarTvShowsAdapter.SimilarTvShowsViewHolder> {

    public SimilarTvShowsAdapter(OnRecyclerItemClickListener listener) {
        super(listener);
    }

    @Override
    public SimilarTvShowsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SimilarItemBinding similarItemBinding =
                SimilarItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new SimilarTvShowsViewHolder(similarItemBinding);
    }

    @Override
    public void onBindViewHolder(SimilarTvShowsViewHolder holder, int position) {
        if(mTvShows != null && mTvShows.size() > position) {
            holder.bindItem(mTvShows.get(position));
        }
    }

    class SimilarTvShowsViewHolder extends RecyclerView.ViewHolder {

        SimilarItemBinding mSimilarItemBinding;

        public SimilarTvShowsViewHolder(SimilarItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.mSimilarItemBinding = itemBinding;
        }

        public void bindItem(TvShow tvShow) {
            if(mSimilarItemBinding.getItemTvShowModel() == null) {
                mSimilarItemBinding.setItemTvShowModel(new ItemTvShowViewModel(tvShow, mListener));
            } else {
                mSimilarItemBinding.getItemTvShowModel().setTvShow(tvShow);
            }
        }
    }
}
