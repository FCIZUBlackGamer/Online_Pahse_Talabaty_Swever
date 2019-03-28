package talabaty.swever.com.online.Utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static talabaty.swever.com.online.Utils.StringUtil.*;

public class APIParamsUtil {
    private static final String API_TOKEN = "?za[ZbGNz2B}MXYZ";

    @NonNull
    public static Map<String, String> createLoginParams(String userName, String password){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_USER_NAME, userName);
        params.put(PARAM_NAME_PASSWORD, password);
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createUploadImageParams(String image, String name){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_IMAGE, image);
        params.put(PARAM_NAME_NAME, name);
        return params;
    }

    @NonNull
    public static Map<String, String> createAddUserParams(String user){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_USER, user);
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListProductsParams(int count){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListOffersParams(int count){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createListShopsParams(int count){
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }
}
