package google.architecture.login;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class LoginUserData extends BaseObservable {

    private String username;
    private String password;

    @Bindable
    public String getUserName() {
        return username;
    }

    /**
     * set方法中添加notifyPropertyChanged();这一句代码，这里是提醒更新数据
     * @param username
     */
    public void setUserName(String username) {
        this.username = username;

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}
