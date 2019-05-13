package google.architecture.pending.console.video;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
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

import java.util.ArrayList;
import java.util.List;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.util.StorageUtils;
import google.architecture.coremodel.datamodel.http.entities.VideoListData;
import google.architecture.coremodel.util.NetUtils;
import google.architecture.pending.console.video.bean.HeaderList;
import io.reactivex.disposables.CompositeDisposable;

import static com.tencent.open.utils.Global.getSharedPreferences;

/**
 * @description:
 * @author: HuaiAngg
 * @create: 2019-05-13 12:40
 */
public class VideoViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private static final MutableLiveData ABSENT = new MutableLiveData();

    public LiveData<VideoListData> getmLiveObservableData() {
        return mLiveObservableData;
    }

    private LiveData<VideoListData> mLiveObservableData;
    public ObservableField<VideoListData> uiObservableData;
    private final CompositeDisposable mDisposable;
    private HttpTask httpTask;
    private SharedPreferences mPreference;
    private MutableLiveData<VideoListData> applyData;

    public VideoViewModel(@NonNull Application application) {
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

                        CoreConfig.domainUrl = "http://oa.leadisoft.com:18081/lead_portal";

                        Log.e("--Application", "-> " + CoreConfig.domainUrl);

                        // 设置api类型之一
                        httpTask.setServiceType("api");

                        HttpHeader httpHeader = new HttpHeader();
                        // 设置IP
                        httpHeader.setUrl(CoreConfig.domainUrl);
                        // 取出token
                        String token = StorageUtils.getStringInCache("SP_TOKEN", "token", "null");
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
                        Log.d("VideoViewModel", "onMessageResponse: " + responseMessage.getJsonText());
                        Log.d("VideoViewModel", "Status: " + responseMessage.header.getStatus());
//                        Log.d("VideoViewModel", "List: " + responseMessage.body.getBody().optString("list"));

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
//                        Toast.makeText(getApplication(),
//                                "加载成功!!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUploading(HttpTask httpTask, long l, long l1) {

                    }
                });
    }

    private VideoListData.BodyBean json2BodyBean(String json) {
        VideoListData.BodyBean bodyBeanList  = new Gson().fromJson(json,
                new TypeToken<List<VideoListData.BodyBean>>(){}.getType());
        return bodyBeanList;
    }


    private HeaderList json2HeaderBean(String json) {
        Gson gson = new Gson();
        HeaderList headerList = gson.fromJson(json, HeaderList.class);
        return headerList;
    }

    public LiveData<VideoListData> getLiveObservableData() {
        return this.mLiveObservableData;
    }

    public void setmLiveObservableData(LiveData<VideoListData> mLiveObservableData) {
        this.mLiveObservableData = mLiveObservableData;
    }

    public void setUiObservableData(VideoListData product) {
        this.uiObservableData.set(product);
    }

    protected void onCleared() {
        super.onCleared();
        this.mDisposable.clear();
        LogUtils.d("=======TodoViewModel--onCleared=========");
    }
}
