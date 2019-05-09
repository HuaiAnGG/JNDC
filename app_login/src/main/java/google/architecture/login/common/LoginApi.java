package google.architecture.login.common;

import google.architecture.coremodel.datamodel.http.entities.LoginRequestMessage;
import google.architecture.coremodel.datamodel.http.entities.LoginResopnseMessage;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("/login/checkLogin.lead/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Call<LoginResopnseMessage> getLoginInfo(@Body LoginRequestMessage request);

    @POST("checkLogin.lead/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<LoginResopnseMessage> getLogin(@Body LoginRequestMessage request);

    @POST("checkLogin.lead/")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable getLoginWithoutParam(@Body LoginRequestMessage request);
}
