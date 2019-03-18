package talabaty.swever.com.online.Utils;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

public class APIParamsUtil {
//    private static final String BASE_URL = "http://onlineapi.rivile.com/";
    private static final String BASE_URL = "http://192.168.0.106/OnlineAPI/";

    public static final String IMAGE_BASE_URL = "http://selltlbaty.rivile.com/";
    public static final String API_TOKEN = "?za[ZbGNz2B}MXYZ";
    public static final String LOGIN_URL = BASE_URL + "/LoginActivity/LoginActivity";

    @NonNull
    public static Map<String, String> createLoginParams(String userName, String password){
        Map<String, String> params = new HashMap<>();
        params.put("Name", userName);
        params.put("Password", password);
        params.put("token", APIParamsUtil.API_TOKEN);
        return params;
    }
}
