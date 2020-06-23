package robin.scaffold.jet

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import robin.scaffold.jet.component.SingleLiveEvent
import robin.scaffold.jet.utils.Preference


class ActivityViewModel(application: Application): AndroidViewModel(application) {
    private val context = application.applicationContext

    private val _fabClickLivaData = SingleLiveEvent<Any>()
    val fabClickLivaData: LiveData<Any>
        get() = _fabClickLivaData

    fun fabClick() {
        _fabClickLivaData.call()
    }
}