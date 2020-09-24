package turkuvaz.sdk.global.ErrorDialog;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import turkuvaz.sdk.global.FirebasePush.PushModel;
import turkuvaz.sdk.global.GlobalMethods.GlobalMethods;
import turkuvaz.sdk.global.R;

/**
 * Created by Ahmet MUŞLUOĞLU on 3/17/2020.
 */


public class ErrorSheetDialog extends BottomSheetDialog {

    private static Locale localeTR = new Locale("tr");
    private ImageButton mBtnClose;
    private Button mBtnReportError;
    private EditText edtReport;
    private Context mContext;
    private Throwable mError;
    private String requestUrl = "None";
    private String mFullTimeChild, mSingleDayChild, mSingleHoursChild, mFullDateChild, screen = "None";
    private DatabaseReference getGroupsDay, getNewGroupsDay;
    private List<String> mIgnoreList;

    private boolean IgnorePushError = true;
    private boolean IgnoreRelatedNews = true;
    private PushModel pushModel;

    public ErrorSheetDialog(@NonNull Context context, Throwable error, String screen, PushModel pushModel) {
        super(context);
        try {
            this.mContext = context;
            this.mError = error;
            this.pushModel = pushModel;

            if (screen != null && !TextUtils.isEmpty(screen))
                this.screen = screen;

            /*if (requestUrl != null && !requestUrl.isEmpty())
                this.requestUrl = requestUrl;*/

            if (error instanceof HttpException)
                this.requestUrl = ((HttpException) error).response().raw().request().url().url().toString();

            setCancelable(false);
            setCanceledOnTouchOutside(false);
            create();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void create() {
        View view = getLayoutInflater().inflate(R.layout.error_sheet_dialog, null);
        setContentView(view);

        mBtnClose = view.findViewById(R.id.mBtnClose);
        mBtnReportError = view.findViewById(R.id.mBtnReportError);
        edtReport = view.findViewById(R.id.edtReport);
        mBtnClose.setOnClickListener(view1 -> exitDialog(false));
        mBtnReportError.setOnClickListener(view12 -> {
            if (edtReport != null) {
                if (!TextUtils.isEmpty(edtReport.getText().toString().trim())) {
                    sendErrorFirebaseWithDesc(edtReport.getText().toString());
                } else {
                    edtReport.setError(mContext.getResources().getString(R.string.kisaca_acikla));
                }
            }
        });

        checkContainsConfig();

        Date date = new Date();
        DatabaseReference root = FirebaseDatabase.getInstance().getReference();

        mFullTimeChild = getDateFormat("HH:mm:ss SSS", date);
        mSingleDayChild = getDateFormat("dd", date);
        mSingleHoursChild = getDateFormat("HH", date);
        mFullDateChild = getDateFormat("dd-MM-yyyy", date);

        getGroupsDay = root.child("ErrorCount").child(mSingleDayChild);
        getNewGroupsDay = root.child("ErrorLogs").child(mFullDateChild).child(mSingleHoursChild + ":00");
    }

    private String getDateFormat(String pattern, Date date) {
        return new SimpleDateFormat(pattern, localeTR).format(date);
    }

    private void sendErrorFirebase() {
        try {
            if (mError == null)
                return;

            //Push'dan gelen hatalar firebase üzerinden kapatılmışsa geç
            if (pushModel != null && IgnorePushError) {
                return;
            }

            //Haberler'dan gelen hatalar firebase üzerinden kapatılmışsa geç
            if (pushModel == null && IgnoreRelatedNews) {
                return;
            }

            if (mError.getMessage() != null && mIgnoreList != null) {
                for (String ignoreWord : mIgnoreList) {
                    if (mError.getMessage().contains(ignoreWord))
                        return;
                }
            }

            if (checkUUID(requestUrl) != null) {
                getGroupsDay.child(Objects.requireNonNull(checkUUID(requestUrl))).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Long currentCount = mutableData.getValue(Long.class);
                        if (currentCount == null)
                            mutableData.setValue(0);
                        else
                            mutableData.setValue(currentCount + 1);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    }
                });
            }

            if (mError != null && mError.getMessage() != null) {
                if (pushModel != null) {
                    if (pushModel.getU() != null && pushModel.getRefid() != null && pushModel.getTitle() != null && mError != null && mError.getMessage() != null) {
                        ErrorLogModel member = new ErrorLogModel(GlobalMethods.getToken(), getAppVersion(), pushModel.getU(), pushModel.getRefid(), pushModel.getTitle(), mError.getMessage(), requestUrl, Build.VERSION.SDK_INT, getDeviceName(), screen);
                        getNewGroupsDay.child(mFullTimeChild + " - " + cutError(mError.getMessage())).setValue(member);
                        exitDialog(true);
                    }
                } else {
                    ErrorLogModel member = new ErrorLogModel(GlobalMethods.getToken(), getAppVersion(), mError.getMessage(), requestUrl, Build.VERSION.SDK_INT, getDeviceName(), screen);
                    getNewGroupsDay.child(mFullTimeChild + " - " + cutError(mError.getMessage())).setValue(member);
                    exitDialog(true);
                }
            }
        } catch (Exception e) {
            if (getWindow() != null) {
                exitDialog(false);
            } else {
                ((Activity) mContext).finish();
            }
        }
    }

    private void sendErrorFirebaseWithDesc(String description) {
        try {
            if (mError == null)
                return;

            if (mIgnoreList != null && mError.getMessage() != null) {
                if (mIgnoreList.contains(mError.getMessage()))
                    return;
            }

            if (checkUUID(requestUrl) != null) {
                getGroupsDay.child(Objects.requireNonNull(checkUUID(requestUrl))).runTransaction(new Transaction.Handler() {
                    @Override
                    public Transaction.Result doTransaction(MutableData mutableData) {
                        Long currentCount = mutableData.getValue(Long.class);
                        if (currentCount == null)
                            mutableData.setValue(0);
                        else
                            mutableData.setValue(currentCount + 1);

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                    }
                });
            }

            if (pushModel != null) {
                if (pushModel.getU() != null && pushModel.getRefid() != null && pushModel.getTitle() != null && mError != null && mError.getMessage() != null) {
                    ErrorLogModel member = new ErrorLogModel(GlobalMethods.getToken(), getAppVersion(), pushModel.getU(), pushModel.getRefid(), pushModel.getTitle(), mError.getMessage(), requestUrl, Build.VERSION.SDK_INT, getDeviceName(), description, screen);
                    getNewGroupsDay.child(mFullTimeChild + " - " + cutError(mError.getMessage())).setValue(member);
                    exitDialog(true);
                }
            } else {
                if (mError != null && mError.getMessage() != null) {
                    ErrorLogModel member = new ErrorLogModel(GlobalMethods.getToken(), getAppVersion(), mError.getMessage(), requestUrl, Build.VERSION.SDK_INT, getDeviceName(), description, screen);
                    getNewGroupsDay.child(mFullTimeChild + " - " + cutError(mError.getMessage())).setValue(member);
                    exitDialog(true);
                }
            }
        } catch (Exception e) {
            if (getWindow() != null) {
                exitDialog(true);
            }
            ((Activity) mContext).finish();
        }
    }

    private static String getDeviceName() {
        try {
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            if (model.startsWith(manufacturer)) {
                return capitalize(model);
            }
            return capitalize(manufacturer) + " " + model;
        } catch (Exception e) {
            return "None";
        }
    }

    private static String capitalize(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return str;
            }
            char[] arr = str.toCharArray();
            boolean capitalizeNext = true;

            StringBuilder phrase = new StringBuilder();
            for (char c : arr) {
                if (capitalizeNext && Character.isLetter(c)) {
                    phrase.append(Character.toUpperCase(c));
                    capitalizeNext = false;
                    continue;
                } else if (Character.isWhitespace(c)) {
                    capitalizeNext = true;
                }
                phrase.append(c);
            }

            return phrase.toString();
        } catch (Exception e) {
            return "None";
        }
    }

    private String getAppVersion() {
        if (mContext == null)
            return "None";
        String appVersion;
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            appVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            appVersion = "null";
        }
        return "v" + appVersion;
    }

    private String checkUUID(String source) {
        Pattern p = Pattern.compile("([a-f0-9]{8}(-[a-f0-9]{4}){3}-[a-f0-9]{11})");
        Matcher m = p.matcher(source);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    private void checkContainsConfig() {
        FirebaseDatabase.getInstance().getReference().child("ErrorEnums").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("IgnoreErrorBodyContains").getValue() != null && String.valueOf(dataSnapshot.child("IgnoreErrorBodyContains").getValue()).contains(",")) {
                        String ignoreText = String.valueOf(dataSnapshot.child("IgnoreErrorBodyContains").getValue());
                        mIgnoreList = new ArrayList<>(Arrays.asList(ignoreText.split(",")));


                        if (dataSnapshot.child("IgnorePushError") != null && dataSnapshot.child("IgnoreRelatedNews") != null) {
                            if (dataSnapshot.child("IgnorePushError").getValue() instanceof Boolean) {
                                IgnorePushError = (boolean) dataSnapshot.child("IgnorePushError").getValue();
                            }

                            if (dataSnapshot.child("IgnoreRelatedNews").getValue() instanceof Boolean) {
                                IgnoreRelatedNews = (boolean) dataSnapshot.child("IgnoreRelatedNews").getValue();
                            }
                            sendErrorFirebase();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private String cutError(String str) {
        if (str.length() > 99) {
            return str.substring(0, 99);
        } else {
            return str;
        }
    }

    private void exitDialog(boolean isSuccess) {
        if (isSuccess)
            Toast.makeText(mContext, "Mesajınız gönderildi.", Toast.LENGTH_SHORT).show();
        dismiss();
        ((Activity) mContext).finish();
    }
}
