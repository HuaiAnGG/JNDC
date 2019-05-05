package google.architecture.coremodel.datamodel.http.repository;

import google.architecture.coremodel.datamodel.http.ApiClient;
import google.architecture.coremodel.datamodel.http.entities.GirlsData;
import google.architecture.coremodel.datamodel.http.entities.LoginData;
import google.architecture.coremodel.util.JsonUtil;
import google.architecture.coremodel.util.SwitchSchedulers;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 登陆信息接口
 */
public class LoginDataReository {

//    public static Observable<LoginData> getLoginData(RequestBody body){
//
//        Observable<LoginData> observableForPostLoginDataFromNetWork
//                = ApiClient.getLoginDataService().getLoginData(body);
//
//        //可以操作Observable来筛选网络或者是本地数据
//
//        return observableForPostLoginDataFromNetWork;
//    }

    public static <T>Observable getLoginData(String pullUrl, final Class<T> clazz) {

        return
                ApiClient
                        .getDynamicDataService()
                        .getDynamicData(pullUrl)
                        .compose(SwitchSchedulers.applySchedulers())
                        .map(new Function<ResponseBody, T>() {
                            @Override
                            public T apply(ResponseBody responseBody) throws Exception {
                                return JsonUtil.Str2JsonBean(responseBody.string(), clazz);
                            }
                        });
    }
}
