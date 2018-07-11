package alexey.tvshowapptest.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import alexey.tvshowapptest.data.models.TvShow;
import alexey.tvshowapptest.data.viewmodels.ItemTvShowViewModel;
import alexey.tvshowapptest.databinding.TvshowItemBinding;
import alexey.tvshowapptest.views.fragments.OnRecyclerItemClickListener;

/**
 * Created by Alexey Lyubchenko.
 */

public class PopularTvShowsAdapter extends TvShowAdapter<PopularTvShowsAdapter.PopularTvShowViewHolder> {

    public PopularTvShowsAdapter(OnRecyclerItemClickListener listener) {
        super(listener);
    }

    @Override
    public PopularTvShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TvshowItemBinding tvshowItemBinding
                = TvshowItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new PopularTvShowViewHolder(tvshowItemBinding);
    }

    @Override
    public void onBindViewHolder(PopularTvShowViewHolder holder, int position) {
        if(mTvShows != null && mTvShows.size() > position) {
            holder.bindItem(mTvShows.get(position));
        }
    }

    class PopularTvShowViewHolder extends RecyclerView.ViewHolder {

        TvshowItemBinding mTvshowItemBinding;

        public PopularTvShowViewHolder(TvshowItemBinding itemBinding) {
            super(itemBinding.getRoot());
            mTvshowItemBinding = itemBinding;
        }

        public void bindItem(TvShow item) {
            if(mTvshowItemBinding.getItemTvShow() == null) {
                mTvshowItemBinding.setItemTvShow(new ItemTvShowViewModel(item, mListener));
            } else {
                mTvshowItemBinding.getItemTvShow().setTvShow(item);
            }
        }
    }
}
