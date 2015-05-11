package io.github.ayinloya.flittera;

import android.app.Application;
import android.content.Context;

/**
 * Created by barnabas on 4/29/15.
 */
public class MyApplication extends Application {
    public static String CUSTOMER_KEY = "cfkShihTzAt8lSnkVyY55uep3";
    public static String CUSTOMER_SECRET = "nqek9o7ZGe0yMArRQJ10aH0sLcKtT7qm5tu2chqpt12w85wtyv";
    public static String APP_ID = "HRVMyzB0jj91QuuxY70FR42FF7FeH5WB1v89gXqa";
    public static String CLIENT_KEY = "4mcv0h0rSxEQ6WlmuLM4JizQOZmCfsQwJryUIDvE";

    private static MyApplication appInstance;

    public static Context getAppInstance() {
        return appInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
    }
}
