package turkuvaz.general.turkuvazgeneralwidgets.AllAuthors;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

public class spinnerDefaultSelection extends AppCompatSpinner {

    OnItemSelectedListener listener;
    int prevPos = 0;
    public spinnerDefaultSelection(Context context) {
        super(context);
    }

    public spinnerDefaultSelection(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public spinnerDefaultSelection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
        if (position == getSelectedItemPosition() && prevPos == position) {
            getOnItemSelectedListener().onItemSelected(null, null, position, 0);
        }
        prevPos = position;
    }
}