package turkuvaz.general.turkuvazgeneralwidgets.AuthorsSnap.Utils;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * abstract class to delegate {@link MultiSnapHelper} methods.
 **/
abstract class BaseSnapHelperDelegator {

  /**
   * delegate method of
   * {@link MultiSnapHelper#calculateDistanceToFinalSnap(RecyclerView.LayoutManager, View)}
   *
   * @return the distance to the gravitated snap position
   */
  abstract int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager,
      @NonNull View targetView);

  /**
   * delegate method of {@link MultiSnapHelper#findSnapView(RecyclerView.LayoutManager)}
   *
   * @return the view to snap when RecyclerView is in the idle state
   */
  abstract View findSnapView(RecyclerView.LayoutManager layoutManager);

  /**
   * delegate method of
   * {@link MultiSnapHelper#findTargetSnapPosition(RecyclerView.LayoutManager, int, int)}
   *
   * @return the view to snap when RecyclerView is fling
   */
  abstract int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX,
      int velocityY);

  /**
   * set a listener to return a position of the snapped view
   *
   * @param listener {@link OnSnapListener}
   */
  abstract void setListener(OnSnapListener listener);
}
