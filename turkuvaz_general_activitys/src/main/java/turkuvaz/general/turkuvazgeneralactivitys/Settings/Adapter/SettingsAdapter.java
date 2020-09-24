package turkuvaz.general.turkuvazgeneralactivitys.Settings.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;

import turkuvaz.general.turkuvazgeneralactivitys.R;
import turkuvaz.sdk.models.Models.ConfigBase.SettingsMenu;
import turkuvaz.sdk.models.Models.Enums.ApiListEnums;
import turkuvaz.sdk.models.Models.Enums.SettingsTypes;
import turkuvaz.sdk.models.Models.Preferences;

public class SettingsAdapter extends RecyclerView.Adapter {
    private final byte TYPE_NOTIFICATION = 0;
    private final byte TYPE_SINGLE = 1;
    private final byte TYPE_OTHER = 2;
    private final byte TYPE_EMPTY = 3;
    private ArrayList<SettingsMenu> arrayList;
    private Context context;
    private LayoutInflater inflater;

    private String apiLink = "";
    private int count = 0;
    private long startMillis=0;

    public SettingsAdapter(ArrayList<SettingsMenu> dummyData, Context context) {
        if (context == null)
            return;
        this.arrayList = dummyData;
        boolean isGdprActive = Preferences.getBoolean(context, ApiListEnums.GDPR_IS_ACTIVE.getType(), false);
        boolean isKvkkActive = Preferences.getBoolean(context, ApiListEnums.KVKK_IS_ACTIVE.getType(), false);
        String  type = Preferences.getString(context, ApiListEnums.PRIVACY_POLICY_TYPE.getType(), "OTHER");

        Log.i("TAGCC", "SettingsAdapter: "+type);
        /*if (arrayList != null && arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).getType().equals("changePrivacyPolicy")) {
                    if (arrayList.get(i).getTitle().contains("OTHER")) {
                        arrayList.remove(i);
                    }else if (arrayList.get(i).getTitle().contains("GDPR")) {
                        if (!type.equals("GDPR") || !isGdprActive)
                            arrayList.remove(i);
                    } else if (arrayList.get(i).getTitle().contains("KVKK")) {
                        if (!type.equals("KVKK") || !isKvkkActive)
                            arrayList.remove(i);
                    }

                    *//*if (GdprPreferences.getModeIsKvkk(context)) {
                        if (arrayList.get(i).getTitle().contains("GDPR"))
                            arrayList.remove(i);
                    } else {
                        if (arrayList.get(i).getTitle().contains("KVKK"))
                            arrayList.remove(i);
                    }*//*
                }
            }
        }*/

        if (arrayList != null && arrayList.size() > 0) {
            for (Iterator<SettingsMenu> iterator = arrayList.iterator(); iterator.hasNext(); ) {
                SettingsMenu settingsMenu = iterator.next();
                if (settingsMenu.getType().equals("changePrivacyPolicy") || settingsMenu.getType().equals("changePrivacyPolicy2")) {
                    if (settingsMenu.getTitle().contains("OTHER")) {
                        iterator.remove();
                    }else if (settingsMenu.getTitle().contains("GDPR")) {
                        if (!type.equals("GDPR") || !isGdprActive)
                            iterator.remove();
                    } else if (settingsMenu.getTitle().contains("KVKK")) {
                        if (!type.equals("KVKK") || !isKvkkActive){
                            iterator.remove();
                        }
                    }
                } else if (settingsMenu.getType().equals("introPages") && !settingsMenu.getIsActive()) {
                    iterator.remove();
                }
            }
        }

        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vRoot = inflater.inflate(R.layout.settings_list_item, parent, false);
        switch (viewType) {
            case TYPE_NOTIFICATION:
                vRoot = inflater.inflate(R.layout.settings_list_item_single, parent, false);
                break;
            case TYPE_SINGLE:
                vRoot = inflater.inflate(R.layout.settings_list_item_single, parent, false);
                break;
            case TYPE_OTHER:
                vRoot = inflater.inflate(R.layout.settings_list_item, parent, false);
                break;
            case TYPE_EMPTY:
                vRoot = inflater.inflate(R.layout.settings_list_item_empty, parent, false);
                break;
        }
        return new ViewHolder(vRoot);
    }

