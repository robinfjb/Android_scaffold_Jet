package robin.scaffold.jet

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import robin.scaffold.track.hook.HookTrack

class SampleApp : Application(), LifecycleObserver {
    companion object {
        private lateinit var instance: SampleApp
        var isAppInForeground = false
        fun getAppContext(): Context =
                instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        HookTrack.init(this)
    }


    /**
     * Callback when the app is open but backgrounded
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        isAppInForeground = false
    }

    /**
     * Callback when the app is foregrounded
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isAppInForeground = true
    }
}