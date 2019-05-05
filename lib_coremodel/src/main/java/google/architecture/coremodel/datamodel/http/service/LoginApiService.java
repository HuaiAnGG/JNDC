package google.architecture.coremodel.datamodel.http.service;

import google.architecture.coremodel.datamodel.http.entities.LoginData;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("/login/checkLogin.lead")
    Observable<LoginData> getLoginData(@Body RequestBody body);
}
