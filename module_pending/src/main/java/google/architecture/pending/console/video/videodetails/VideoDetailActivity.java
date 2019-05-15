package google.architecture.pending.console.video.videodetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.pending.R;
import google.architecture.pending.databinding.ActivityVideoDetailBinding;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

@Route(path = ARouterPath.CONSOLE_VIDEO_DETAIL_ATY)
public class VideoDetailActivity extends BaseActivity {

    private ActivityVideoDetailBinding activityVideoDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVideoDetailBinding
                = DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        // ARouter
        ARouter.getInstance().inject(this);

        setSupportActionBar(activityVideoDetailBinding.toolbar);
        // 返回导航键
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("监控列表");

        // 在activity中加载so包，设置监听器，设置路径
//        try{
//            IjkMediaPlayer.loadLibrariesOnce(null);
//            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//        } catch (Exception e) {
//            this.finish();
//        }
    }

}
