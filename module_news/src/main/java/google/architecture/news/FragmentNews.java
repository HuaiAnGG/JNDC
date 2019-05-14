package google.architecture.news;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import google.architecture.common.base.ARouterPath;
import google.architecture.common.base.BaseFragment;
import google.architecture.coremodel.datamodel.http.entities.NewsData;
import google.architecture.coremodel.viewmodel.NewsViewModel;
import google.architecture.coremodel.viewmodel.ViewModelProviders;
import google.architecture.news.databinding.FragmentNewsBinding;


/**
 * @Desc FragmentNews
 */
@Route(path = ARouterPath.NewsListFgt)
public class FragmentNews extends BaseFragment {

    FragmentNewsBinding newsBinding;
    NewsAdapter         newsAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentNews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentNews.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentNews newInstance(String param1, String param2) {
        FragmentNews fragment = new FragmentNews();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ARouter.getInstance().inject(FragmentNews.this);
        // Inflate the layout for this fragment
        newsBinding = DataBindingUtil
                .inflate(inflater,R.layout.fragment_news, container, false);

//        newsBinding.newsList.setLayoutManager(new LinearLayoutManager(
//                getContext(), LinearLayoutManager.VERTICAL, false));

        newsAdapter = new NewsAdapter(newsItemClickCallback);
        newsBinding.setRecyclerAdapter(newsAdapter);
        final NewsViewModel newsViewModel
                = ViewModelProviders.of(FragmentNews.this).get(NewsViewModel.class);

        subscribeToModel(newsViewModel);

        return newsBinding.getRoot();
    }

    NewsItemClickCallback newsItemClickCallback = new NewsItemClickCallback() {
        @Override
        public void onClick(NewsData.ResultsBean newsItem) {
            Toast.makeText(getContext(), newsItem.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 订阅数据变化来刷新UI
     * @param model NewsViewModel
     */
    private void subscribeToModel(final NewsViewModel model){
        //观察数据变化来刷新UI
        model.getLiveObservableData().observe(FragmentNews.this, new Observer<NewsData>() {
            @Override
            public void onChanged(@Nullable NewsData newsData) {
                Log.i("danxx", "subscribeToModel onChanged onChanged");
                Log.i("newsData -------->>>>", newsData != null ? newsData.toString() : null);
                model.setUiObservableData(newsData);
                newsAdapter.setNewsList(newsData.getResults());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        newsBinding = null;
    }
}
