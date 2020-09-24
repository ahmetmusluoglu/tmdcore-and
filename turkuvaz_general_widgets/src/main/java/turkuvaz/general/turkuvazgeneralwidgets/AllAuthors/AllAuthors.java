package turkuvaz.general.turkuvazgeneralwidgets.AllAuthors;

import android.content.Context;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatButton;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import turkuvaz.general.turkuvazgeneralwidgets.R;
import turkuvaz.sdk.models.Models.AllAuthors.AllAuthorsData;
import turkuvaz.sdk.models.Models.AllAuthors.Response;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.ApiClient;
import turkuvaz.sdk.turquazapiclient.RetrofitClient.RestInterface;

public class AllAuthors extends RelativeLayout {
    private RestInterface mRestInterface;
    private AppCompatButton _hintButton;
    private spinnerDefaultSelection _spinner;
    private ImageView _downArrow;
    private ArrayAdapter<String> _spinnerAdapterString;
    private ArrayAdapter<CharSequence> _spinnerAdapterCharSequence;
    private boolean _allowToSelect;
    private onSpinnerItemClickListener<String> _callback;
    private static final String TAG = AllAuthors.class.getSimpleName();
    private boolean _isItemResourceDeclared = false;
    private int _spinnerType = 0;
    private boolean _isSelected;
    private String hintButtonLabel;
    private int HINT_BUTTON_NOT_SELECTED_COLOR = Color.parseColor("#aaaaaa");
    private final int HINT_BUTTON_DISABLED_COLOR = Color.parseColor("#BDBDBD");
    private int HINT_BUTTON_COLOR = Color.BLACK;
    private final int DOWN_ARROW_DEFAULT_TINT_COLOR = Color.parseColor("#797979");
    private int DOWN_ARROW_TINT_COLOR = Color.parseColor("#797979");
    private String apiUrl;

    private ArrayList<Response> allAuthorsList = new ArrayList<>();

    public AllAuthors(Context context) {
        super(context);
        Response response = new Response();
        response.setSource("Köşe Yazarı Seçin");
        response.setId("None");
        allAuthorsList.add(response);
    }

    public AllAuthors(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllAuthors(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init() {
        inflate(getContext(), R.layout.all_authors_view, this);
        this._hintButton = findViewById(R.id.allAuthors_hintButton);
        this._spinner = findViewById(R.id.allAuthors_spinner);
        _spinner.setVisibility(INVISIBLE); // yazarlar ekranındaki spinner'ın altındaki küçük ok görünüyordu. üst üste 2 spinner eklendiği için bir tanesi gizlendi, işlevini yerine getiriyor.
        this._downArrow = findViewById(R.id.allAuthors_downArrow);
        this.mRestInterface = ApiClient.getClientApi(getContext()).create(RestInterface.class);
        getAllAuthors();
    }


    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    private void getAllAuthors() {
        mRestInterface.getAllAuthors(apiUrl).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllAuthorsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllAuthorsData allAuthorsData) {
                        try {
                            if (allAuthorsData != null && allAuthorsData.getData() != null && allAuthorsData.getData().getAuthors().getResponse() != null) {
                                allAuthorsList.addAll(allAuthorsData.getData().getAuthors().getResponse());
                            }
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        setData(allAuthorsList);
                    }
                });
    }

