package turkuvaz.general.turkuvazgeneralactivitys.SliderPages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Ahmet MUŞLUOĞLU on 9/22/2020.
 */
public class NewsPagesAdapter extends FragmentStatePagerAdapter {

    public NewsPagesAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment mFragment = SliderPageFragment.newInstance(position);
        return mFragment;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
