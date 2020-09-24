package turkuvaz.general.turkuvazgeneralwidgets.Programmes;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class Programmes extends RecyclerView implements ProgrammesAdapter.OnLoadMoreListener {

    public Programmes(@NonNull Context context) {
        super(context);
    }

    public Programmes(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Programmes(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnSelectedNewsListener(ProgrammesAdapter.onSelectedNewsListener onSelectedNewsListener) {
    }

    public void setOnStatusListener(onStatusListener onStatusListener) {
    }


    public void init() {
    }


    @Override
    public void onLoadMore() {
    }


    public interface onStatusListener {
        void onStartLoad();

        void onSuccess();

        void onError(String errorMessage);
    }
}