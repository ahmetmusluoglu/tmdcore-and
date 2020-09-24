
package turkuvaz.sdk.models.Models.ConfigBase;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("leftMenu")
    @Expose
    private ArrayList<LeftMenu> left = null;
    @SerializedName("bottomMenu")
    @Expose
    private ArrayList<BottomMenu> footer = null;
    @SerializedName("toolbarMenu")
    @Expose
    private ArrayList<ToolbarMenu> toolbarMenu = null;
    @SerializedName("settingsMenu")
    @Expose
    private ArrayList<SettingsMenu> settingsMenu = null;

    public ArrayList<LeftMenu> getLeft() {
        return left;
    }

    public void setLeft(ArrayList<LeftMenu> left) {
        this.left = left;
    }

    public ArrayList<BottomMenu> getFooter() {
        return footer;
    }

    public void setFooter(ArrayList<BottomMenu> footer) {
        this.footer = footer;
    }

    public ArrayList<ToolbarMenu> getToolBarMenu() {
        return toolbarMenu;
    }

    public void setHeader(ArrayList<ToolbarMenu> header) {
        this.toolbarMenu = header;
    }

    public ArrayList<SettingsMenu> getSettingsMenu() {
        return settingsMenu;
    }

    public void setSettingsMenu(ArrayList<SettingsMenu> settingsMenu) {
        this.settingsMenu = settingsMenu;
    }

}
