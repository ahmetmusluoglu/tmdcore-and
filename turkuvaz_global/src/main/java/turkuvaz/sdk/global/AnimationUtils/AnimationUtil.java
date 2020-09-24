package turkuvaz.sdk.global.AnimationUtils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import androidx.recyclerview.widget.RecyclerView;

public class AnimationUtil {
    public static void animate(RecyclerView.ViewHolder holder) {
        AnimatorSet animatorSet = new AnimatorSet();
        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animatorTransilateY = ObjectAnimator.ofFloat(holder.itemView, "translationY",  400, 0);
        animatorTransilateY.setDuration(650);

        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animatorTransilateX = ObjectAnimator.ofFloat(holder.itemView, "translationX", 200, 0);
        animatorTransilateX.setDuration(650);

        @SuppressLint("ObjectAnimatorBinding") ObjectAnimator rotation = ObjectAnimator.ofFloat(holder.itemView, "rotation", 360);
        rotation.setDuration(650);
        if (Common.anim_type == Common.BOTTOM) {
            animatorSet.playTogether(animatorTransilateY);
            animatorSet.start();
        } else if (Common.anim_type == Common.LEFT) {
            animatorSet.playTogether(animatorTransilateX);
            animatorSet.start();
        } else if (Common.anim_type == Common.RIGHT) {
            animatorSet.playTogether(rotation);
            animatorSet.start();
        }
    }
}