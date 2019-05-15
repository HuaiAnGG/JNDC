package google.architecture.pending.console.video.videolist;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.just.core.common.AuthModel;
import com.just.core.common.CoreConfig;
import com.just.core.http.HttpEngine;
import com.just.core.http.HttpHeader;
import com.just.core.http.HttpTask;
import com.just.core.listener.MessageCallbackListener;
import com.just.core.message.ResponseMessage;

import org.json.ext.JSONObject;

import google.architecture.common.util.Constants;
import google.architecture.common.util.SPUtils;
import google.architecture.coremodel.datamodel.http.entities.VideoListData;
import google.architecture.coremodel.util.NetUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 12:40
 */
public class VideoListViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private static final MutableLiveData ABSENT = new MutableLiveData();

    public LiveData<VideoListData> getmLiveObservableData() {
        return mLiveObservableData;
    }

    private LiveData<VideoListData> mLiveObservableData;
    private ObservableField<VideoListData> uiObservableData;
    private final CompositeDisposable mDisposable;
    private HttpTask httpTask;
    private MutableLiveData<VideoListData> applyData;

    public VideoListViewModel(@NonNull Application application) {
        super(application);
        ABSENT.setValue((Object)null);
        this.uiObservableData = new ObservableField();
        this.mDisposable = new CompositeDisposable();
        Log.i("danxx", "TodoViewModel------>");
        this.mLiveObservableData = Transformations.switchMap(NetUtils.netConnected(application),
                new Function<Boolean, LiveData<VideoListData>>() {
                    public LiveData<VideoListData> apply(Boolean isNetConnected) {
                        applyData = new MutableLiveData();
                        if (httpTask == null) {
                            initHttpTask();
                        }

                        CoreConfig.domainUrl = Constants.APP_URL;

                        Log.e("--Application", "-> " + CoreConfig.domainUrl);

                        // 设置api类型之一
                        httpTask.setServiceType("api");

                        HttpHeader httpHeader = new HttpHeader();
                        // 设置IP
                        httpHeader.setUrl(CoreConfig.domainUrl);
                        // 取出token
                        String token = SPUtils.getStringInCache("SP_TOKEN", "token", "null");
                        Log.e("TOKEN", "==================" + token);
                        // 设置token
//                        httpHeader.setToken(token);

                        httpTask.getRequestMessage().body.setItemParaListWithKeys(
                                "pageno", "1",
                                "key", "");
                        // 验证模式
//                        httpHeader.setAuthModel(AuthModel.AUTH_LOGIN);
                        httpTask.getRequestMessage().header.getHttpHeader().setAuthModel(AuthModel.AUTH_DEFAULT);
                        httpTask.getRequestMessage().header.getHttpHeader().setToken(token);
                        // 打印
                        Log.d("--HttpUtil", "[HTTP RQ]" + httpTask.getRequestMessage().toJSON());

                        HttpEngine.getInstance().emit(httpTask);
                        return applyData;
                    }
                });
    }

    private void initHttpTask() {
        httpTask = new HttpTask("videoDevice", "getVideoDevice.lead",
                new MessageCallbackListener() {
                    @Override
                    public void onWaiting(HttpTask httpTask) {

                    }

                    @Override
                    public void onMessageResponse(HttpTask httpTask, ResponseMessage responseMessage) {
                        Log.d("VideoListViewModel", "onMessageResponse: " + responseMessage.getJsonText());

                        if (responseMessage == null) {
                            Log.i("******************", "ResponseMessage is null!");
                            return;
                        }

                        if ("309".equals(responseMessage.header.getStatus())) {
                            Toast.makeText(getApplication(),
                                    "空消息!!!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if ("200".equals(responseMessage.header.getStatus())) {
                            JSONObject body = responseMessage.body.getBody();
                            Log.d("JSONObject", "onMessageResponse: " + body);
                            String str = body.optString("list");

                            if (!TextUtils.isEmpty(str)) {
                                str = body.toString();
                                Log.e("======!!!!!====", str);
                                // 保存上次获取的bodyBean
                                SPUtils.saveInDisk("EQUIPMENT_LIST", "bodyBean", str);
                                VideoListData videoListData = new VideoListData();
                                VideoListData.BodyBean bodyBean = new Gson().fromJson(str,
                                        new TypeToken<VideoListData.BodyBean>(){}.getType());
                                videoListData.setBody(bodyBean);
                                applyData.setValue(videoListData);
                            }

                            Toast.makeText(getApplication(),
                                    "加载成功!!!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String info = responseMessage.header.getInfo();
                        Toast.makeText(getApplication(), info, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFinished(HttpTask httpTask) {
                    }

                    @Override
                    public void onUploading(HttpTask httpTask, long l, long l1) {

                    }
                });
    }

    LiveData<VideoListData> getLiveObservableData() {
        return this.mLiveObservableData;
    }

    void setUiObservableData(VideoListData product) {
        this.uiObservableData.set(product);
    }

    protected void onCleared() {
        super.onCleared();
        this.mDisposable.clear();
        LogUtils.d("=======TodoViewModel--Cleared=========");
    }
}
