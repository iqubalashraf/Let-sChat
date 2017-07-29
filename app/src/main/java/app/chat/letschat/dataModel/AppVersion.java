package app.chat.letschat.dataModel;

/**
 * Created by ashrafiqubal on 28/07/17.
 */

public class AppVersion {
    int versionCode;

    public AppVersion(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
