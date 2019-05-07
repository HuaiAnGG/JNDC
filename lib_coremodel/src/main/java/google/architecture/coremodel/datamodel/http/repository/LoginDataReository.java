package google.architecture.coremodel.datamodel.http.repository;

import com.google.gson.Gson;
import com.just.core.message.RequestMessage;
import com.just.core.message.ResponseMessage;

import google.architecture.coremodel.datamodel.http.ApiClient;
import google.architecture.coremodel.datamodel.http.entities.LoginRequestMessage;
import google.architecture.coremodel.datamodel.http.entities.LoginResopnseMessage;
import google.architecture.coremodel.util.JsonUtil;
import google.architecture.coremodel.util.SwitchSchedulers;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * 登陆信息接口
 */
public class LoginDataReository {

    public static <T>Observable getLoginData(LoginRequestMessage requestMessage, final Class<T> clazz) {

        Gson gson = new Gson();

        return
                ApiClient
                        .getLoginDataService()
                        .getLoginData(requestMessage)
                        .compose(SwitchSchedulers.applySchedulers())
                        .map(new Function<LoginResopnseMessage, T>() {
                            @Override
                            public T apply(LoginResopnseMessage responseMessage) throws Exception {
                                return JsonUtil.Str2JsonBean(gson.toJson(responseMessage, LoginResopnseMessage.class), clazz);
                            }
                        });
    }
}
