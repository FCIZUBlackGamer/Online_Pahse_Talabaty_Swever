package talabaty.swever.com.online.Utils;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
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
        return addToRequestQueue(APIURLUtil.LOGIN_URL,
                APIParamsUtil.createLoginParams(userName, password));
    }

    public LiveData<String> uploadImage(String image, String name) {
        return addToRequestQueue(APIURLUtil.UPLOAD_URL,
                APIParamsUtil.createUploadImageParams(image, name));
    }

    public LiveData<String> addUser(String user) {
        return addToRequestQueue(APIURLUtil.ADD_USER_URL,
                APIParamsUtil.createAddUserParams(user));
    }

    public LiveData<String> listProducts(Integer count) {
        return addToRequestQueue(APIURLUtil.LIST_PRODUCTS_URL,
                APIParamsUtil.createListProductsParams(count));
    }

    public LiveData<String> listOffers(Integer count) {
        return addToRequestQueue(APIURLUtil.LIST_OFFERS_URL,
                APIParamsUtil.createListOffersParams(count));
    }

    public LiveData<String> listShops(Integer count) {
        return addToRequestQueue(APIURLUtil.LIST_SHOPS_URL,
                APIParamsUtil.createListShopsParams(count));
    }

    public LiveData<String> listMostVisitedShops(Integer type, Integer count, Integer x) {
        return addToRequestQueue(APIURLUtil.LIST_MOST_VISITED_SHOPS_URL,
                APIParamsUtil.createListMostVisitedShopsParams(type, count, x));
    }

    public LiveData<String> listNearestShops(Integer type, Integer count, Integer x) {
        return addToRequestQueue(APIURLUtil.LIST_MOST_VISITED_SHOPS_URL,
                APIParamsUtil.createListNearestShopsParams(type, count, x));
    }

    public LiveData<String> listCategories() {
        return addToRequestQueue(APIURLUtil.LIST_CATEGORIES_URL,
                APIParamsUtil.createListCategoriesParams());
    }

    public LiveData<String> addShop(String shop) {
        return addToRequestQueue(APIURLUtil.ADD_SHOP_URL,
                APIParamsUtil.createAddShopParams(shop));
    }

    public LiveData<String> contactUsWithSocial(String name, String email, String subject, String content) {
        return addToRequestQueue(APIURLUtil.CONTACT_US_WITH_MAIL_URL,
                APIParamsUtil.createContactUsWithMailParams(name, email, subject, content));
    }

    public LiveData<String> contactUsWithSocial() {
        return addToRequestQueue(APIURLUtil.CONTACT_US_WITH_SOCIAL,
                APIParamsUtil.createContactUsWithSocial());
    }

    public LiveData<String> listPackages() {
        return addToRequestQueue(APIURLUtil.LIST_PACKAGES,
                APIParamsUtil.createListPackagesParams());
    }

    //    public LiveData<String>(){
//        return addToRequestQueue(APIURLUtil.,
//                APIParamsUtil.);
//    }

    private MutableLiveData<String> addToRequestQueue(String url, Map<String, String> params) {
        MutableLiveData<String> responseLiveData = new MutableLiveData<>();

        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST,
                url,
                responseLiveData::postValue,
                error -> {
                    responseLiveData.postValue("STRING_TO_HIDE_THE_PROGRESS_BAR");
                    showNetworkOperationError(error);
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.addToRequestQueue(request);

        return responseLiveData;
    }

    private void showNetworkOperationError(VolleyError error) {
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
    }

}
