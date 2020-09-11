package robin.scaffold.track.hook;

import android.app.Application;
import android.util.Log;

public class HookTrack {
    private static boolean activityLifeCycleRegister = false;

    public static void init(Application application) {
        if (application == null) {
            Log.e("e", "Please init with the param \"Application\"/");
            throw new RuntimeException();
        }
        if (!activityLifeCycleRegister) {
            application.registerActivityLifecycleCallbacks(new HookActivityLifecycleCallbacks());
            activityLifeCycleRegister = true;
        }
    }
}
