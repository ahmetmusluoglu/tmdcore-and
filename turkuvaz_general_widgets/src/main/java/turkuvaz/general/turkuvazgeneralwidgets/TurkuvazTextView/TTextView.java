package turkuvaz.general.turkuvazgeneralwidgets.TurkuvazTextView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


import turkuvaz.general.turkuvazgeneralwidgets.R;

/**
 * Created by turgay.ulutas on 16/05/2019
 * Turkuvaz,
 * Istanbul, TURKEY.
 */

public class TTextView extends TextView {

    public TTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public TTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        int fontFlag;
        int typeFlag;
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TTextView, 0, 0);

        try {
            fontFlag = a.getInteger(R.styleable.TTextView_fontType, 0);
            typeFlag = a.getInteger(R.styleable.TTextView_itemType, 0);
        } finally {
            a.recycle();
        }

        Typeface tf = null;

        if (fontFlag == 1)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Black.ttf");
        else if (fontFlag == 2)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-BlackItalic.ttf");
        else if (fontFlag == 3)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Bold.ttf");
        else if (fontFlag == 4)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-BoldItalic.ttf");
        else if (fontFlag == 5)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-ExtraBold.ttf");
        else if (fontFlag == 6)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-ExtraBoldItalic.ttf");
        else if (fontFlag == 7)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-ExtraLight.ttf");
        else if (fontFlag == 8)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-ExtraLightItalic.ttf");
        else if (fontFlag == 9)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Italic.ttf");
        else if (fontFlag == 10)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Light.ttf");
        else if (fontFlag == 11)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-LightItalic.ttf");
        else if (fontFlag == 12)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Medium.ttf");
        else if (fontFlag == 13)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-MediumItalic.ttf");
        else if (fontFlag == 14)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Regular.ttf");
        else if (fontFlag == 15)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-SemiBold.ttf");
        else if (fontFlag == 16)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-SemiBoldItalic.ttf");
        else if (fontFlag == 17)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-Thin.ttf");
        else if (fontFlag == 18)
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Exo-ThinItalic.ttf");

        setTypeface(tf);


        if (typeFlag == 1) {
            setTextAppearance(getContext(), R.style.Title);
        } else if (typeFlag == 2) {
            setTextAppearance(getContext(), R.style.TitleShort);
        } else if (typeFlag == 3) {
            setTextAppearance(getContext(), R.style.Spot);
        } else if (typeFlag == 4) {
            setTextAppearance(getContext(), R.style.SpotShort);
        } else if (typeFlag == 5) {
            setTextAppearance(getContext(), R.style.Description);
        }



    }
}