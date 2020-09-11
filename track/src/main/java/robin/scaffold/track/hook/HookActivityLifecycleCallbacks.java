package robin.scaffold.track.hook;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

class HookActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    public static Set<String> activityNameSet = new HashSet();

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (!activityNameSet.contains(activity.getClass().getName())) {
            ViewGroup viewGroup = (ViewGroup)activity.getWindow().getDecorView();

            if (viewGroup != null) {
                int size = viewGroup.getChildCount();
                ProxyFrameLayout customFrameLayout = new ProxyFrameLayout(activity);

                for (int i = 0; i < size; i++) {
                    View view = viewGroup.getChildAt(i);
                    if (view != null) {
                        viewGroup.removeView(view);
                        customFrameLayout.addView(view);
                    }
                }
                viewGroup.addView(customFrameLayout);
            }
            activityNameSet.add(activity.getClass().getName());
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
