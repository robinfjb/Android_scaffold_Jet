package robin.scaffold.track.hook

import android.app.Application
import android.util.Log

object HookTrack {
    private var activityLifeCycleRegister = false
    fun init(application: Application?) {
        if (application == null) {
            Log.e("e", "Please init with the param \"Application\"/")
            throw RuntimeException()
        }
        if (!activityLifeCycleRegister) {
            application.registerActivityLifecycleCallbacks(HookActivityLifecycleCallbacks())

            activityLifeCycleRegister = true
        }
    }
}