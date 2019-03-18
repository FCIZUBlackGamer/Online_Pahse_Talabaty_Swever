package talabaty.swever.com.online.Utils;

import com.google.gson.Gson;

import org.json.JSONException;

import androidx.annotation.NonNull;
import talabaty.swever.com.online.UserModel;

public class JSONParserUtil {

    @NonNull
    public static UserModel parseUserDetails(String userDetails) throws JSONException {
        Gson gson = new Gson();
       return gson.fromJson(userDetails, UserModel.class);
    }
}
