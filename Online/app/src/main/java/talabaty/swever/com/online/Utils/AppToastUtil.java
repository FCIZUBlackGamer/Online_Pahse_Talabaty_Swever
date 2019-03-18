package talabaty.swever.com.online.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import talabaty.swever.com.online.R;

public class AppToastUtil {
    private static Toast mErrorToast = null;
    private static Toast mWarningToast = null;
    private static Toast mInfoToast = null;

    private static final int TOAST_MARGIN_BOTTOM = 16;

    public static final int LENGTH_LONG = Toast.LENGTH_LONG;
    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;

    @NonNull
    public static Toast showErrorToast(@NonNull String message, int duration, @NonNull Context context) {
        initializeErrorToast(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_error, null);
        TextView text = layout.findViewById(R.id.tv_error_toast);
        text.setText(message);

        mErrorToast.setView(layout);
        mErrorToast.setDuration(duration);

        mErrorToast.show();

        return mErrorToast;
    }

    private static void initializeErrorToast(@NonNull Context context) {
        if (mErrorToast == null) {
            mErrorToast = new Toast(context);
            mErrorToast.setGravity(Gravity.BOTTOM, 0, TOAST_MARGIN_BOTTOM);
        }
    }

    @NonNull
    public static Toast showWarningToast(@NonNull String message, int duration, @NonNull Context context) {
        initializeWarningToast(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_warning, null);
        TextView text = layout.findViewById(R.id.tv_warning_toast);
        text.setText(message);

        mWarningToast.setView(layout);
        mWarningToast.setDuration(duration);

        mWarningToast.show();

        return mWarningToast;
    }

    private static void initializeWarningToast(@NonNull Context context) {
        if (mWarningToast == null) {
            mWarningToast = new Toast(context);
            mWarningToast.setGravity(Gravity.BOTTOM, 0, TOAST_MARGIN_BOTTOM);
        }
    }

    @NonNull
    public static Toast showInfoToast(@NonNull String message, int duration, @NonNull Context context) {
        initializeInfoToast(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_info, null);
        TextView text = layout.findViewById(R.id.tv_info_toast);
        text.setText(message);

        mInfoToast.setView(layout);
        mInfoToast.setDuration(duration);

        mInfoToast.show();

        return mInfoToast;
    }

    private static void initializeInfoToast(@NonNull Context context) {
        if (mInfoToast == null) {
            mInfoToast = new Toast(context);
            mInfoToast.setGravity(Gravity.BOTTOM, 0, TOAST_MARGIN_BOTTOM);
        }
    }
}
