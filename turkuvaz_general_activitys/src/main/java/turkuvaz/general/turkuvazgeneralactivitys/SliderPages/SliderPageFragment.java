package turkuvaz.general.turkuvazgeneralactivitys.SliderPages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import turkuvaz.general.turkuvazgeneralactivitys.R;

/**
 * Created by Ahmet MUŞLUOĞLU on 9/22/2020.
 */
public class SliderPageFragment extends Fragment {
    public static final String ARG_OBJECT = "object";


    static SliderPageFragment newInstance(int someInt) {
        SliderPageFragment sliderPageFragment = new SliderPageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        sliderPageFragment.setArguments(args);

        return sliderPageFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_news_single_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
//        ((TextView) view.findViewById(android.R.id.text1))
//                .setText(Integer.toString(args.getInt(ARG_OBJECT)));
    }
}
