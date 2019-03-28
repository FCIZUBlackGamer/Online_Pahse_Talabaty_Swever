package talabaty.swever.com.online.Utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_COUNT;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_IMAGE;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_NAME;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_PASSWORD;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_TOKEN;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_TYPE;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_USER;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_USER_NAME;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_X;

public class APIParamsUtil {
    private static final String API_TOKEN = "?za[ZbGNz2B}MXYZ";

    @NonNull
    public static Map<String, String> createLoginParams(String userName, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_USER_NAME, userName);
        params.put(PARAM_NAME_PASSWORD, password);
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createUploadImageParams(String image, String name) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_IMAGE, image);
        params.put(PARAM_NAME_NAME, name);
        return params;
    }

    @NonNull
    public static Map<String, String> createAddUserParams(String user) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_USER, user);
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListProductsParams(Integer count) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListOffersParams(Integer count) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListShopsParams(Integer count) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListMostVisitedShopsParams(Integer type, Integer count, Integer x) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_TYPE, String.valueOf(type));
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_X, String.valueOf(x));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListNearestShopsParams(Integer type, Integer count, Integer x) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_TYPE, String.valueOf(type));
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_X, String.valueOf(x));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }
}
