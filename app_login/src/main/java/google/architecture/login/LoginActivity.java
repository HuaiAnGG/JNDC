package google.architecture.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.coremodel.viewmodel.GirlsViewModel;
import google.architecture.coremodel.viewmodel.LoginViewModel;
import google.architecture.login.databinding.ActivityLoginBinding;

@Route(path = ARouterPath.LoginAty)
public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(LoginActivity.this);
        binding = DataBindingUtil.setContentView(
                LoginActivity.this, R.layout.activity_login);
//        binding.setItemClick(itemClick);
        binding.loginBtn.setOnClickListener(loginBtnOnclickListener);
    }

    View.OnClickListener loginBtnOnclickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("danxx", "onClick toMainWindows");
            ARouter.getInstance()
                    .build(ARouterPath.MainAty)
                    /**可以针对性跳转跳转动画*/
                    .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                    .navigation(LoginActivity.this);
        }
    };

    /**
     * 事件点击监听接口
     */
    public interface ItemClick {
        void onClick(int id);
    }

    /**
     * 订阅数据变化来刷新UI
     *
     * @param model
     */
    private void subscribeToModel(final LoginViewModel model) {
        //观察数据变化来刷新UI
    }

}