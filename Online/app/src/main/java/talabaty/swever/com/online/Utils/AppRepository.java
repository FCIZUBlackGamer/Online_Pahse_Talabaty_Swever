package talabaty.swever.com.online.Utils;

import android.app.Application;

import com.android.volley.NetworkError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class AppRepository {
    private static AppRepository ourInstance = null;
    private static VolleyRequestQueue mRequestQueue = null;
    private AppExecutors mExecutors;
    private Application mApplication;

    @NonNull
    public static synchronized AppRepository getInstance(@NonNull Application application) {
        if (ourInstance == null) ourInstance = new AppRepository(application);

        return ourInstance;
    }

    private AppRepository(@NonNull Application application) {
        mRequestQueue = VolleyRequestQueue.getInstance(application);
        mRequestQueue = VolleyRequestQueue.getInstance(application);
        mExecutors = AppExecutors.getInstance();

        mApplication = application;
    }

    public LiveData<String> login(String userName, String password) {
        MutableLiveData<String> responseLiveData = new MutableLiveData<>();

        addToRequestQueue(
                APIParamsUtil.LOGIN_URL,
                APIParamsUtil.createLoginParams(userName, password),
                responseLiveData::postValue,
                error -> {
                    responseLiveData.postValue("STRING_TO_HIDE_THE_PROGRESS_BAR");

                    mExecutors.mainThread().execute(() -> {
                        String WarningMessage = null;
                        if (error instanceof ServerError)
                            WarningMessage = "خطأ فى الاتصال بالخادم";
                        else if (error instanceof TimeoutError)
                            WarningMessage = "خطأ فى مدة الاتصال";
                        else if (error instanceof NetworkError)
                            WarningMessage = "شبكه الانترنت ضعيفه حاليا";

                        if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                                AppToastUtil.LENGTH_LONG, mApplication);
                    });
                });

        return responseLiveData;
    }

    private void addToRequestQueue(String url,
                                   Map<String, String> params,
                                   Response.Listener<String> listener,
                                   Response.ErrorListener errorListener) {
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST,
                url,
                listener,
                errorListener) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        mRequestQueue.addToRequestQueue(request);
    }
}
