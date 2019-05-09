package google.architecture.pending;

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

import google.architecture.coremodel.datamodel.http.entities.TodoData;
import google.architecture.coremodel.util.NetUtils;
import io.reactivex.disposables.CompositeDisposable;

import static google.architecture.pending.utils.Constants.TODO_CONSOLE_LIST_ICON;
import static google.architecture.pending.utils.Constants.TODO_CONSOLE_LIST_NAME;

public class TodoViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private static final MutableLiveData ABSENT = new MutableLiveData();
    private LiveData<TodoData> mLiveObservableData;
    public ObservableField<TodoData> uiObservableData;
    private final CompositeDisposable mDisposable;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        ABSENT.setValue((Object)null);
        this.uiObservableData = new ObservableField();
        this.mDisposable = new CompositeDisposable();
        Log.i("danxx", "TodoViewModel------>");
        this.mLiveObservableData = Transformations.switchMap(NetUtils.netConnected(application),
                new Function<Boolean, LiveData<TodoData>>() {
            public LiveData<TodoData> apply(Boolean isNetConnected) {
                Log.i("danxx", "apply------>");
                if (!isNetConnected) {
                    return TodoViewModel.ABSENT;
                } else {
                    final MutableLiveData<TodoData> applyData = new MutableLiveData();
                    TodoData todoData = new TodoData();

                    // 控制台
                    List<TodoData.ConsoleResultsBean> resultsBeanList = new ArrayList<>();
                    Log.i("danxx", "setConsoleValue------>");
                    TodoData.ConsoleResultsBean consoleResults;
                    for (int i = 0; i < TODO_CONSOLE_LIST_ICON.length; i++) {
                        consoleResults = new TodoData.ConsoleResultsBean();
                        consoleResults.setIcon(TODO_CONSOLE_LIST_ICON[i]);
                        consoleResults.setName(TODO_CONSOLE_LIST_NAME[i]);
                        resultsBeanList.add(consoleResults);
                    }
                    todoData.setConsoleResults(resultsBeanList);

                    // 待办列表
                    List<TodoData.PendingResultsBean> pendingResultsBeanList = new ArrayList<>();
                    Log.i("danxx", "setConsoleValue------>");
                    TodoData.PendingResultsBean pendingResults = new TodoData.PendingResultsBean();
                    for (int i = 0; i < 3; i++) {
                        pendingResults.setIcon(R.drawable.workpending_pending_information_ic);
                        pendingResults.setTitle("关于2019年春节放假连休");
                        pendingResults.setAuthor("创建人：杨鹤");
                        pendingResults.setUpdateTime("09-18");

                        pendingResultsBeanList.add(pendingResults);
                    }
                    todoData.setPendingResults(pendingResultsBeanList);

                    // 通知列表
                    List<TodoData.NoticeResultsBean> noticeResultsBeanList = new ArrayList<>();
                    Log.i("danxx", "setConsoleValue------>");
                    TodoData.NoticeResultsBean noticeResultsBean = new TodoData.NoticeResultsBean();
                    for (int i = 0; i < 3; i++) {
                        noticeResultsBean.setIcon(R.drawable.workpending_notice_ic);
                        noticeResultsBean.setTitle("关于2019年春节放假连休");
                        noticeResultsBean.setAuthor("创建人：杨鹤");
                        noticeResultsBean.setUpdateTime("09-18");
                        noticeResultsBean.setReadNum("阅读量：165");

                        noticeResultsBeanList.add(noticeResultsBean);
                    }
                    todoData.setNoticeResults(noticeResultsBeanList);


                    applyData.setValue(todoData);
                    Log.i("danxx", "onComplete------>");

                    return applyData;
                }
            }
        });
    }

    public LiveData<TodoData> getLiveObservableData() {
        return this.mLiveObservableData;
    }

    public void setUiObservableData(TodoData product) {
        this.uiObservableData.set(product);
    }

    protected void onCleared() {
        super.onCleared();
        this.mDisposable.clear();
        LogUtils.d("=======TodoViewModel--onCleared=========");
    }
}
