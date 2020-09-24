package turkuvaz.general.apps.podcast.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import turkuvaz.sdk.models.Models.podcast.Response;

/**
 * Created by Ahmet MUŞLUOĞLU on 2/24/2020.
 */
public class PodcastAdapter extends RecyclerView.Adapter {


    public PodcastAdapter(ArrayList<Response> responseList, Context activity) {
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
