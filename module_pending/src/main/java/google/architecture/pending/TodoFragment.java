package google.architecture.pending;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
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
import google.architecture.coremodel.datamodel.http.entities.TodoData;
import google.architecture.pending.console.TodoConsoleAdapter;
import google.architecture.pending.console.TodoConsoleClickCallback;
import google.architecture.pending.databinding.TodoFragmentBinding;
import google.architecture.pending.notice.TodoNoticeAdapter;
import google.architecture.pending.notice.TodoNoticeClickCallback;
import google.architecture.pending.pending.TodoPendingAdapter;
import google.architecture.pending.pending.TodoPendingClickCallback;
import google.architecture.pending.utils.NoScrollLinearView;

@Route(path = ARouterPath.PendingFgt)
public class TodoFragment extends BaseFragment {

    private TodoConsoleAdapter consoleAdapter;
    private TodoNoticeAdapter noticeAdapter;
    private TodoPendingAdapter pendingAdapter;
    private TodoViewModel todoViewModel;

    public static TodoFragment newInstance() {
        return new TodoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ARouter.getInstance().inject(TodoFragment.class);
        TodoFragmentBinding todoDataBinding = DataBindingUtil
                .inflate(inflater, R.layout.todo_fragment, container, false);

        // 初始化适配器
        consoleAdapter = new TodoConsoleAdapter(consoleClickCallback);
        noticeAdapter = new TodoNoticeAdapter(noticeClickCallback);
        pendingAdapter = new TodoPendingAdapter(pendingClickCallback);

        // 加载布局
        todoDataBinding.todoConsoleList.setLayoutManager(new GridLayoutManager(getContext(),
                4, LinearLayoutManager.VERTICAL, false));
        todoDataBinding.todoNoticeList.setLayoutManager(new NoScrollLinearView(getContext()));
        todoDataBinding.todoPendingList.setLayoutManager(new NoScrollLinearView(getContext()));

        // 设置适配器
        todoDataBinding.setRecyclerAdapter(consoleAdapter);
        todoDataBinding.setNoticeRecyclerAdapter(noticeAdapter);
        todoDataBinding.setPendingRecyclerAdapter(pendingAdapter);

        todoViewModel = ViewModelProviders.of(TodoFragment.this).get(TodoViewModel.class);
        subscribeToModel(todoViewModel);
        return todoDataBinding.getRoot();
    }

    // 控制台回调事件
    TodoConsoleClickCallback consoleClickCallback = new TodoConsoleClickCallback() {
        @Override
        public void onClick(TodoData.ConsoleResultsBean newsItem, int position) {
            switch (position) {
                case 1:
                    Log.i("danxx", "onClick toGirls");
                    //跳转到VideoActivity
                    ARouter.getInstance()
                            .build(ARouterPath.CONSOLE_VIDEO_LIST_ATY)
                            .withTransition(R.anim.activity_up_in, R.anim.activity_up_out)
                            .navigation(getContext());
                    break;
                default:
                    Toast.makeText(getContext(), newsItem.getName() + position, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    // 通知
    TodoNoticeClickCallback noticeClickCallback = new TodoNoticeClickCallback() {
        @Override
        public void onClick(TodoData.NoticeResultsBean newsItem) {
            Toast.makeText(getContext(), newsItem.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };

    // 待办
    TodoPendingClickCallback pendingClickCallback = new TodoPendingClickCallback() {
        @Override
        public void onClick(TodoData.PendingResultsBean newsItem) {
            Toast.makeText(getContext(), newsItem.getTitle(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mViewModel = ViewModelProviders.of(this).get(TodoViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * 订阅数据变化来刷新UI
     *
     * @param model TodoViewModel
     */
    private void subscribeToModel(final TodoViewModel model) {
        //观察数据变化来刷新UI
        model.getLiveObservableData().observe(TodoFragment.this, new Observer<TodoData>() {
            @Override
            public void onChanged(@Nullable TodoData todoData) {
                Log.i("danxx", "subscribeToModel onChanged onChanged");
                model.setUiObservableData(todoData);
                // 设置数据列表
                consoleAdapter.setConsoleList(todoData != null ? todoData.getConsoleResults() : null);
                noticeAdapter.setNoticeList(todoData != null ? todoData.getNoticeResults() : null);
                pendingAdapter.setPendingList(todoData != null ? todoData.getPendingResults() : null);
            }
        });
    }

}
