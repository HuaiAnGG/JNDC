package google.architecture.login.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.just.core.common.AuthModel;
import com.just.core.common.CoreConfig;
import com.just.core.http.HttpEngine;
import com.just.core.http.HttpHeader;
import com.just.core.http.HttpTask;
import com.just.core.listener.MessageCallbackListener;
import com.just.core.message.ResponseMessage;
import com.just.core.util.MD5;

import org.json.ext.JSONObject;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.common.util.StorageUtils;
import google.architecture.coremodel.viewmodel.LoginViewModel;
import google.architecture.login.R;
import google.architecture.login.bean.HeaderList;
import google.architecture.login.databinding.ActivityLoginBinding;

@Route(path = ARouterPath.LoginAty)
public class LoginActivity extends BaseActivity {

    final String TAG = LoginActivity.class.getSimpleName();

    private ActivityLoginBinding binding;
    private HttpTask httpTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(LoginActivity.this);
        binding = DataBindingUtil.setContentView(
                LoginActivity.this, R.layout.activity_login);
        binding.loginBtn.setOnClickListener(loginBtnOnclickListener);
    }

    View.OnClickListener loginBtnOnclickListener
            = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("danxx", "onClick toMainWindows");

            if (!TextUtils.isEmpty(binding.username.getText().toString().trim())
                    && !TextUtils.isEmpty(binding.password.getText().toString().trim())) {
                // 登陆
                doTask();
                return;
            }

            Toast.makeText(getApplicationContext(),
                    "账号、密码不能为空!!!", Toast.LENGTH_SHORT).show();
        }
    };

    private HeaderList json2HeaderBean(String json) {
        Gson gson = new Gson();
        HeaderList headerList = gson.fromJson(json, HeaderList.class);
        return headerList;
    }

    private void initHttpTask() {

        httpTask = new HttpTask("login", "checkLogin.lead", HttpTask.Type.COMMON,
                new MessageCallbackListener() {
                    @Override
                    public void onWaiting(HttpTask httpTask) {
                        //TODO  show the loading dialog
                    }

                    @Override
                    public void onMessageResponse(HttpTask httpTask, ResponseMessage responseMessage) {

                        Log.d(TAG, "onMessageResponse: " + responseMessage.getJsonText());

                        if (responseMessage == null) {
                            Log.i("******************", "ResponseMessage is null!");
                            return;
                        }

                        String status = responseMessage.header.getStatus();

                        if ("309".equals(status)) {
                            Toast.makeText(getApplicationContext(),
                                    "Empty Msg", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if ("200".equals(status)) {
                            JSONObject body = responseMessage.body.getBody();
                            String str = body.optString("list");

                            if (TextUtils.isEmpty(str)) {
                                str = body.toString();
                                Log.e("======!!!!!====", str);
                            }
                            // 获取响应头
                            HeaderList headerList = json2HeaderBean(str);
                            //存储到disk的数据，这里会先存到cache，再存储到disk
                            StorageUtils.saveInDisk("SP_TOKEN", "token", headerList.getToken());

                            Toast.makeText(getApplicationContext(),
                                    "登录成功!!!", Toast.LENGTH_SHORT).show();

                            ARouter.getInstance()
                                    .build(ARouterPath.MainAty)
                                    // 可以针对性跳转跳转动画
                                    .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                                    .navigation(LoginActivity.this);
                            return;
                        }

                        String info = responseMessage.header.getInfo();
                        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
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

        CoreConfig.domainUrl = "http://oa.leadisoft.com:18081/lead_portal";

        Log.e("--Application", "-> " + CoreConfig.domainUrl);

        // 设置api类型之一
        httpTask.setServiceType("api");

        HttpHeader httpHeader = new HttpHeader();
        // 设置IP
        httpHeader.setUrl(CoreConfig.domainUrl);
        // 验证模式
        httpHeader.setAuthModel(AuthModel.AUTH_LOGIN);
        httpTask.getRequestMessage().header.setHttpHeader(httpHeader);
        String username;
        String password;
        username = binding.username.getText().toString().trim();
        password = binding.password.getText().toString().trim();
        httpTask.getRequestMessage().body.setItemParaListWithKeys(
                "userName", username,
                "password", MD5.md5(password));
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