    private void setSpinnerStyle(TypedArray typedArray) {
        hintButtonLabel = typedArray.getString(R.styleable.AllAuthorsStyle_spinnerHint);
        setHintButtonText(typedArray.getString(R.styleable.AllAuthorsStyle_spinnerHint));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 40, 10, 10);
        switch (typedArray.getInt(R.styleable.AllAuthorsStyle_spinnerDirection, 0)) {
            case 0:
                _hintButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                _downArrow.setLayoutParams(params);
                break;
            case 1:
                _hintButton.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                _downArrow.setLayoutParams(params);
                break;
        }

    }

    public ArrayList<Response> getData() {
        return allAuthorsList;
    }

    private void setData(ArrayList<Response> data) {
        List<String> categories = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getSource() != null)
                categories.add(data.get(i).getSource());
        }

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        setAdapter(categoriesAdapter);
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this._spinnerAdapterString = adapter;
        _spinner.setAdapter(_spinnerAdapterString);
        initiateSpinnerString();
    }

    public void setAdapter(ArrayAdapter<CharSequence> adapter, int idle) {
        _spinnerType = 1;
        this._spinnerAdapterCharSequence = adapter;
        _spinner.setAdapter(_spinnerAdapterCharSequence);
        initiateSpinnerCharSequence();
    }

    public boolean isSelected() {
        return _isSelected;
    }

    private void initiateSpinnerString() {

        if (!_isItemResourceDeclared) {
            _spinnerAdapterString.setDropDownViewResource(R.layout.all_authors_list_item);
        }

        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (AllAuthors.this._callback == null) {
                        throw new IllegalStateException("callback cannot be null");
                    }
                    if (_allowToSelect) {
                        _isSelected = true;
                        Object item = AllAuthors.this._spinner.getItemAtPosition(position);
                        AllAuthors.this._callback.onItemSelected(position, (String) item, allAuthorsList.get(position).getId());
                        setHintButtonText(_spinner.getSelectedItem().toString());
                    }
                    _allowToSelect = true;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing selected");
            }
        });
    }

    private void initiateSpinnerCharSequence() {

        if (!_isItemResourceDeclared) {
            _spinnerAdapterCharSequence.setDropDownViewResource(R.layout.all_authors_list_item);
        }

        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    if (AllAuthors.this._callback == null) {
                        throw new IllegalStateException("callback cannot be null");
                    }
                    if (_allowToSelect) {
                        _isSelected = true;
                        Object item = AllAuthors.this._spinner.getItemAtPosition(position);
                        AllAuthors.this._callback.onItemSelected(position, (String) item, allAuthorsList.get(position).getId());
                        setHintButtonText(_spinner.getSelectedItem().toString());
                    }
                    _allowToSelect = true;
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "Nothing selected");
            }
        });


    }

    public void setDropDownViewResource(int resource) {

        if (_spinnerType == 1) {
            _spinnerAdapterCharSequence.setDropDownViewResource(resource);
        } else {
            _spinnerAdapterString.setDropDownViewResource(resource);
        }

        _isItemResourceDeclared = true;

    }

    public void setOnSpinnerItemClickListener(onSpinnerItemClickListener<String> callback) {

        this._callback = callback;

        _hintButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _spinner.performClick();
            }
        });
    }

    public void setSelection(int position) {
        _allowToSelect = true;
        _spinner.setSelection(position);
    }

    public void setSelection(String value) {
        if (value.trim().isEmpty()) {
            _allowToSelect = true;
            if (_spinnerType == 0) {
                int spinnerPosition = _spinnerAdapterString.getPosition(value);
                _spinner.setSelection(spinnerPosition);
            } else {
                int spinnerPosition = _spinnerAdapterCharSequence.getPosition(value);
                _spinner.setSelection(spinnerPosition);
            }
        }
    }

    public String getSelectedItem() {
        if (isSelected()) {
            return _spinner.getSelectedItem().toString();
        } else {
            return null;
        }
    }

    public spinnerDefaultSelection getSpinner() {
        return _spinner;
    }

    public int getSelectedItemPosition() {
        if (isSelected()) {
            return _spinner.getSelectedItemPosition();
        } else {
            return -1;
        }
    }

    public interface onSpinnerItemClickListener<T> {
        /**
         * When a spinner item has been selected.
         *
         * @param position       Position selected
         * @param itemAtPosition Item selected
         */
        void onItemSelected(int position, T itemAtPosition, String authorsID);
    }

    public void setSpinnerEnable(boolean enable) {
        this._spinner.setEnabled(enable);
        this._hintButton.setEnabled(enable);
        setDownArrowStyle();
        setHitButtonStyle();
    }

    public boolean isSpinnerEnable() {
        return this._spinner.isEnabled();
    }

    private void setHitButtonStyle() {
        this._hintButton.setTextColor(
                this._hintButton.isEnabled() ?
                        (isSelected() ? HINT_BUTTON_COLOR : HINT_BUTTON_NOT_SELECTED_COLOR)
                        :
                        HINT_BUTTON_DISABLED_COLOR
        );
    }

    public void setHintTextColor(int color) {
        this.HINT_BUTTON_NOT_SELECTED_COLOR = color;
        this._hintButton.setTextColor(isSelected() ? this.HINT_BUTTON_COLOR : this.HINT_BUTTON_NOT_SELECTED_COLOR);
    }

    public void setSelectedItemHintColor(int color) {
        this.HINT_BUTTON_COLOR = color;
        this._hintButton.setTextColor(isSelected() ? this.HINT_BUTTON_COLOR : this.HINT_BUTTON_NOT_SELECTED_COLOR);
    }

    private void setHintButtonText(String label) {
        _hintButton.setText(label);
        setHitButtonStyle();
    }

    private void setDownArrowStyle() {
        this._downArrow.setColorFilter(
                this._hintButton.isEnabled() ? this.DOWN_ARROW_TINT_COLOR : this.DOWN_ARROW_DEFAULT_TINT_COLOR,
                PorterDuff.Mode.SRC_ATOP);
        this._downArrow.setAlpha(this._hintButton.isEnabled() ? 1.0f : 0.6f);
    }

    public void setDownArrowTintColor(int color) {
        this.DOWN_ARROW_TINT_COLOR = color;
        setDownArrowStyle();
    }

    public void clearSelection() {
        _isSelected = false;
        this._hintButton.setTextColor(
                this._hintButton.isEnabled() ?
                        HINT_BUTTON_NOT_SELECTED_COLOR
                        :
                        HINT_BUTTON_DISABLED_COLOR
        );
        _hintButton.setText(hintButtonLabel);
    }

    public void setSpinnerHint(String label) {
        hintButtonLabel = label;
        if (!isSelected()) {
            _hintButton.setText(hintButtonLabel);
        }
    }
}