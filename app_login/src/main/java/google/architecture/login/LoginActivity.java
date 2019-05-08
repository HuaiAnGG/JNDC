package google.architecture.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.just.core.common.AuthModel;
import com.just.core.common.CoreConfig;
import com.just.core.http.HttpEngine;
import com.just.core.http.HttpTask;
import com.just.core.listener.MessageCallbackListener;
import com.just.core.message.RequestBody;
import com.just.core.message.RequestHeader;
import com.just.core.message.ResponseMessage;
import com.just.core.util.MD5;

import org.json.ext.JSONObject;

import java.util.HashMap;
import java.util.Map;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.coremodel.viewmodel.GirlsViewModel;
import google.architecture.coremodel.viewmodel.LoginViewModel;
import google.architecture.login.databinding.ActivityLoginBinding;
import google.architecture.todo.utils.Constants;

@Route(path = ARouterPath.LoginAty)
public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private HttpTask httpTask;

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

//            ARouter.getInstance()
//                    .build(ARouterPath.MainAty)
//                    /**可以针对性跳转跳转动画*/
//                    .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
//                    .navigation(LoginActivity.this);

            /**
             * 启动校验
             */
            doTask();
        }
    };

    private void initHttpTask() {

        httpTask = new HttpTask("A023", "loginApp",
                new MessageCallbackListener() {
                    @Override
                    public void onWaiting(HttpTask httpTask) {

                    }

                    @Override
                    public void onMessageResponse(HttpTask httpTask, ResponseMessage responseMessage) {
                        Log.e("onMessageResponse: ", responseMessage.getJsonText());

                    }

                    @Override
                    public void onFinished(HttpTask httpTask) {

                    }

                    @Override
                    public void onUploading(HttpTask httpTask, long l, long l1) {

                    }
                });
    }
    
    private void doTask() {
        if (httpTask == null) {
            initHttpTask();
        }


		CoreConfig.domainUrl = "http://oa.leadisoft.com:18081/lead_portal/api/login/checkLogin.lead";

        Log.e("--App", "-> " + CoreConfig.domainUrl);
//        Log.e("--App", "-> " + CoreConfig.domainUploadUrl);

        Map<String, String> hashMap = new HashMap();
        hashMap.put("loginApp", AuthModel.AUTH_DEFAULT);
        CoreConfig.commandCodeAuth = hashMap;

        httpTask.getRequestMessage().header.setDataType(2);
        httpTask.getRequestMessage().body.setItemParaListWithKeys(
                "userName", "admin",
                "password", MD5.md5("1")
        );
        httpTask.setUrl(CoreConfig.domainUrl);

        Log.d("--HttpUtil", "[HTTP RQ]" + httpTask.getRequestMessage().toJSON());

        HttpEngine.getInstance().emit(httpTask);
    }


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