package google.architecture.specific;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.ArrayList;
import java.util.List;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseActivity;
import google.architecture.common.base.BaseFragment;
import google.architecture.common.widget.NoScrollViewPager;
import google.architecture.girls.FragmentGirls;
import google.architecture.specific.databinding.ActivityMainBinding;

@Route(path = ARouterPath.MainAty)
public class ActivityMain extends BaseActivity {

    ActivityMainBinding mainBinding;
    private NoScrollViewPager mPager;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private FragmentAdapter mAdapter;

    public BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int i = item.getItemId();
            if (i == R.id.navigation_newest_news) {
                mPager.setCurrentItem(0);
                return true;
            } else if (i == R.id.navigation_todo_work) {
                mPager.setCurrentItem(1);
                return true;
            } else if (i == R.id.navigation_inner_email) {
                mPager.setCurrentItem(2);
                return true;
            } else if (i == R.id.navigation_about_me) {
                mPager.setCurrentItem(3);
                return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(ActivityMain.this);

        mainBinding = DataBindingUtil.setContentView(
                ActivityMain.this, R.layout.activity_main);

        BottomNavigationViewHelper.disableShiftMode(mainBinding.navigation);
        mainBinding.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        mPager = mainBinding.containerPager;
        mPager.setOffscreenPageLimit(3);

        BaseFragment fragmentNews = (BaseFragment) ARouter.getInstance().build(ARouterPath.NewsListFgt).navigation();
        BaseFragment fragmentGirls = (BaseFragment) ARouter.getInstance().build(ARouterPath.GirlsListFgt).navigation();
        BaseFragment fragmentAbout = (BaseFragment) ARouter.getInstance().build(ARouterPath.AboutFgt).navigation();
        BaseFragment fragmentPending = (BaseFragment) ARouter.getInstance().build(ARouterPath.PendingFgt).navigation();
//        BaseFragment fragmentPending = (BaseFragment) new To

        mFragments.add(fragmentNews);
        mFragments.add(fragmentPending);
        mFragments.add(fragmentGirls);
        mFragments.add(fragmentAbout);

        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mFragments);
        mainBinding.setViewPaAdapter(mAdapter);

    }
}
