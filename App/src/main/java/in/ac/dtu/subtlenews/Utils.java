package in.ac.dtu.subtlenews;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility class which includes stuffs that wll be used at various places
 */

public class Utils {

    public static String[] categoryMap = {"India", "World", "Entertainment" , "Technology", "Business","Science" , "Sports", "Health"};

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }
}