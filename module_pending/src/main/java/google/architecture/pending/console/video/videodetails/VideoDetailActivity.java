package google.architecture.pending.console.video.videodetails;

import android.arch.lifecycle.Observer;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.common.util.SPUtils;
import google.architecture.coremodel.datamodel.http.entities.VideoDetailData;
import google.architecture.coremodel.viewmodel.ViewModelProviders;
import google.architecture.pending.R;
import google.architecture.pending.databinding.ActivityVideoDetailBinding;

@Route(path = ARouterPath.CONSOLE_VIDEO_DETAIL_ATY)
public class VideoDetailActivity extends BaseActivity {

    private ActivityVideoDetailBinding activityVideoDetailBinding;
    private VideoDetailViewModel videoDetailViewModel;
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVideoDetailBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        videoDetailViewModel = ViewModelProviders.of(this).get(VideoDetailViewModel.class);
        itemPosition = SPUtils.getIntInCache("VIDEO_LIST_POSITION", "position", 0);
        activityVideoDetailBinding.setPosition(itemPosition);
        activityVideoDetailBinding.setEquipmentDetail(videoDetailViewModel.getmLiveObservableData().getValue());
        // ARouter
        ARouter.getInstance().inject(this);

        setSupportActionBar(activityVideoDetailBinding.toolbar);
        // 返回导航键
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("监控列表");

        subscribeToModel(videoDetailViewModel);
        // 在activity中加载so包，设置监听器，设置路径
//        try{
//            IjkMediaPlayer.loadLibrariesOnce(null);
//            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//        } catch (Exception e) {
//            this.finish();
//        }
        activityVideoDetailBinding.executePendingBindings();
    }

    /**
     * 订阅数据变化来刷新UI
     *
     * @param model TodoViewModel
     */
    private void subscribeToModel(final VideoDetailViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveObservableData().observe(this, new Observer<VideoDetailData.EquipmentBean>() {
            @Override
            public void onChanged(@Nullable VideoDetailData.EquipmentBean videoDetailDataList) {
                Log.i("danxx", "subscribeToModel onChanged onChanged");
                model.setUiObservableData(videoDetailDataList);
                // 设置数据列表
            }
        });
    }

}
