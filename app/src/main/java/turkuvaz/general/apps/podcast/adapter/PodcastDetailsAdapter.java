package turkuvaz.general.apps.podcast.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import turkuvaz.sdk.models.Models.podcast.PodcastResponse;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/25/2020.
 */
public class PodcastDetailsAdapter extends RecyclerView.Adapter {

    private static final byte TYPE_ONE = 0;
    private static final byte TYPE_TWO = 1;

    public PodcastDetailsAdapter(ArrayList<PodcastResponse> podcastResponse, Context activity, String detailsName, String detailsImageUrl) {
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO CHECK ITEM VIEW TYPE HERE
        return position == 0 ? TYPE_ONE : TYPE_TWO;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
