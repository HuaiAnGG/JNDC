package google.architecture.coremodel.viewmodel;

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

import java.util.ArrayList;
import java.util.List;

import google.architecture.coremodel.datamodel.http.entities.NewsData;
import google.architecture.coremodel.util.NetUtils;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dxx on 2017/11/17.
 */

public class NewsViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    static {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    //生命周期观察的数据
    private LiveData<NewsData> mLiveObservableData;
    //UI使用可观察的数据 ObservableField是一个包装类
    private ObservableField<NewsData> uiObservableData = new ObservableField<>();

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public NewsViewModel(@NonNull Application application) {
        super(application);
        Log.i("danxx", "NewsViewModel------>");
        //这里的trigger为网络检测，也可以换成缓存数据是否存在检测
        mLiveObservableData = Transformations.switchMap(NetUtils.netConnected(application),
                (Function<Boolean, LiveData<NewsData>>) isNetConnected -> {

                    Log.i("danxx", "apply------>");
                    if (!isNetConnected) {
                        return ABSENT; //网络未连接返回空
                    }
                    MutableLiveData<NewsData> applyData = new MutableLiveData<>();

                    NewsData newsData = new NewsData();
                    List<NewsData.ResultsBean> resultsBeanList = new ArrayList<>();

                    Log.i("danxx", "setValue------>");
                    NewsData.ResultsBean resultsBean = new NewsData.ResultsBean();
                    resultsBean.setTitle("人勤春来早，年后开工忙，收心聚力再出发");
                    resultsBean.setAuthor("编辑人：岑仕诚");
                    resultsBean.setApartment("燃料部/运行分部");
                    resultsBean.setUpdateTime("2019-02-19 11:58");

                    for (int i = 0; i < 4; i++) {
                        resultsBeanList.add(resultsBean);
                    }
                    newsData.setResults(resultsBeanList);

                    applyData.setValue(newsData);
                    Log.i("danxx", "onComplete------>");
                    return applyData;
                });

    }
    /**
     * LiveData支持了lifecycle生命周期检测
     * @return mLiveObservableData
     */
    public LiveData<NewsData> getLiveObservableData() {
        return mLiveObservableData;
    }

    /**
     * 设置
     * @param product NewsData
     */
    public void setUiObservableData(NewsData product) {
        this.uiObservableData.set(product);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        LogUtils.d("========NewsViewModel--onCleared=========");
        mDisposable.clear();
    }
}
