package turkuvaz.general.turkuvazgeneralwidgets.AuthorsSnap.Utils;

import android.view.View;

import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * {@link SnapHelperDelegator} for center gravity
 **/
class CenterSnapHelperDelegator extends SnapHelperDelegator {

  /**
   * Constructor
   *
   * @param snapCount the number of items to scroll over
   */
  CenterSnapHelperDelegator(int snapCount) {
    super(snapCount);
  }

  @Override
  int getDistance(RecyclerView.LayoutManager layoutManager, View targetView,
                  OrientationHelper helper) {
    final int childCenter = getChildPosition(targetView, helper);
    final int containerCenter = getContainerPosition(layoutManager, helper);
    return childCenter - containerCenter;
  }

  @Override
  int getContainerPosition(RecyclerView.LayoutManager layoutManager,
                           OrientationHelper helper) {
    return layoutManager.getClipToPadding() ? helper.getStartAfterPadding()
        + helper.getTotalSpace() / 2 : helper.getEnd() / 2;
  }

  @Override
  int getChildPosition(View targetView, OrientationHelper helper) {
    return helper.getDecoratedStart(targetView) + (helper.getDecoratedMeasurement(targetView) / 2);
  }

  @Override
  boolean shouldSkipTarget(View targetView, RecyclerView.LayoutManager layoutManager,
                           OrientationHelper helper, boolean forwardDirection) {
    return forwardDirection ? getDistance(layoutManager, targetView, helper) < 0
        : getDistance(layoutManager, targetView, helper) > 0;
  }
}
