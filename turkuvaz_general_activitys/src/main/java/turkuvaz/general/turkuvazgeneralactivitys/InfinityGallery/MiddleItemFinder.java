package turkuvaz.general.turkuvazgeneralactivitys.InfinityGallery;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

public class MiddleItemFinder extends RecyclerView.OnScrollListener {

    private Context mContext;
    private LinearLayoutManager mLayoutManager;
    private MiddleItemCallback mCallback;
    private int mLastPosition = -1;

    public MiddleItemFinder(Context context, LinearLayoutManager layoutManager, MiddleItemCallback callback) {
        this.mContext = context;
        this.mLayoutManager = layoutManager;
        this.mCallback = callback;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        //TODO SCROLL BAŞLADIĞINDA - DURDUĞUNDA (SCROLL YAPILABILIYORSA)
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE && recyclerView.canScrollVertically(1)) {
            int firstVisible = mLayoutManager.findFirstVisibleItemPosition();
            int lastVisible = mLayoutManager.findLastVisibleItemPosition();
            int itemsCount = lastVisible - firstVisible + 1;
            int screenCenter = mContext.getResources().getDisplayMetrics().heightPixels / 2;
            int minCenterOffset = Integer.MAX_VALUE;
            int middleItemIndex = 0;

            for (int index = 0; index < itemsCount; index++) {
                View listItem = mLayoutManager.getChildAt(index);

                if (listItem == null)
                    return;

                int topOffset = listItem.getTop();
                int bottomOffset = listItem.getBottom();
                int centerOffset = Math.abs(topOffset - screenCenter) + Math.abs(bottomOffset - screenCenter);

                if (minCenterOffset > centerOffset) {
                    minCenterOffset = centerOffset;
                    middleItemIndex = index + firstVisible;
                }
            }

            //TODO AYNI ITEM'ı TEKRAR TEKRAR YOLLAMAMASI İÇİN KONTROL
            if (mLastPosition != middleItemIndex) {
                mLastPosition = middleItemIndex;
                if (mCallback != null)
                    mCallback.scrollFinished(middleItemIndex);
            }

            if (mCallback != null && firstVisible >= 4) {
                mCallback.jumpToTop(true);
            } else if (mCallback != null) {
                mCallback.jumpToTop(false);
            }
        }
    }

    public interface MiddleItemCallback {
        void scrollFinished(int middleElement);

        void jumpToTop(boolean isShow);
    }
}