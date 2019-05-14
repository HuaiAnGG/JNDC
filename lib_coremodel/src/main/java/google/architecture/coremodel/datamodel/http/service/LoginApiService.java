package google.architecture.coremodel.datamodel.http.service;

import google.architecture.coremodel.datamodel.http.entities.LoginRequestMessage;
import google.architecture.coremodel.datamodel.http.entities.LoginResopnseMessage;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {

    @POST("/lead_portal/api/login/checkLogin.lead")
    Observable<LoginResopnseMessage> getLoginData(@Body LoginRequestMessage requestMessage);
}
