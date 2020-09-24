package turkuvaz.general.turkuvazgeneralwidgets.AuthorsSnap.Utils;

/**
 * Listener to notify snapping
 **/
public interface OnSnapListener {

  /**
   * Called when RecyclerView is snapped
   *
   * @param position snapped position
   */
  void snapped(int position);
}
