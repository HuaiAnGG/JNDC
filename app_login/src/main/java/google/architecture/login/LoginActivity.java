package google.architecture.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.just.auth.iscs.impl.ISCSAuthImpl;
import com.just.core.common.AuthModel;
import com.just.core.common.CoreConfig;
import com.just.core.http.HttpEngine;
import com.just.core.http.HttpHeader;
import com.just.core.http.HttpTask;
import com.just.core.listener.MessageCallbackListener;
import com.just.core.message.RequestMessage;
import com.just.core.message.ResponseMessage;
import com.just.core.util.MD5;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.coremodel.datamodel.http.entities.LoginRequestMessage;
import google.architecture.coremodel.datamodel.http.entities.LoginResopnseMessage;
import google.architecture.coremodel.datamodel.http.repository.LoginDataReository;
import google.architecture.coremodel.viewmodel.LoginViewModel;
import google.architecture.login.databinding.ActivityLoginBinding;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
//            doTask();
            LoginRequestMessage loginRequestMessage = new LoginRequestMessage();
            loginRequestMessage.getBody().setPassword(MD5.md5("1"));
            loginRequestMessage.getBody().setUserName("admin");

            loginRequestMessage.getHeader().setAppcode("A024");
            loginRequestMessage.getHeader().setDatatype(2);
            loginRequestMessage.getHeader().setFunc("loginApp");
            loginRequestMessage.getHeader().setSeqid("");
            loginRequestMessage.getHeader().setTimestamp("20151225154016");

//            Log.e("LoginRequestMessage", loginRequestMessage.toString());

//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://oa.leadisoft.com:18081/lead_portal/api/login/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .build();
//
//            Log.e("===================", retrofit.baseUrl().toString());
//
//            LoginApi service = retrofit.create(LoginApi.class);
//
//            Observable<LoginResopnseMessage> observable = service.getLogin(loginRequestMessage);
//            observable.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<LoginResopnseMessage>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            Log.e("=====================", "Disposable" );
//                        }
//
//                        @Override
//                        public void onNext(LoginResopnseMessage value) {
//                            Log.e("====================", value.toString());
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.e("===================","error info:", e);
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Log.e("====================", "onComplete: " );
//                        }
//                    });

            final RequestMessage requestMessage = new RequestMessage();
            requestMessage.header.setDataType(2);
            requestMessage.header.setAppcode("A024");
            requestMessage.header.setFunc("loginApp");
            requestMessage.header.setTimestamp("20151225154016");
            requestMessage.header.setSeqId("");

            requestMessage.body.setItemParaListWithKeys(
                    "userName", "admin",
                    "password", "c4ca4238a0b923820dcc509a6f75849b"
            );

            LoginDataReository.getLoginData(loginRequestMessage, LoginResopnseMessage.class)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginResopnseMessage>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(LoginResopnseMessage msg) {

                            if (msg == null) {
                                return;
                            }

                            String status = msg.getHeader().getStatus();

                            if ("200".equals(status)) {
                                ARouter.getInstance()
                                        .build(ARouterPath.MainAty)
                                        /**可以针对性跳转跳转动画*/
                                        .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                                        .navigation(LoginActivity.this);
                                Toast.makeText(getApplicationContext(), "登录成功!", Toast.LENGTH_LONG).show();
                                return;
                            }

                            String info = msg.getHeader().getInfo();
                            if ("HTTP请求响应数据失败".equals(info)) {
                                info = "网络连接出错";
                            }
                            Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });

//            doTask();
        }
    };

    private void initHttpTask() {

        httpTask = new HttpTask("A024", "loginApp", HttpTask.Type.COMMON,
                new MessageCallbackListener() {
                    @Override
                    public void onWaiting(HttpTask httpTask) {

                    }

                    @Override
                    public void onMessageResponse(HttpTask httpTask, ResponseMessage responseMessage) {
                        Log.i("onMessageResponse: ", responseMessage.toString());

                        ARouter.getInstance()
                                .build(ARouterPath.MainAty)
                                /**可以针对性跳转跳转动画*/
                                .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                                .navigation(LoginActivity.this);
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


        CoreConfig.appCode = "A023";
//        CoreConfig.commandCodeAuth = Cmd.cmdMap;

//        CoreConfig.domainUrl 		= SpfUtils.getSpf(getApplicationContext(), Constants.SPF_DOMAIN_URL, BuildConfig.DOMAIN_URL);
//        CoreConfig.domainUploadUrl 	= SpfUtils.getSpf(getApplicationContext(), Constants.SPF_DOMAIN_UPLOAD, BuildConfig.DOMAIN_URL_PIC);

        CoreConfig.domainUrl = "http://oa.leadisoft.com:18081/lead_portal/api/login/checkLogin.lead/";
        CoreConfig.domainUploadUrl = "http://oa.leadisoft.com:18081/lead_portal/api/login/checkLogin.lead/";

        Log.e("--App", "-> " + CoreConfig.domainUrl);
        Log.e("--App", "-> " + CoreConfig.domainUploadUrl);

        CoreConfig.setAuthGenerator(new ISCSAuthImpl());

        HttpHeader httpHeader = new HttpHeader();
        httpHeader.setUrl(CoreConfig.domainUrl);
        httpTask.getRequestMessage().header.setHttpHeader(httpHeader);

        // 验证模式
//        httpTask.getRequestMessage().header.setAppcode("A024");
//        httpTask.getRequestMessage().header.setFunc("loginApp");
//        httpTask.getRequestMessage().header.setTimestamp("20151225154016");
//        httpTask.getRequestMessage().header.setDataType(2);
        httpTask.getRequestMessage().header.getHttpHeader().setAuthModel(AuthModel.AUTH_DEFAULT);

//        httpTask.getRequestMessage().body.setItemParaListWithKeys(
//                "userName", binding.username.getText().toString(),
//                "password", binding.password.getText().toString()
//        );

        httpTask.getRequestMessage().body.setItemValue("userName", "admin");
        httpTask.getRequestMessage().body.setItemValue("password", MD5.md5("1"));
        httpTask.getRequestMessage().header.setTimestamp("20190501172202");
        Log.e("--HttpUtil", "[HTTP RQ]" + httpTask.getRequestMessage().toJSON());

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