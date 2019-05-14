package google.architecture.pending.console.videodetails;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.pending.R;
import google.architecture.pending.databinding.ActivityVideoDetailBinding;

@Route(path = ARouterPath.CONSOLE_VIDEO_DETAIL_ATY)
public class VideoDetailActivity extends BaseActivity {

    private ActivityVideoDetailBinding activityVideoDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVideoDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_video_detail);
        // ARouter
        ARouter.getInstance().inject(this);

        setSupportActionBar(activityVideoDetailBinding.toolbar);
        // 返回导航键
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("监控列表");
    }

}
