package talabaty.swever.com.online.Utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_COUNT;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_EMAIL;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_IMAGE;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_MESSAGE;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_NAME;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_PASSWORD;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_SELL_ONLINE;
import static talabaty.swever.com.online.Utils.StringUtil.PARAM_NAME_SUBJECT;
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
        return create_Count_Token_Param(count);
    }

    @NonNull
    public static Map<String, String> createListOffersParams(Integer count) {
        return create_Count_Token_Param(count);
    }

    @NonNull
    public static Map<String, String> createListShopsParams(Integer count) {
        return create_Count_Token_Param(count);
    }

    @NonNull
    public static Map<String, String> createListMostVisitedShopsParams(Integer type, Integer count, Integer x) {
        return create_Count_Type_X_TOKEN_Param(type, count, x);
    }

    @NonNull
    public static Map<String, String> createListNearestShopsParams(Integer type, Integer count, Integer x) {
        return create_Count_Type_X_TOKEN_Param(type, count, x);
    }

    @NonNull
    public static Map<String, String> createListCategoriesParams() {
        return createTokenParam();
    }

    @NonNull
    public static Map<String, String> createAddShopParams(String shop) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_SELL_ONLINE, shop);
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createContactUsWithMailParams(String name, String email, String subject, String content) {
        HashMap<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_NAME, name);
        params.put(PARAM_NAME_EMAIL, email);
        params.put(PARAM_NAME_SUBJECT, subject);
        params.put(PARAM_NAME_MESSAGE, content);
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    public static Map<String, String> createContactUsWithSocial() {
        return createTokenParam();
    }

    @NonNull
    public static Map<String, String> createListPackagesParams() {
        return createTokenParam();
    }

    @NonNull
    private static Map<String, String> createTokenParam() {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_TOKEN, API_TOKEN);
        return params;
    }

    @NonNull
    private static Map<String, String> create_Count_Token_Param(int count) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_COUNT, String.valueOf(count));
        params.putAll(createTokenParam());
        return params;
    }

    @NonNull
    private static Map<String, String> create_Count_Type_X_TOKEN_Param(Integer type, Integer count, Integer x) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME_TYPE, String.valueOf(type));
        params.put(PARAM_NAME_X, String.valueOf(x));
        params.putAll(create_Count_Token_Param(count));
        return params;
    }
}
