package talabaty.swever.com.online.Utils;
import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRequestQueue {

    private static VolleyRequestQueue mInstance;
    private static Application mApplicationContext;
    private RequestQueue mRequestQueue;

    private VolleyRequestQueue(Application application) {
        mApplicationContext = application;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Application application) {
        if (mInstance == null) {
            mInstance = new VolleyRequestQueue(application);
        }
        return mInstance;
    }

    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mApplicationContext);
            mRequestQueue.start();
            //TODO response time
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

//    public void setImageFromUrl(NetworkImageView networkImageView, String url) {
//        if (mImageRequester == null)
//            mImageRequester = ImageRequester.getInstance(mApplicationContext);
//
//        mImageRequester.setImageFromUrl(networkImageView, url);
//    }
}