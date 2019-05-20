package google.architecture.pending.console.video.videodetails;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import google.architecture.common.util.SPUtils;
import google.architecture.coremodel.datamodel.http.entities.VideoDetailData;
import google.architecture.coremodel.datamodel.http.entities.VideoListData;
import google.architecture.coremodel.util.NetUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Function: VideoDetailData
 * <p>
 * 作者: yanhuaian@just-tech.com.cn
 * 版权: Copyright (c) 2019
 * 公司: 广州佳时达软件股份有限公司
 * 日期: 2019-05-16
 */
public class VideoDetailViewModel extends AndroidViewModel {

    // TODO: Implement the ViewModel
    private static final MutableLiveData ABSENT = new MutableLiveData();

    public LiveData<VideoDetailData.EquipmentBean> getmLiveObservableData() {
        return mLiveObservableData;
    }

    private LiveData<VideoDetailData.EquipmentBean> mLiveObservableData;
    private ObservableField<VideoDetailData.EquipmentBean> uiObservableData;
    private final CompositeDisposable mDisposable;
//    private HttpTask httpTask;
    private MutableLiveData<VideoDetailData.EquipmentBean> applyData;


    public VideoDetailViewModel(@NonNull Application application) {
        super(application);
        ABSENT.setValue((Object)null);
        this.uiObservableData = new ObservableField();
        this.mDisposable = new CompositeDisposable();
        Log.i("danxx", "TodoViewModel------>");
        this.mLiveObservableData = Transformations.switchMap(NetUtils.netConnected(application),
                new Function<Boolean, LiveData<VideoDetailData.EquipmentBean>>() {
                    public LiveData<VideoDetailData.EquipmentBean> apply(Boolean isNetConnected) {
                        applyData = new MutableLiveData();
                        String listStr = SPUtils.getStringInCache("EQUIPMENT_LIST", "bodyBean", "");
                        int itemPosition = SPUtils.getIntInCache("VIDEO_LIST_POSITION", "position", 0);
                        Log.e("VideoDetailViewModel", "apply: " + listStr);
                        VideoDetailData videoDetailData = new Gson().fromJson(listStr,
                                new TypeToken<VideoDetailData>(){}.getType());
                        applyData.setValue(videoDetailData.getList().get(itemPosition));
                        return applyData;
                    }
                });
    }

    LiveData<VideoDetailData.EquipmentBean> getLiveObservableData() {
        return this.mLiveObservableData;
    }

    void setUiObservableData(VideoDetailData.EquipmentBean product) {
        this.uiObservableData.set(product);
    }

    protected void onCleared() {
        super.onCleared();
        this.mDisposable.clear();
        LogUtils.d("=======VideoDetailViewModel--Cleared=========");
    }
}
