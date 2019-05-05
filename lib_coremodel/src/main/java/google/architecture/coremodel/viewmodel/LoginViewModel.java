package google.architecture.coremodel.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import google.architecture.coremodel.R;
import google.architecture.coremodel.datamodel.http.entities.LoginData;
import io.reactivex.disposables.CompositeDisposable;

public class LoginViewModel extends AndroidViewModel {

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    //生命周期观察的数据
    private LiveData<LoginData> mLiveObservableData;
    //UI使用可观察的数据 ObservableField是一个包装类
    public ObservableField<LoginData> uiObservableData = new ObservableField<>();

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        Log.i("danxx", "GirlsViewModel------>");
    }

}