    @Override
    public int getItemViewType(int position) {
        // TODO CHECK ITEM VIEW TYPE HERE
        if (!arrayList.get(position).getIsActive())
            return TYPE_EMPTY;
        else if (arrayList.get(position).getType().equals(SettingsTypes.CURRENT_VERSION.getType()))
            return TYPE_SINGLE;
        else
            return TYPE_OTHER;

    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SettingsMenu currentModel = arrayList.get(position);
        final ViewHolder viewHolder = (ViewHolder) holder;

        final byte CURRENT_ITEM_TYPE = (byte) getItemViewType(position);

        if (CURRENT_ITEM_TYPE == TYPE_OTHER) {
            if (currentModel.getIsActive())
                viewHolder.mTv_title.setText(currentModel.getTitle());
            if (currentModel.getType().equals(SettingsTypes.CURRENT_VERSION.getType())) {
                try {
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    String version = pInfo.versionName;
                    viewHolder.mTv_title.setText(currentModel.getTitle() + version);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else if (CURRENT_ITEM_TYPE == TYPE_SINGLE) {
            if (currentModel.getIsActive()) {
                try {
                    PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    String version = pInfo.versionName;
                    int buildNumber = pInfo.versionCode;
                    viewHolder.mTv_title.setText(currentModel.getTitle() + version + " (" + String.valueOf(buildNumber) + ")");

                    apiLink = Preferences.getString(context, "connectedurl", "https://api.tmgrup.com.tr");

                    viewHolder.mTv_title.setText(viewHolder.mTv_title.getText().toString() + "  " + (apiLink.equals("https://api.tmgrup.com.tr") ? "" : apiLink)); // eğer canlı link varsa yani db canlıyı görüyorsa bir şey yazmasın. değilse ip yazsın.
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        if (currentModel.getType().equals(SettingsTypes.CURRENT_VERSION.getType())){
            viewHolder.mTv_title.setOnTouchListener((v, event) -> {
                int eventaction = event.getAction();
                if (eventaction == MotionEvent.ACTION_DOWN) {

                    long time = System.currentTimeMillis(); //get system current milliseconds

                    if (startMillis == 0 || (time - startMillis > 4000)) { //if it is the first time, or if it has been more than 3 seconds since the first tap ( so it is like a new try), we reset everything
                        startMillis = time;
                        count = 1;
                    } else    //it is not the first, and it has been  less than 3 seconds since the first
                        count++; //  time-startMillis< 4000
                    if (count == 10)
                        showDialogUserPass((Activity) context);
                    return true;
                }
                return false;
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // TODO Create object HERE
        TextView mTv_title;
        Switch mSw_toogleNotification;

        ViewHolder(View itemView) {
            super(itemView);
            mTv_title = itemView.findViewById(R.id.tv_menuListTitle);
            mSw_toogleNotification = itemView.findViewById(R.id.sw_settingsNotificationToggle);
        }
    }

    public void showDialogSelectDb(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_db_select);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        RadioButton rbCanli = dialog.findViewById(R.id.rbCanli);
        RadioButton rbQA = dialog.findViewById(R.id.rbQA);
        RadioButton rbTest = dialog.findViewById(R.id.rbTest);

        Button btnDbSec = dialog.findViewById(R.id.btnDbSec);
        btnDbSec.setOnClickListener(v -> {
            if (rbCanli.isChecked()) {
                Preferences.saveApiLink(activity, "connectedurl", "https://api.tmgrup.com.tr"); // pref.save çağrılmayacak. çünkü orada replace işlemi yapılıyor.
            } else if (rbQA.isChecked()) {
                Preferences.saveApiLink(activity, "connectedurl", "http://api-qa.dev.tmd");
            } else if (rbTest.isChecked())
                Preferences.saveApiLink(activity, "connectedurl", "http://api-test.dev.tmd");
            dialog.dismiss();
        });
        if(!((Activity) context).isFinishing())
            dialog.show();
    }

    public void showDialogToken(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_get_token);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        Button btnTokenKopyala = dialog.findViewById(R.id.btnTokenKopyala);
        btnTokenKopyala.setOnClickListener(v -> {
            Toast.makeText(context, "Token Kopyalandı", Toast.LENGTH_SHORT).show();
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copiedToken", Preferences.getString(activity, "token", ""));
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
        });

        Button btnHMSTokenKopyala = dialog.findViewById(R.id.btnHMSTokenKopyala);
        btnHMSTokenKopyala.setOnClickListener(v -> {
            Toast.makeText(context, "HMS Token Kopyalandı", Toast.LENGTH_SHORT).show();
            ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("copiedToken", Preferences.getString(activity, "hms_token", ""));
            clipboard.setPrimaryClip(clip);
            dialog.dismiss();
        });

        if (!((Activity) context).isFinishing())
            dialog.show();
    }

    public void showDialogUserPass(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.popup_db_select_security);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        EditText edtUser = dialog.findViewById(R.id.edtUser);
        EditText edtPass = dialog.findViewById(R.id.edtPass);

        Button btnGirisYap = dialog.findViewById(R.id.btnGirisYap);
        Button btnKapat = dialog.findViewById(R.id.btnKapat);

        btnKapat.setOnClickListener(v -> dialog.dismiss());

        btnGirisYap.setOnClickListener(v -> {
            if (edtUser.getText().toString().equals("test") && edtPass.getText().toString().equals("1453")) {
                dialog.dismiss();
                showDialogSelectDb(activity);
            }else if (edtUser.getText().toString().equals("push") && edtPass.getText().toString().equals("1453")) {
                dialog.dismiss();
                showDialogToken(activity);
            } else
                Toast.makeText(activity, "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_SHORT).show();
        });

        if (!((Activity) context).isFinishing())
            dialog.show();
    }
}